package com.challenge.Desafio.DTO.Produtos;

import com.challenge.Desafio.DTO.Moedas.Moedas;

import java.math.BigDecimal;

public record Produtos(Long IdProduto, String Descricao, BigDecimal Valor, Moedas Moeda, Boolean Ativo) {}
