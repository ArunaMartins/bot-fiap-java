package com.fiap.bot.principal;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

import com.fiap.bot.exceptions.CouldNotConnectToBotException;
import com.fiap.bot.integrations.abstracts.AbstractInteracao;
import com.fiap.bot.integrations.impl.TelegramBotImpl;
import com.fiap.bot.threads.runnables.TratamentoDeMensagensRunnable;

/**
 * Esta classe é responsável por iniciar o programa gerenciador do Bot do
 * Telegram da Pizzaria FIAP
 * 
 * @author Carlos Eduardo Roque da Silva
 *
 */
public class GerenciadorBotTelegramFIAP {

	private Map<Long, AbstractInteracao> listaDeMensagens = new HashMap<Long, AbstractInteracao>();
	private Thread threadTratamentoDeMensagens;
	private String mensagem;

	/**
	 * Este método inicia o programa. Ele carrega os recursos necessários para o
	 * funcionamento correto do gerenciador
	 * 
	 * @throws CouldNotConnectToBotException Exceção que ocorre caso não seja
	 *                                       possível se conectar no Bot
	 */
	public void iniciarBot() throws CouldNotConnectToBotException {

		System.out.println("Gerenciador do Bot do Telegram da Pizzaria FIAP");
		System.out.println("***********************************************");
		System.out.println("Iniciando conexão com o Bot do Telegram...");

		// Recuperando arquivo params.properties
		System.out.println("Recuperando arquivos de configurações...");
		Properties arquivoConfiguracao = this.recuperaArquivoDeConfiguracoes();

		// Valida se arquivo foi recuperado com sucesso
		if (!this.arquivoDeConfiguracaoValido(arquivoConfiguracao))
			return;

		// Recupera a chave de conexão com o Telegram do arquivo de configuração
		System.out.println("Recuperando chave de conexão com o Bot do Telegram do arquivo de configurações...");
		String chaveBotTelegram = this.recuperaChaveBot(arquivoConfiguracao);

		// Valida se a chave é valida
		if (this.isChaveNullOrEmpty(chaveBotTelegram))
			return;

		// Iniciando a conexão com o Bot e validando se a conexão está válida
		System.out.println("Conectando no Bot do Telegram...");
		TelegramBotImpl bot = new TelegramBotImpl(chaveBotTelegram);
		boolean conectado = bot.isConnected();
		System.out.println("Bot Conectado? " + conectado);
		if (conectado) {
			this.iniciaThreadDeTratamentoDasMensagensDoTelegram(bot);
			System.out.println("Todos os componentes carregados...\n\n\n");
			this.iniciaMenusDeConsultaDoGerenciador();
		}
	}

	private boolean isChaveNullOrEmpty(String chaveBotTelegram) {
		boolean chaveInvalida = chaveBotTelegram == null || chaveBotTelegram.isEmpty();
		if (chaveInvalida)
			System.out.println("Não foi possível recuperar chave do BOT do arquivo de configurações.");
		else
			System.out.println("Chave recuperada.");
		return chaveInvalida;
	}

	private boolean arquivoDeConfiguracaoValido(Properties arquivoConfiguracao) {
		if (arquivoConfiguracao == null) {
			System.out.println("Não foi possível recuperar o arquivo de configurações.");
			return false;
		}

		System.out.println("Arquivo de configuração recuperado.");
		return true;
	}

	private void iniciaMenusDeConsultaDoGerenciador() {
		Scanner s = new Scanner(System.in);
		boolean processa = true;
		while (processa) {

			System.out.println("******************************************************************");
			System.out.println("	      Gerenciador do Bot do Telegram da Pizzaria FIAP         ");
			System.out.println("******************************************************************");
			System.out.println("	Escolha uma das opções para iniciar:      ");
			System.out.println("                                              ");
			System.out.println(" 1. Consultar número de conversas tratadas pelo Bot");
			System.out.println(" 2. Consultar número de pedidos feitos na pizzaria");
			System.out.println(" 3. Consultar total vendido pela pizzaria hoje");
			System.out.println(" 4. Sair");
			System.out.println("\n");
			
			if(mensagem != null) {
				System.out.println(mensagem + "\n");
				mensagem = null;
			}
			
			String valorLido = lerTela(s);
			this.trataLeituraTela(valorLido);
		}
		s.close();
	}

	private void trataLeituraTela(String valorLido) {
		switch (valorLido) {
		case "1":
			this.consultaNumeroDeConversasTratadasNoBot();
			break;
		case "2":
			this.consultaNumeroDePedidosFeitosNaPizzaria();
			break;
		case "3":
			this.consultaTotalVendidoPelaPizzaria();
			break;
		case "4":
			this.finalizaExecucaoDoPrograma();
			break;
		default:
			mensagem = "Digite uma opção válida (1, 2, 3, 4)";
		}

	}

	private void consultaTotalVendidoPelaPizzaria() {
		double total = this.listaDeMensagens.entrySet().size();
		System.out.println("O valor total dos pedidos feitos por usuários tratados pelo Bot é de: " + total);
		System.out.println("\n\n");
	}

	private void consultaNumeroDePedidosFeitosNaPizzaria() {
		int size = this.listaDeMensagens.entrySet().size();
		System.out.println("O número de pedidos feitos por usuários tratados pelo Bot é de: " + size);
		System.out.println("\n\n");
	}

	private void consultaNumeroDeConversasTratadasNoBot() {
		int size = this.listaDeMensagens.entrySet().size();
		System.out.println("O número de conversas com usuários distintos tratados pelo Bot é de: " + size);
		System.out.println("\n\n");
	}

	private void finalizaExecucaoDoPrograma() {
		System.exit(0);
	}

	private String lerTela(Scanner s) {
		String valorLido = "";
		try {
			valorLido = s.nextLine();

		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
		}

		return valorLido;
	}

	private void iniciaThreadDeTratamentoDasMensagensDoTelegram(TelegramBotImpl bot) {
		System.out.println("Iniciando Thread de tratamento de mensagens...");
		this.threadTratamentoDeMensagens = new Thread(new TratamentoDeMensagensRunnable(bot, listaDeMensagens));
		this.threadTratamentoDeMensagens.start();
		System.out.println("Thread de tratamento de mensagens inicado.");
	}

	private String recuperaChaveBot(Properties arquivoConfiguracao) {
		// TODO Auto-generated method stub
		return "asdfa";
	}

	private Properties recuperaArquivoDeConfiguracoes() {
		// TODO Auto-generated method stub
		return new Properties();
	}

}
