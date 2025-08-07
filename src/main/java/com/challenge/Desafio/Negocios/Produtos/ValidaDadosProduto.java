package com.challenge.Desafio.Negocios.Produtos;

import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.model.PostProduto;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class ValidaDadosProduto {
    public static void DadosCriarProduto(PostProduto produto){
        if (StringUtils.isBlank(produto.getDescricao()) || produto.getDescricao().length() > 100)
            throw new InvalidException("Nome inválido");
        if (produto.getIdMoeda() == 0)
            throw new InvalidException("ID da moeda inválido");
        if (produto.getIdReino() == 0)
            throw new InvalidException("ID do reino inválido");
        if (produto.getValor() == 0)
            throw new InvalidException("Produto deve conter o valor base de seu reino");

    }

    public static void DadosAlterarProduto(com.challenge.Desafio.model.PatchProduto patchProduto, ProdutosEntity produto){
        if (!StringUtils.isBlank(patchProduto.getDescricao()) && patchProduto.getDescricao().length() <= 100)
            produto.setDescricao(patchProduto.getDescricao());
        if (patchProduto.getValor() != null && patchProduto.getValor() > 0)
            produto.setValor(BigDecimal.valueOf(patchProduto.getValor()));
        if (patchProduto.isAtivo() != null)
            produto.setAtivo(patchProduto.isAtivo());
    }
}
