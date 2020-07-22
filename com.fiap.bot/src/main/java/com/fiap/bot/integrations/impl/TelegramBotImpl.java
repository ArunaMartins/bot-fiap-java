package com.fiap.bot.integrations.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fiap.bot.exceptions.CouldNotConnectToBotException;
import com.fiap.bot.integrations.abstracts.AbstractBot;
import com.fiap.bot.integrations.abstracts.AbstractInteracao;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

/**
 * Esta classe é a implementação efetiva do Bot de integração com o Telegram.
 * Ela é responsável por conectar no Bot através da chave de acesso e interagir
 * com a biblioteca de integração com o Bot (TelegramBot.jar);
 * 
 * @author Carlos Eduardo Roque da Silva
 *
 */
public class TelegramBotImpl extends AbstractBot {

	// private static final String chaveBot = "1245760106:AAER1kXER5VM334i0vWKK9Y98aCSQikmSF8";

	

	private TelegramBot bot;
	private SendResponse sendResponse;
	private int idMensagemInicial = 0;

	/**
	 * Construtor da classe que interage com o Telegram
	 * @param chaveBotTelegram A chave gerada pelo Telegram para manipular o seu Bot
	 * @throws CouldNotConnectToBotException Exceção lançada caso a conexão não tenha sido possível
	 */
	public TelegramBotImpl(String chaveBotTelegram) throws CouldNotConnectToBotException {
		super(chaveBotTelegram);
	}

	
	/**
	 * Método que cria os objetos necessários para interagir com o Telegram
	 */
	@Override
	protected void conectarBot(String chave) throws CouldNotConnectToBotException {
		try {
			this.bot = TelegramBotAdapter.build(chave);
		} catch (Exception e) {
			throw new CouldNotConnectToBotException(
					"Não foi possível se conectar ao Bot do Telegram com a chave de acesso fornecida. Verique as configurações no arquivo de propriedades.",
					e.getCause());
		}
	}

	/**
	 * Método que envia a mensagem para um contato que fez uma interação recebida pelo Bot via Telegram
	 */
	@Override
	public boolean enviaMensagem(AbstractInteracao interacao) {
		Update update = (Update) interacao.getObjetoManipuladorDaMensagem();
		this.enviaSinalDeDigitandoParaOChat(update);
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), interacao.getMensagemResposta()));
		return sendResponse.isOk();
	}

	/**
	 * Método interno que envia o label "Digitando..." para o Bot do Telegram
	 * @param update O objeto de manipulação da interação do Telegram
	 */
	private void enviaSinalDeDigitandoParaOChat(Update update) {
		this.bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

	/**
	 * Método que obtem as interações do telegram
	 * @param numeroMensagens O número de interações que o método consultará a cada invocação
	 */
	@Override
	public List<AbstractInteracao> obtemInteracoesComOBot(int numeroMensagens) {

		// Cria objeto de recebimento de mensagens
		GetUpdatesResponse updatesResponse;
		updatesResponse = bot.execute(new GetUpdates().limit(numeroMensagens).offset(idMensagemInicial));

		// Recupera a lista de mensagens solicitada e começa a interação pra retornar
		List<Update> updates = updatesResponse.updates();

		List<AbstractInteracao> listaDeInteracoes = new ArrayList<AbstractInteracao>();

		if (updates != null) {
			for (Update update : updates) {
				// Incrementa o offset
				idMensagemInicial = update.updateId() + 1;

//				if (update.message().audio() != null) {
//					throw new NotSupportedMessageTypeException(MessageTypes.AUDIO.getNome());
//				}

				String mensagemEnviada = update.message().text();
				if (mensagemEnviada != null) {
					InteracaoTelegram it = new InteracaoTelegram(update);
					LocalDateTime date = LocalDateTime.of(1970, 01, 01, 0, 0);
					date = date.plusSeconds(update.message().date()); // A data é passada em segundos pelo bot.
					date = date.minusHours(3); // Diminui em 3hs por conta do timezone UTC-3
					it.setDataHoraMensagem(date);
					listaDeInteracoes.add(it);
				}
			}

		} else {
			System.out.println(
					"Não foi possível conectar na API do Telegram. Provavelmente outra pessoa está conectado.");
		}

		return listaDeInteracoes;
	}

	/**
	 * Método que avalia se a conexão com o Telegram está ativa
	 * @return
	 */
	public boolean isConnected() {
		// TODO buscar a forma de validar se esta conexão é valida

		return true;
	}

	@Override
	public boolean enviaMensagem(Long idConversa, String mensagem) {
		sendResponse = bot.execute(new SendMessage(idConversa, mensagem));
		return sendResponse.isOk();
	}

}
