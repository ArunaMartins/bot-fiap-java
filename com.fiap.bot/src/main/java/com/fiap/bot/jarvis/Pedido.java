package com.fiap.bot.jarvis;

public class Pedido {
    private String Pizza;
    private String Valor;
    private String Status;

    public String getPizza() {
        return Pizza;
    }
    public void setPizza(String pizza) {
        this.Pizza = pizza;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        this.Valor = valor;
    }

    public Pedido(String pizza) {
        Pizza = pizza;
    }

    public Pedido(String pizza, String valor, String status) {
        setPizza(pizza);
        setValor(valor);
        setStatus(status);
    }
}
