package com.fiap.bot.jarvis;

import com.fiap.bot.integrations.enums.Intencoes;
import com.fiap.bot.integrations.enums.Pizzas;

import java.util.regex.*;

public class Intencao {
    public static Intencoes Identificar(String mensagem) {
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

    public static String ObterCEP(String mensagem) {
        Pattern p = Pattern.compile("(\\d)+");
        Matcher m = p.matcher(mensagem);
        m.find();
        // Matcher m = p.matcher(stringToSearch);
        return m.group();
    }
}