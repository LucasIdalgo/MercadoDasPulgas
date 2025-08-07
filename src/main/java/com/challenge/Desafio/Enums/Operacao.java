package com.challenge.Desafio.Enums;

import lombok.Getter;

@Getter
public enum Operacao {
    MULTIPLICACAO("Multiplicação"),
    DIVISAO("Divisão");

    private final String op;

    Operacao(String op){this.op = op;}

    public Operacao inverso(){
        return switch (this){
            case MULTIPLICACAO -> DIVISAO;
            case DIVISAO -> MULTIPLICACAO;
        };
    }
}
