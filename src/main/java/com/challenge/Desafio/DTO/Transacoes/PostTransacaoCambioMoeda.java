package com.challenge.Desafio.DTO.Transacoes;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PostTransacaoCambioMoeda(
        @NotNull(message = "ID do reino onde ocorreu o câmbio é obrigatório")
        Long IdReino,

        @NotNull(message = "ID da moeda base é obrigatório")
        Long IdMoedaOrigem,

        @NotNull(message = "ID da moeda final é obrigatório")
        Long IdMoedaDestino,

        @NotNull(message = "Valor a ser cambiado é obrigatório")
        BigDecimal ValorTransacao
) {}
