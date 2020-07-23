package com.fiap.bot.jarvis;

import com.fiap.bot.integrations.enums.Intencoes;

public class Intencao {
    public static Intencoes Identificar(String mensagem) {
        //TODO: REGEX

        
        return Intencoes.NOVO_PEDIDO;
    }
    public static String ObterCEP(String mensagem){
        return "numero";
    }
}