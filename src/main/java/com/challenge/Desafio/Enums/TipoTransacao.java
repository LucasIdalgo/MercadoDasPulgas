package com.challenge.Desafio.Enums;

import lombok.Getter;

@Getter
public enum TipoTransacao {
    CAMBIO("Cambio"),
    COMPRA("Compra");

    private final String tipo;

    TipoTransacao(String tipo){
        this.tipo = tipo;
    }
}
