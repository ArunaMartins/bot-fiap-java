package com.fiap.bot.threads.runnables;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.fiap.bot.integrations.abstracts.AbstractInteracao;
import com.fiap.bot.integrations.impl.TelegramBotImpl;

/**
 * Classe Runnable que trata a execução da leitura das interações com o Bot
 * @author Carlos Eduardo Roque da Silva
 *
 */
public class TratamentoDeMensagensRunnable implements Runnable {

	/**
	 * O objeto de interação com o Bot
	 */
	private TelegramBotImpl bot;
	/**
	 * O mapa de interações compartilhado com a Thread Principal
	 */
	private Map<Long, AbstractInteracao> mapaDeConversasPorUsuario;

	/**
	 * Construtor que recebe o objeto de interação com o Bot do Telegram e o mapa de interações por usuario da thread principal
	 * @param bot O objeto do tipo TelegramBotImpl com a conexão ativa
	 * @param mapaDeConversasPorUsuario O hashmap da lista de interaçoes por usuário
	 */
	public TratamentoDeMensagensRunnable(TelegramBotImpl bot, Map<Long, AbstractInteracao> mapaDeConversasPorUsuario) {
		this.bot = bot;
		this.mapaDeConversasPorUsuario = mapaDeConversasPorUsuario;
	}

	/**
	 * Método sobrescrito que roda eternamente a cada 1 segundo onde ele lê as interações com o Bot de 10 em 10, trata uma a uma e atualiza o mapa de interações por usuario da thread principal
	 */
	@Override
	public void run() {
		while (true) {
			// Recupera as ultimas 10 interações não lidas pelo Bot
			List<AbstractInteracao> interacoes = this.bot.obtemInteracoesComOBot(10);
			
			// Itera uma a uma
			for(AbstractInteracao interacao : interacoes) {
				
				// Recupera Id do Usuario Telegram que interagiu			
				long idUsuarioInteracao = interacao.getIdContatoInteracao();
				if(this.mapaDeConversasPorUsuario.containsKey(idUsuarioInteracao)) {
					System.out.println("Usuario já tem uma conversa ativa. Por enquanto ignorar.");
				}
				
				// Cria uma nova conversa no mapa
				this.mapaDeConversasPorUsuario.put(idUsuarioInteracao, interacao);
				
				LocalDateTime dataHoraMensagem = interacao.getDataHoraMensagem();
				System.out.println("Data/Hora da mensagem: " + dataHoraMensagem.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
				System.out.println("ID do Usuario   : " + idUsuarioInteracao + " - " + interacao.getNomeContatoInteracao());
				System.out.println("ID da Mensagem  : " + interacao.getIdMensagem());
				
				// recupera mensagem enviada pelo contato no bot para tratamento
				String mensagem = interacao.getMensagemEnviadaPeloUsuario();
				
				// Trata mensagem para dar um encaminhamento
				this.trataMensagemRecebida(mensagem, interacao);		
			}
			try {
				//Espera 2s antes de iniciar uma nova consulta
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Método que trata a mensagem enviada numa interação 
	 * @param mensagem A mensagem enviada
	 * @param interacao A interação executada por um contato do Telegram
	 */
	private void trataMensagemRecebida(String mensagem, AbstractInteracao interacao) {
		if (!mensagem.equals("/start") && !mensagem.equals("/previsao") && !mensagem.equals("/pedido")
				&& !mensagem.equals("/localizacao")) {
			interacao.setMensagemResposta("Desculpe... não entendi. Para começar, digite /start.");
			// Responde ao usuario via objeto de interação com o Bot no Telegram
			this.bot.enviaMensagem(interacao);
			return;
		}
		if (mensagem.equals("/start")) {
			interacao.setMensagemResposta("Olá! Eu sou o Bot da Pizzaria! Escolha suas opções abaixo:\n"
					+ "/pedido\n" + "/localizacao\n" + "/previsao\n" + "/sair");

			this.bot.enviaMensagem(interacao);
			return;
		}
		if (mensagem.equals("/previsao")) {
			interacao.setMensagemResposta("O tempo hoje está muito bom para uma pizza!");
			this.bot.enviaMensagem(interacao);
			return;
		}
		if (mensagem.equals("/localizacao")) {
			interacao.setMensagemResposta("Estou na Avenida Paulista, 901!!!");
			this.bot.enviaMensagem(interacao);
			return;
		}
		if (mensagem.equals("/pedido")) {
			interacao.setMensagemResposta("Legal! Já vou anotar o seu pedido!!!");
			this.bot.enviaMensagem(interacao);
			return;
		}
		if (mensagem.equals("/sair")) {
			interacao.setMensagemResposta("Até mais!!!");
			this.bot.enviaMensagem(interacao);
			return;
		}
		
	}

}
