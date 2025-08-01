package com.challenge.Desafio.DTO.Transacoes;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transacoes(Long IdTransacao, LocalDate DataTransacao, String TipoTransacao, Long IdProduto,
                         Long IdReino, Long IdMoedaOrigem, Long IdMoedaDestino, BigDecimal ValorTransacao,
                         BigDecimal ValorFinalTransacao) {}
