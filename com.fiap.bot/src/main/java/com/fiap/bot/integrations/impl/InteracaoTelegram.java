package com.fiap.bot.integrations.impl;

import com.fiap.bot.integrations.abstracts.AbstractInteracao;
import com.pengrad.telegrambot.model.Update;

public class InteracaoTelegram extends AbstractInteracao {

	private Update update;
	
	public InteracaoTelegram(Update update) {
		this.update = update;
	}
	
	@Override
	public String getMensagemEnviadaPeloUsuario() {
		return this.update.message().text();
	}

	@Override
	public String getNomeContatoInteracao() {
		String firstName = this.update.message().from().firstName();
		return firstName;
	}

	@Override
	public long getIdContatoInteracao() {
		return this.update.message().from().id();
	}

	@Override
	public Object getObjetoManipuladorDaMensagem() {
		return this.update;
	}

	@Override
	public int getIdMensagem() {
		return this.update.message().messageId();
	}

}
