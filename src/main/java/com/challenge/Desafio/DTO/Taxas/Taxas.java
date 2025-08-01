package com.challenge.Desafio.DTO.Taxas;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Taxas(Long IdTaxaMoeda, LocalDate Data, Long IdMoedaOrigem, BigDecimal Taxa, Long IdMoedaDestino, Long IdProduto) {}
