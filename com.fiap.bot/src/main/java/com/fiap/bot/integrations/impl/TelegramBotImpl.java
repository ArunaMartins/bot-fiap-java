package com.fiap.bot.integrations.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fiap.bot.exceptions.CouldNotConnectToBotException;
import com.fiap.bot.exceptions.NotSupportedMessageTypeException;
import com.fiap.bot.integrations.abstracts.AbstractBot;
import com.fiap.bot.integrations.abstracts.AbstractInteracao;
import com.fiap.bot.integrations.enums.MessageTypes;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class TelegramBotImpl extends AbstractBot {

	private static final String chaveBot = "1218848996:AAEaq23sqJbLhx3hOwriDrdp_C0PmyTMAA8";

	public TelegramBotImpl() throws CouldNotConnectToBotException {
		super();
	}

	private TelegramBot bot;
	private SendResponse sendResponse;
	private int idMensagemInicial = 0;

	@Override
	protected void conectarBot() throws CouldNotConnectToBotException {
		try {
			this.bot = TelegramBotAdapter.build(chaveBot);
		} catch (Exception e) {
			throw new CouldNotConnectToBotException(
					"Não foi possível se conectar ao Bot do Telegram com a chave de acesso fornecida. Verique as configurações no arquivo de propriedades.",
					e.getCause());
		}
	}

	@Override
	public boolean enviaMensagem(AbstractInteracao interacao) {
		Update update = (Update) interacao.getObjetoManipuladorDaMensagem();
		this.enviaSinalDeDigitandoParaOChat(update);
		sendResponse = bot.execute(new SendMessage(update.message().chat().id(), interacao.getMensagemResposta()));
		return sendResponse.isOk();
	}

	private void enviaSinalDeDigitandoParaOChat(Update update) {
		this.bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

	@Override
	public List<AbstractInteracao> obtemInteracoesComOBot(int numeroMensagens) throws NotSupportedMessageTypeException {

		// Cria objeto de recebimento de mensagens
		GetUpdatesResponse updatesResponse;
		updatesResponse = bot.execute(new GetUpdates().limit(numeroMensagens).offset(idMensagemInicial));

		// Recupera a lista de mensagens solicitada e começa a interação pra retornar
		List<Update> updates = updatesResponse.updates();

		List<AbstractInteracao> listaDeInteracoes = new ArrayList<AbstractInteracao>();

		for (Update update : updates) {
			// Incrementa o offset
			idMensagemInicial = update.updateId() + 1;

			if (update.message().audio() != null) {
				throw new NotSupportedMessageTypeException(MessageTypes.AUDIO.getNome());
			}

			String mensagemEnviada = update.message().text();
			if (mensagemEnviada != null) {
				InteracaoTelegram it = new InteracaoTelegram(update);
				it.setDataHoraMensagem(LocalDateTime.now());
				listaDeInteracoes.add(it);
			}

		}

		return listaDeInteracoes;
	}

}
