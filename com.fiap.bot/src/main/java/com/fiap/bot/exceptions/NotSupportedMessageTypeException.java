package com.fiap.bot.exceptions;

public class NotSupportedMessageTypeException extends Exception {

	public NotSupportedMessageTypeException(String nome) {
		super("Não é possível tratar mensagens de " + nome);
	}
	
}
