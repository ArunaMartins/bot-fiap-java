package com.fiap.bot.integrations.abstracts;

import java.util.List;

import com.fiap.bot.exceptions.CouldNotConnectToBotException;
import com.fiap.bot.exceptions.NotSupportedMessageTypeException;

public abstract class AbstractBot {
	
	public AbstractBot() throws CouldNotConnectToBotException {
		this.conectarBot();
	}

	protected abstract void conectarBot() throws CouldNotConnectToBotException;
	
	public abstract boolean enviaMensagem(AbstractInteracao interacao);
	
	public abstract List<AbstractInteracao> obtemInteracoesComOBot(int numeroMensagens) throws NotSupportedMessageTypeException;
	
}
