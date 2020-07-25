package com.fiap.bot.jarvis;

import com.fiap.bot.integrations.enums.Intencoes;
import com.fiap.bot.integrations.enums.Pizzas;

import java.util.regex.*;
/**
 * Classe de intenções para identificar o status do pedido enviado para o cliente;
 * @author Ayton Henrique
 *
 */

public class Intencao {
	/**
	 * Método para identificar o status do pedido de acordo com o comando enviado
	 * @param mensagem do bot
	 * @return intencoes com os status do pedido
	 */
    public static Intencoes identificar(String mensagem) {
        // TODO: REGEX
        Intencoes _intencao = Intencoes.NOVO_PEDIDO;
        if (mensagem.equalsIgnoreCase("/confirmarPedido")) {
            return Intencoes.CONFIRMAR_PEDIDO;
        }

        for (Pizzas pizza : Pizzas.values()) {
            if(mensagem.trim().equalsIgnoreCase("/" + pizza)) return Intencoes.PEDIDO_EM_ANDAMENTO;
        }

        if (mensagem.equalsIgnoreCase("/finalizarPedido")) {
            return Intencoes.FINALIZAR_PEDIDO;
        }

        return Intencoes.NOVO_PEDIDO;
    }

    /**
	 * Método para efetuar a busca do endereço pelo cep informado pelo cliente.
	 * @param mensagem cep informado na mensagem do bot
	 * @return numeracao do cep
	 */
    public static String obterCEP(String mensagem) {
        Pattern p = Pattern.compile("(\\d)+");
        Matcher m = p.matcher(mensagem);
        m.find();
        // Matcher m = p.matcher(stringToSearch);
        return m.group();
    }
}