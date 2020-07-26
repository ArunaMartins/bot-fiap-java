package com.fiap.bot.jarvis;

import java.util.ArrayList;
import java.util.Optional;

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
    
    /**
     * Método auxiliar que retorna para um chamador o número de conversas manipuladas
     * @return
     */
    public static int recuperaNumeroDeConversasManipuladas() {
    	return JarvisBot._conversas.size();
    }

    /**
     * Método auxiliar que retorna o numero de pedidos feito via Bot
     * @return
     */
    public static int recuperaNumeroDePedidosEfetuadosViaBot() {
    	int retorno = 0;
    	for(Conversa c : JarvisBot._conversas) {
    		retorno += c.getPedidos().size();
    	}
    	return retorno;
    }
    
    public static void encerrarConversa() {

    }

}