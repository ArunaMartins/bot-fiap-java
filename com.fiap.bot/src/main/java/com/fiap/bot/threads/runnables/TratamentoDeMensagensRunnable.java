package com.fiap.bot.threads.runnables;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.fiap.bot.integrations.abstracts.AbstractInteracao;
import com.fiap.bot.integrations.impl.TelegramBotImpl;

public class TratamentoDeMensagensRunnable implements Runnable {

	private TelegramBotImpl bot;
	private Map<Long, AbstractInteracao> mapaDeConversasPorUsuario;

	public TratamentoDeMensagensRunnable(TelegramBotImpl bot, Map<Long, AbstractInteracao> mapaDeConversasPorUsuario) {
		this.bot = bot;
		this.mapaDeConversasPorUsuario = mapaDeConversasPorUsuario;
	}

	@Override
	public void run() {
		while (true) {
			List<AbstractInteracao> interacoes = this.bot.obtemInteracoesComOBot(10);
			for(AbstractInteracao interacao : interacoes) {
				// Recupera Id do Usuario Telegram que interagiu			
				long idUsuarioInteracao = interacao.getIdContatoInteracao();
				if(this.mapaDeConversasPorUsuario.containsKey(idUsuarioInteracao)) {
					System.out.println("Usuario já tem uma conversa ativa. Por enquanto ignorar.");
				}
				// Cria uma nova conversa no mapa
				this.mapaDeConversasPorUsuario.put(idUsuarioInteracao, interacao);
				
				LocalDateTime dataHoraMensagem = interacao.getDataHoraMensagem();
				System.out.println("Data/Hora da mensagem: "
						+ dataHoraMensagem.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
				System.out.println("ID do Usuario   : " + idUsuarioInteracao + " - "
						+ interacao.getNomeContatoInteracao());
				System.out.println("ID da Mensagem  : " + interacao.getIdMensagem());
				String mensagem = interacao.getMensagemEnviadaPeloUsuario();
				this.trataMensagemRecebida(mensagem, interacao);		
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void trataMensagemRecebida(String mensagem, AbstractInteracao interacao) {
		if (!mensagem.equals("/start") && !mensagem.equals("/previsao") && !mensagem.equals("/pedido")
				&& !mensagem.equals("/localizacao")) {
			interacao.setMensagemResposta("Desculpe... não entendi. Para começar, digite /start.");
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
