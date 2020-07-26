package com.fiap.bot.jarvis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fiap.bot.api.Localizacao;
import com.fiap.bot.api.LocalizacaoApi;
import com.fiap.bot.integrations.enums.Intencoes;
import com.fiap.bot.integrations.enums.Pizzas;
import com.fiap.bot.integrations.enums.StatusPedido;

/**
 * Classe de interaçao com o Cliente no telegram para identificar as mensagens
 * enviadas pelo cliente.
 * 
 * @author Ayton Henrique
 *
 */
public class Conversa {
	private List<Mensagem> mensagens = new ArrayList<Mensagem>();
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	private List<Pedido> pedidosPassados = new ArrayList<Pedido>();
	
	private String NomeCliente;
	private Long IdConversa;

	public List<Pedido> getPedidos() {
		return Collections.unmodifiableList(this.pedidos);
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public String getNomeCliente() {
		return NomeCliente;
	}

	public Long getIdConversa() {
		return this.IdConversa;
	}

	public Conversa(Long idConversa, String NomeCliente) {
		this.IdConversa = idConversa;
		this.NomeCliente = NomeCliente;
	}

	/**
	 * Método de resposta aos pedidos solicitados pelo cliente;
	 * 
	 * @param mensagem mensagens enviadas do bot
	 * @return respostas retorna uma lista de mensagens
	 */

	public ArrayList<String> responder(Mensagem mensagem) {
		// Array de respostas para serem enviadas ao final das definições de ação
		ArrayList<String> respostas = new ArrayList<String>();
		// Valida se já existe um pedido em andamento e o recupera
		Optional<Pedido> _pedidoAbertoAndamento = pedidos.stream()
				.filter(x -> x.getStatus() == StatusPedido.ABERTO || x.getStatus() == StatusPedido.ANDAMENTO)
				.findFirst();
		// Recupera a mensagem
		String _mensagem = mensagem.getTexto();

		// Verifica a intenção do usuário
		Intencoes _intencao = Intencao.identificar(_mensagem);

		if(_intencao == Intencoes.INTENCAO_DESCONHECIDA && !_pedidoAbertoAndamento.isPresent()) {
			respostas.add("Desculpe. Não entendi. Poderia digitar novamente?");
		} else {
			// Valida e norteia as possiveis escolhas do usuário
			this.recuperaCEPeFinalizaPedido(respostas, _pedidoAbertoAndamento, _mensagem, _intencao);
			this.validaNovoPedido(respostas, _pedidoAbertoAndamento, _mensagem, _intencao);
			this.validaPedidoEmAndamento(respostas, _pedidoAbertoAndamento, _mensagem, _intencao);
			this.validaConfirmacaoDoPedido(respostas, _pedidoAbertoAndamento, _mensagem, _intencao);
			this.finalizaPedido(respostas, _pedidoAbertoAndamento, _mensagem, _intencao);
			this.alteracaoDePedido(respostas, _pedidoAbertoAndamento, _mensagem, _intencao);
			this.adicionarNovasPizzas(respostas, _pedidoAbertoAndamento, _mensagem, _intencao);
			this.recomecarPedidoNovamente(respostas, _pedidoAbertoAndamento, _mensagem, _intencao);
		}
		mensagens.add(mensagem);

		return respostas;
	}

	/**
	 * Método que valida se o usuário quer recomeçar o pedido
	 * @param respostas A lista de respostas que será enviada para o cliente
	 * @param _pedidoAbertoAndamento O pedido atual em andamento, caso exista
	 * @param _mensagem A mensagem digitada pelo usuario no chat
	 * @param _intencao A intenção do usuário com base na mensagem digitada
	 */
	private void recomecarPedidoNovamente(ArrayList<String> respostas, Optional<Pedido> _pedidoAbertoAndamento, String _mensagem, Intencoes _intencao) {
		if (Intencao.identificar(_mensagem) == Intencoes.ESCOLHER_PIZZAS_NOVAMENTE
				&& _pedidoAbertoAndamento.isPresent()) {
			respostas.add("Ok! Vamos recomeçar! Escolha novamente as suas pizzas:");
			this.pedidos.clear();
			for (Pizzas pizza : Pizzas.values()) {
				respostas.add("/" + pizza);
			}
		}


	}

	/**
	 * Método qeu avalia a escolha do usuário de incluir novas pizzas no pedido
	 * @param respostas A lista de respostas que será enviada para o cliente
	 * @param _pedidoAbertoAndamento O pedido atual em andamento, caso exista
	 * @param _mensagem A mensagem digitada pelo usuario no chat
	 * @param _intencao A intenção do usuário com base na mensagem digitada
	 */
	private void adicionarNovasPizzas(ArrayList<String> respostas, Optional<Pedido> _pedidoAbertoAndamento, String _mensagem, Intencoes _intencao) {
		if (Intencao.identificar(_mensagem) == Intencoes.ADICIONAR_PIZZAS && _pedidoAbertoAndamento.isPresent()) {
			respostas.add("Ok! Escolha mais pizzas para o seu pedido.");
			for (Pizzas pizza : Pizzas.values()) {
				respostas.add("/" + pizza);
			}
		}

	}

	/**
	 * Método que avalia se o usuário quer fazer alteraçã no pedido já feito
	 * @param respostas A lista de respostas que será enviada para o cliente
	 * @param _pedidoAbertoAndamento O pedido atual em andamento, caso exista
	 * @param _mensagem A mensagem digitada pelo usuario no chat
	 * @param _intencao A intenção do usuário com base na mensagem digitada
	 */
	private void alteracaoDePedido(ArrayList<String> respostas, Optional<Pedido> _pedidoAbertoAndamento, String _mensagem, Intencoes _intencao) {
		if (Intencao.identificar(_mensagem) == Intencoes.ALTERAR_PEDIDO && _pedidoAbertoAndamento.isPresent()) {
			respostas.add("Ok! O que você gostaria de alterar?");
			respostas.add("/adicionarPizzas ou /escolherNovamente ?");
		}

	}	

	/**
	 * Método que recupera o CEP do usuário e trata possíveis erros de digitação.
	 * Também consulta o CEP para pegar os dados do endereço na API de Endereços.
	 * 
	 * @param respostas A lista de respostas que será enviada para o cliente
	 * @param _pedidoAbertoAndamento O pedido atual em andamento, caso exista
	 * @param _mensagem A mensagem digitada pelo usuario no chat
	 * @param _intencao A intenção do usuário com base na mensagem digitada
	 */
	private void recuperaCEPeFinalizaPedido(ArrayList<String> respostas, Optional<Pedido> _pedidoAbertoAndamento,
			String _mensagem, Intencoes _intencao) {
		if (_pedidoAbertoAndamento.isPresent() && _pedidoAbertoAndamento.get().getStatus() == StatusPedido.ANDAMENTO) {
			String cep = Intencao.obterCEP(_mensagem);

			if (cep.length() == 0) {
				respostas.add("Desculpe. Acho que há algum problema com o CEP digitado. Poderia digitar novamente?");
			} else {
				try {
					LocalizacaoApi localizacaoAPI = new LocalizacaoApi();
					Localizacao _localizacao = localizacaoAPI.consultaCEP(cep);

					respostas.add("Você está em:");
					respostas.add(_localizacao.getLogradouro() + " - " + _localizacao.getBairro() + " " + _localizacao.getCidade() + "/" + _localizacao.getEstado());
					respostas.add("Seu pedido já está sendo preparado.");
					respostas.add("Clique em /start para fazer um novo pedido.");
					
					_pedidoAbertoAndamento.get().setStatus(StatusPedido.FECHADO);
					//Adiciona o pedido atual na lista de pedidos passados desta conversa
					this.pedidosPassados.addAll(this.pedidos);
					
					// Limpa a lista de pedidos atuais para permitir um novo pedido
					this.pedidos.clear();
				} catch (Exception e) {
					respostas.add("Desculpe-nos. Ocorreu um problema na busca do seu endereço.");
					respostas.add("Por favor, tente novamente em alguns instantes.");
				}
			}
		}

	}

	/**
	 * Método que fecha o pedido e o coloca em andamento. Também recupera o CEP do
	 * contato.
	 * 
	 * @param respostas A lista de respostas que será enviada para o cliente
	 * @param _pedidoAbertoAndamento O pedido atual em andamento, caso exista
	 * @param _mensagem A mensagem digitada pelo usuario no chat
	 * @param _intencao A intenção do usuário com base na mensagem digitada
	 */
	private void finalizaPedido(ArrayList<String> respostas, Optional<Pedido> _pedidoAbertoAndamento, String _mensagem,
			Intencoes _intencao) {
		if (Intencao.identificar(_mensagem) == Intencoes.FINALIZAR_PEDIDO && _pedidoAbertoAndamento.isPresent()) {
			respostas.add("Pedido fechado. Poderia me informar o seu CEP ?");
			_pedidoAbertoAndamento.get().setStatus(StatusPedido.ANDAMENTO);
		}
	}

	/**
	 * Método que valida e confirma se o usuário concorda com o pedido
	 * 
	 * @param respostas A lista de respostas que será enviada para o cliente
	 * @param _pedidoAbertoAndamento O pedido atual em andamento, caso exista
	 * @param _mensagem A mensagem digitada pelo usuario no chat
	 * @param _intencao A intenção do usuário com base na mensagem digitada
	 */
	private void validaConfirmacaoDoPedido(ArrayList<String> respostas, Optional<Pedido> _pedidoAbertoAndamento,
			String _mensagem, Intencoes _intencao) {
		if (Intencao.identificar(_mensagem) == Intencoes.CONFIRMAR_PEDIDO && _pedidoAbertoAndamento.isPresent()) {
			respostas.add("Legal! Já vou fechar pra você!");
			respostas.add("O seu pedido ficou assim:");
			String pizzas = "";
			for (Pedido p : pedidos) {
				pizzas += "Uma pizza: " + p.getPizza() + " no valor de " + p.getValor() + "\n";
			}
			respostas.add(pizzas);
			respostas.add("Tudo certo? Podemos finalizar o pedido?");
			respostas.add("Escolha entre: /finalizarPedido ou /alterarPedido");
		}

	}

	/**
	 * Método que valida o se o usuário deseja continuar a escolher as pizzas
	 * 
	 * @param respostas A lista de respostas que será enviada para o cliente
	 * @param _pedidoAbertoAndamento O pedido atual em andamento, caso exista
	 * @param _mensagem A mensagem digitada pelo usuario no chat
	 * @param _intencao A intenção do usuário com base na mensagem digitada
	 */
	private void validaPedidoEmAndamento(ArrayList<String> respostas, Optional<Pedido> _pedidoAbertoAndamento,
			String _mensagem, Intencoes _intencao) {
		if (_intencao == Intencoes.PEDIDO_EM_ANDAMENTO) {

			Pizzas p = Pizzas.valueOf(_mensagem.replace('/', ' ').trim());
			Pedido _pedido = new Pedido(p.getNome(), p.getValor(), StatusPedido.ABERTO);

			respostas.add("Você escolheu a pizza de " + _pedido.getPizza() + " no valor de " + _pedido.getValor());
			respostas.add("Muito boa opção! Anotei! Gostaria de pedir mais alguma coisa? ");
			respostas.add("Caso contrário, /confirmarPedido");
			for (Pizzas pizza : Pizzas.values()) {
				respostas.add("/" + pizza);
			}
			pedidos.add(_pedido);
		}

	}

	/**
	 * Método que valida se o usuário tem a intenção de fazer um novo pedido
	 * 
	 * @param respostas A lista de respostas que será enviada para o cliente
	 * @param _pedidoAbertoAndamento O pedido atual em andamento, caso exista
	 * @param _mensagem A mensagem digitada pelo usuario no chat
	 * @param _intencao A intenção do usuário com base na mensagem digitada
	 */
	private void validaNovoPedido(ArrayList<String> respostas, Optional<Pedido> _pedidoAbertoAndamento,
			String _mensagem, Intencoes _intencao) {
		if (Intencao.identificar(_mensagem) == Intencoes.NOVO_PEDIDO && !_pedidoAbertoAndamento.isPresent()) {
			respostas.add("Seja bem-vindo " + this.NomeCliente + ", qual o sabor de pizza que você deseja?");
			respostas.add("Escolha um dos sabores:");
			for (Pizzas pizza : Pizzas.values()) {
				respostas.add("/" + pizza);
			}
		}

	}

}