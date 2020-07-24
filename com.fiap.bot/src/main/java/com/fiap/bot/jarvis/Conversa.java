package com.fiap.bot.jarvis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fiap.bot.api.Localizacao;
import com.fiap.bot.api.LocalizacaoApi;
import com.fiap.bot.integrations.enums.Intencoes;
import com.fiap.bot.integrations.enums.Pizzas;
import com.fiap.bot.integrations.enums.StatusPedido;

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

    public ArrayList<String> Responder(Mensagem mensagem) {

        ArrayList<String> respostas = new ArrayList<String>();
        Optional<Pedido> _pedidoAbertoAndamento = pedidos.stream()
                .filter(x -> x.getStatus() == StatusPedido.ABERTO || x.getStatus() == StatusPedido.ANDAMENTO)
                .findFirst();

        String _mensagem = mensagem.getTexto();

        Intencoes _intencao = Intencao.Identificar(_mensagem);

        if (_pedidoAbertoAndamento.isPresent() && _pedidoAbertoAndamento.get().getStatus() == StatusPedido.ANDAMENTO) {
            String cep = Intencao.ObterCEP(_mensagem);

            LocalizacaoApi api = new LocalizacaoApi();
            Localizacao _localizacao = api.consultaCEP(cep);
            
            respostas.add("Você está em:");
            respostas.add(_localizacao.getLogradouro()  + " - " + _localizacao.getBairro() + " " + _localizacao.getCidade() + "/" +_localizacao.getEstado());
        } 

        if (_intencao == Intencoes.PEDIDO_EM_ANDAMENTO && !_pedidoAbertoAndamento.isPresent()) {
            Pedido _pedido = new Pedido(Pizzas.MUZZARELA.toString(), "15.60", StatusPedido.ABERTO);
            respostas.add("Pizza de " + _pedido.getPizza() + " no valor de " + _pedido.getValor());
            respostas.add("Anotei, gostaria de pedir mais alguma coisa ? contrario /confirmarPedido");
            pedidos.add(_pedido);
        }

        if (Intencao.Identificar(_mensagem) == Intencoes.CONFIRMAR_PEDIDO
                && _pedidoAbertoAndamento.isPresent()) {
            respostas.add("O seu pedido é : Pizza de " + _pedidoAbertoAndamento.get().getPizza() + ", no valor de "
                    + _pedidoAbertoAndamento.get().getValor());
            respostas.add("Tudo certo? /finalizarPedido ou /alterarPedido");
        }

        if (Intencao.Identificar(_mensagem) == Intencoes.FINALIZAR_PEDIDO && _pedidoAbertoAndamento.isPresent()) {
            respostas.add("Qual o numero do seu CEP ?");
            _pedidoAbertoAndamento.get().setStatus(StatusPedido.ANDAMENTO);
        }

        if (Intencao.Identificar(_mensagem) == Intencoes.NOVO_PEDIDO && !_pedidoAbertoAndamento.isPresent()) {
            respostas.add("Seja bem vindo " + this.NomeCliente + ", qual o sabor de pizza que você deseja?");
        }

        mensagens.add(mensagem);

        return respostas;
    }
}