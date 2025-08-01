package com.challenge.Desafio.DTO.Taxas;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PostTaxa(
        @NotNull(message = "ID da moeda base é obrigatório")
        Long IdMoedaOrigem,

        @NotNull(message = "Taxa é obrigatório")
        BigDecimal Taxa,

        @NotNull(message = "ID da moeda final é obrigatório")
        Long IdMoedaDestino,

        Long IdProduto
) {}
