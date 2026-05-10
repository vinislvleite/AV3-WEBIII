package com.autobots.automanager.excecoes;

public class VendaException extends RuntimeException {

    public VendaException(String identificacao) {
        super("Já existe uma venda cadastrada com a identificação: " + identificacao);
    }

    public VendaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}