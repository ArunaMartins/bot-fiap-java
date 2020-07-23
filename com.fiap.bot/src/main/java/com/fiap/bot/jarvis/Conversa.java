package com.fiap.bot.jarvis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Conversa {
    private List<Mensagem> mensagens = new ArrayList<Mensagem>();
    private List<Pedido> pedidos = new ArrayList<Pedido>();

    private Long IdConversa;

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public Long getIdConversa() {
        return this.IdConversa;
    }

    public void setIdConversa(Long idConversa) {
        this.IdConversa = idConversa;
    }

    public Conversa(Long idConversa) {
        this.IdConversa = idConversa;
    }

    public String Responder(Mensagem mensagem) {
        String resposta = "";

        // sugestão criar uma classe INTENCAO.Indentificar(mensagem) retorna ENUM das
        // intenções disponiveis.
        if (Intencao.Identificar(mensagem.getTexto()) == Intencoes.PEDIDO_EM_ANDAMENTO) {
            Optional<Pedido> _pedido = pedidos.stream().filter(x -> x.getStatus() == StatusPedido.ABERTO).findFirst();

            if (_pedido.isPresent()) {
                resposta = "O seu pedido é : Pizza de " + _pedido.get().getPizza() + ", no valor de "
                        + _pedido.get().getValor();
            }

            resposta = "Você ainda não fez um pedido, gostaria de pedir";
        }

        if (Intencao.Identificar(mensagem.getTexto()) == Intencoes.NOVO_PEDIDO) {
            resposta = "Legal, qual o sabor de pizza que você deseja";
        }

        mensagens.add(mensagem);

        return resposta;
    }
}