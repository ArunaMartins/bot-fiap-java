package com.fiap.bot.jarvis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Classe para iniciar a conversa no chatbot
 * @author Ayton Henrique
 *
 */

public class JarvisBot {

    private static ArrayList<Conversa> _conversas = new ArrayList<Conversa>();

    public static Conversa iniciarConversa(Long idConversa, String Nome) {
        Optional<Conversa> conversa = _conversas.stream().filter(c -> c.getIdConversa().equals(idConversa)).findFirst();

        if (conversa.isPresent()) {
            return conversa.get();
        }
        
        Conversa novaConversa = new Conversa(idConversa, Nome);
        _conversas.add(novaConversa);
        return novaConversa;
    }

    public static void encerrarConversa() {

    }

}