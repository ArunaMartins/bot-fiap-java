package com.fiap.bot.integrations.abstracts;

import java.time.LocalDateTime;

/**
 * Classe abstrata que representa uma interação recebida pela Bot por parte de um usuário
 * @author Carlos Eduardo Roque da Silva
 *
 */
public abstract class AbstractInteracao {
	
	private LocalDateTime dataHoraMensagem;
	private String mensagemResposta; // Será usado nas classes filhas para enviar a resposta

	/**
	 * Método que retorna a mensagem enviada pelo usuário que interagiu com o Bot
	 * @return A mensagem enviada
	 */
	public abstract String getMensagemEnviadaPeloUsuario();
	
	/**
	 * Método que retorna o nome do usuário que interagiu com o Bot
	 * @return O nome do usuario
	 */
	public abstract String getNomeContatoInteracao();
	
	
	/**
	 * Método que retorna o id do usuário que interagiu com o Bot
	 * @return O id do usuario que interagiu com o Bot
	 */
	public abstract long getIdContatoInteracao();

	/**
	 * Método que retorna o objeto interno manipulador da interação dentro do Bot
	 * @return
	 */
	public abstract Object getObjetoManipuladorDaMensagem();
	
	public String getMensagemResposta() {
		return mensagemResposta;
	}

	public void setDataHoraMensagem(LocalDateTime dataHoraMensagem) {
		this.dataHoraMensagem = dataHoraMensagem;
	}
	
	public void setMensagemResposta(String mensagemResposta) {
		this.mensagemResposta = mensagemResposta;
	}
	
	public LocalDateTime getDataHoraMensagem() {
		return dataHoraMensagem;
	}

	public abstract int getIdMensagem();
}