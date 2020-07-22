package com.fiap.bot.jarvis;

import java.time.LocalDateTime;

public class Mensagem {

    private Integer IdMensagem;
    private String Texto;
    private LocalDateTime DataHora;

    public Mensagem(Integer idMensagem, String texto, LocalDateTime dataHora) {
        setIdMensagem(idMensagem);
        setTexto(texto);
        setDataHora(dataHora);
    }

    public LocalDateTime getDataHora() {
        return DataHora;
    }

    private void setDataHora(LocalDateTime dataHora) {
        this.DataHora = dataHora;
    }

    public String getTexto() {
        return Texto;
    }

    private void setTexto(String mensagem) {
        this.Texto = mensagem;
    }

    public Integer getIdMensagem() {
        return IdMensagem;
    }

    private void setIdMensagem(Integer idMensagem) {
        this.IdMensagem = idMensagem;
    }

}