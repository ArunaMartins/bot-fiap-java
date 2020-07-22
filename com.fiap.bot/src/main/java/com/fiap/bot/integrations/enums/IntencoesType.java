package com.fiap.bot.integrations.enums;

/**
 * Este Enum é responsavel por classificar os tipos dos pedidos enviados
 * @author Ayrton
 *
 */

public enum IntencoesType {
    NOVO_PEDIDO("NOVO_PEDIDO",1),
    CONFIRMAR_PEDIDO("CONFIRMAR_PEDIDO",2),
    CONFIRMAR_ENDERECO("CONFIRMAR_ENDERECO",3),
    FINALIZAR_PEDIDO("FINALIZAR_PEDIDO",4);
    
    
    private String nome;
	private int valor;
	
	
	private IntencoesType() {
		// TODO Auto-generated constructor stub
	}

	private IntencoesType(String nome, int valor) {
		this.nome = nome;
		this.valor = valor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
	
	
}