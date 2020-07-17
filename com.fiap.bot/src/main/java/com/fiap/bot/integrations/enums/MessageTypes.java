package com.fiap.bot.integrations.enums;

public enum MessageTypes {
	
	AUDIO("Audio", 1);

	private String nome;
	private int valor;

	MessageTypes(String nome, int valor) {
		this.nome = nome;
		this.valor = valor;
	}

	public String getNome() {
		return nome;
	}

	public int getValor() {
		return valor;
	}
	
}
