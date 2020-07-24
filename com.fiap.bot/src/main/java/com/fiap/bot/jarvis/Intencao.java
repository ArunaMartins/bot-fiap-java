package com.fiap.bot.jarvis;

import com.fiap.bot.integrations.enums.Intencoes;

public class Intencao {
    public static Intencoes Identificar(String mensagem) {
        // TODO: REGEX

        if (mensagem.equalsIgnoreCase("/confirmarPedido")) {
            return Intencoes.CONFIRMAR_PEDIDO;
        }

        if (mensagem.equalsIgnoreCase("pizza de queijo")) {
            return Intencoes.PEDIDO_EM_ANDAMENTO;
        }

        if (mensagem.equalsIgnoreCase("/finalizarPedido")) {
            return Intencoes.FINALIZAR_PEDIDO;
        }

        return Intencoes.NOVO_PEDIDO;
    }

    public static String ObterCEP(String mensagem) {
        return mensagem;
    }
}