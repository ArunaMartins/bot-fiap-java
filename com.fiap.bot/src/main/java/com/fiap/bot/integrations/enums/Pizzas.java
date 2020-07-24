package com.fiap.bot.integrations.enums;

public enum Pizzas {
    MUZZARELA("MUZZARELA","25.60"),
    PORTUGUESA("PORTUGUESA","35.60");

    private String nome;
	private String valor;
    
	Pizzas(String nome, String valor) {
		this.nome = nome;
		this.valor = valor;
    }
    public String getNome() {
		return nome;
	}

	public String getValor() {
		return valor;
	}
}