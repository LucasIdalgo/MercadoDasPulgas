package com.challenge.Desafio.DTO.Transacoes;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PostTransacaoCompra(
        @NotNull(message = "ID do produto comprado é obrigatório")
        Long IdProduto,

        @NotNull(message = "ID do reino onde ocorreu a compra é obrigatório")
        Long IdReino,

        @NotNull(message = "ID da moeda base é obrigatório")
        Long IdMoedaOrigem,

        @NotNull(message = "ID da moeda final é obrigatório")
        Long IdMoedaDestino,

        @NotNull(message = "Valor a ser cambiado é obrigatório")
        BigDecimal ValorTransacao
) {}
