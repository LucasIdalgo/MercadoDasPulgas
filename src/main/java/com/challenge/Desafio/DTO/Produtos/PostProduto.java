package com.challenge.Desafio.DTO.Produtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PostProduto(
        @NotNull(message = "Descrição do produto é obrigatório")
        String Descricao,

        @NotNull(message = "Valor do produto é obrigatório")
        BigDecimal Valor,

        @NotNull(message = "ID da moeda local do produto é obrigatório")
        Long IdMoeda
) {}
