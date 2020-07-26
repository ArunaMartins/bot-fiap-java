package com.fiap.bot.jarvis;

import java.util.ArrayList;
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
	private String NomeCliente;
	private Long IdConversa;

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

		String _mensagem = mensagem.getTexto();

		Intencoes _intencao = Intencao.identificar(_mensagem);

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

				} catch (Exception e) {
					respostas.add("Desculpe-nos. Ocorreu um problema na busca do seu endereço.");
					respostas.add("Por favor, tente novamente em alguns instantes.");
				}
			}
		}

		if (_intencao == Intencoes.PEDIDO_EM_ANDAMENTO && !_pedidoAbertoAndamento.isPresent()) {

			Pizzas p = Pizzas.valueOf(_mensagem.replace('/', ' ').trim());
			Pedido _pedido = new Pedido(p.getNome(), p.getValor(), StatusPedido.ABERTO);

			respostas.add("Você escolheu a pizza de " + _pedido.getPizza() + " no valor de " + _pedido.getValor());
			respostas.add("Anotei! Gostaria de pedir mais alguma coisa? Caso contrário /confirmarPedido");
			pedidos.add(_pedido);
		}

		if (Intencao.identificar(_mensagem) == Intencoes.CONFIRMAR_PEDIDO && _pedidoAbertoAndamento.isPresent()) {
			respostas.add("O seu pedido é: Pizza de " + _pedidoAbertoAndamento.get().getPizza() + ", no valor de "
					+ _pedidoAbertoAndamento.get().getValor());
			respostas.add("Tudo certo?, podemos finalizar o pedido? /finalizarPedido ou /alterarPedido");
		}

		if (Intencao.identificar(_mensagem) == Intencoes.FINALIZAR_PEDIDO && _pedidoAbertoAndamento.isPresent()) {
			respostas.add("Qual o número do seu CEP ?");
			_pedidoAbertoAndamento.get().setStatus(StatusPedido.ANDAMENTO);
		}

		if (Intencao.identificar(_mensagem) == Intencoes.NOVO_PEDIDO && !_pedidoAbertoAndamento.isPresent()) {
			respostas.add("Seja bem-vindo " + this.NomeCliente + ", qual o sabor de pizza que você deseja?");
			respostas.add("Escolha um dos sabores:");
			for (Pizzas pizza : Pizzas.values()) {
				respostas.add("/" + pizza);
			}
		}

		mensagens.add(mensagem);

		return respostas;
	}
}