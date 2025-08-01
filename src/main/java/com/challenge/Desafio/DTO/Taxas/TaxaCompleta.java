package com.challenge.Desafio.DTO.Taxas;

import com.challenge.Desafio.DTO.Moedas.Moedas;
import com.challenge.Desafio.DTO.Produtos.Produtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TaxaCompleta(Long IdTaxaMoeda, LocalDate Data, Moedas MoedaOrigem, BigDecimal Taxa, Moedas MoedaDestino, Produtos Produto) {}
