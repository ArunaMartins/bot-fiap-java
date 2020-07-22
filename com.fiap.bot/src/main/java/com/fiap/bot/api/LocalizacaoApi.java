package com.fiap.bot.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Esta classe é utilizada para buscar informações de 
 * localização a partir de um CEP
 * 
 * @author Willian
 *
 */
public class LocalizacaoApi {
	/**
	 * Método de busca de Consulta do CEP
	 * @param codigoCEP Código do CEP a ser consultado
	 */
	public Localizacao consultaCEP(String codigoCEP){
		Localizacao localizacao = new Localizacao();
		try {
			codigoCEP = codigoCEP.replace("-", "");
			
			if (codigoCEP.isEmpty())
				throw new Exception("CEP não informado!");
				
            URL url = new URL("https://viacep.com.br/ws/" + codigoCEP + "/json/");//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Falha: Erro HTTP Codigo: "
                        + conn.getResponseCode());
            }
            
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            
            String output = "";
            while ((output = br.readLine()) != null) { 
            	output = output.trim();
            	if (output.indexOf(":") > -1) {
            		String[] itens = output.split(":");
            		itens[1] = itens[1].substring(1, itens[1].length()-1).replaceAll("\"", ""); // Remove
            		
            		switch (itens[0]) {
	                	case "\"cep\"":
	                		localizacao.setCep(itens[1]);
	    					break;
	                	case "\"logradouro\"":
	                		localizacao.setLogradouro(itens[1]);
	    					break;
	                	case "\"bairro\"":
	                		localizacao.setBairro(itens[1]);
	    					break;
	                	case "\"localidade\"":
	                		localizacao.setCidade(itens[1]);
	    					break;
	                	case "\"uf\"":
	                		localizacao.setEstado(itens[1]);
	    					break;
	                	case "\"ibge\"":
	    					break;
	                	case "\"gia\"":
	                		break;    					
	    				default:
	    					break;
    				}
            	}
           	            	            	
            }
            System.out.println("Dados do CEP encontrado");
            System.out.println(localizacao.toString());
            conn.disconnect();
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar dados do CEP " + e);
        }
		
		return localizacao;
	}

}
