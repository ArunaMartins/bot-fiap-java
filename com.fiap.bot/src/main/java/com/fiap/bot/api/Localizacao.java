package com.fiap.bot.api;

/**
 * Classe representando a Localização
 * @author Willian
 *
 */
public class Localizacao {
	private String cep;
	private String logradouro;
	private String bairro;
	private String cidade;
	private String estado;
	private String numero;
	
	/**
	 * Retorna o CEP
	 * @return
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * Seta o CEP
	 * @param cep
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	/**
	 * Retorna o Logradouro
	 * @return
	 */
	public String getLogradouro() {
		return logradouro;
	}
	
	/**
	 * Seta o Logradouro
	 * @param logradouro
	 */
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	
	/**
	 * Retorna o Bairro
	 * @return
	 */
	public String getBairro() {
		return bairro;
	}
	
	/**
	 * Seta o Bairro
	 * @param bairro
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	/**
	 * Retorna a Cidade
	 * @return
	 */
	public String getCidade() {
		return cidade;
	}
	
	/**
	 * Seta a Cidade
	 * @param cidade
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	/**
	 * Retorna o Estado
	 * @return
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * Seta o Estado
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * Retorna o numero
	 * @return
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * Seta o numero
	 * @param numero
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * Retorna as informações da Localização
	 */
	@Override
	public String toString(){
		return "CEP: " + this.cep + " | Estado: " + this.estado + " | Cidade: " + this.cidade + " | Bairro: " + this.bairro +
				" | Logradouro: " + this.logradouro;
		
		
	}

}
