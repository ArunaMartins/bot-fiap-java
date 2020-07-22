package com.fiap.bot.jarvis;

import java.util.ArrayList;
import java.util.List;

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

        //sugestão criar uma classe INTENCAO.Indentificar(mensagem) retorna ENUM das intenções disponiveis.
        if( Intencao.Identificar(mensagem.getTexto()) == Intencoes.CONFIRMAR_PEDIDO){
            
        }
        
        mensagens.add(mensagem);
        return resposta;
    }
}