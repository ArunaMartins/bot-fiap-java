package com.fiap.bot.main;

import com.fiap.bot.exceptions.CouldNotConnectToBotException;
import com.fiap.bot.principal.GerenciadorBotTelegramFIAP;

/**
 * Classe inicial do GerenciadorBotTelegramFIAP que será chamado pela JVM
 * @author Carlos Eduardo Roque da Silva
 *
 */
public class GerenciadorBotFiapMain 
{
    public static void main( String[] args )
    {
    	// Inicia o programa gerenciador de mensagens do Bot do Telegram
    	GerenciadorBotTelegramFIAP gerenciador = new GerenciadorBotTelegramFIAP();
    	try {
    		// Iniciando...
			gerenciador.iniciarBot();
		} catch (CouldNotConnectToBotException e) {
			// TODO Auto-generated catch block
			// TODO Add log4J
			e.printStackTrace();
		}
    }
}
