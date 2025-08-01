package com.challenge.Desafio.DTO.Transacoes;

import com.challenge.Desafio.DTO.Moedas.Moedas;
import com.challenge.Desafio.DTO.Produtos.Produtos;
import com.challenge.Desafio.DTO.Reinos.Reinos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoCompleta(Long IdTransacao, LocalDate DataTransacao, String TipoTransacao, Produtos Produto,
                                Reinos Reino, Moedas MoedaOrigem, Moedas MoedaDestino, BigDecimal ValorTransacao,
                                BigDecimal ValorFinalTransacao) {}
