package com.fiap.bot.integrations.abstracts;

import java.time.LocalDateTime;

public abstract class AbstractInteracao {
	
	private LocalDateTime dataHoraMensagem;
	private String mensagemResposta; // Será usado nas classes filhas para enviar a resposta

	public abstract String getMensagemEnviadaPeloUsuario();
	
	public abstract String getNomeContatoInteracao();
	
	public abstract long getIdContatoInteracao();

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