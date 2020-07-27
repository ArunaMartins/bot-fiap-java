package com.fiap.bot.jarvis;

import java.util.ArrayList;
import java.util.List;

public class CompraFinalizada {
	
	private List<Pedido> pedidosFeitosNestaCompra = new ArrayList<Pedido>();
	private String cep;
	private String endereco;

	public void setCEPEntrega(String cep) {
		this.cep = cep;		
	}

	public void setEnderecoEntrega(String endereco) {
		this.endereco = endereco;
	}

	public void setPedidosFeitosNestaCompra(List<Pedido> pedidos) {
		this.pedidosFeitosNestaCompra.addAll(pedidos);
	}

	public List<Pedido> getPedidosFeitosNestaCompra() {
		return pedidosFeitosNestaCompra;
	}

	public String getCep() {
		return cep;
	}

	public String getEndereco() {
		return endereco;
	}
	
	
}
