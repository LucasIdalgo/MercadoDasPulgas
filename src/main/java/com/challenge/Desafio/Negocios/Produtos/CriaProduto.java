package com.challenge.Desafio.Negocios.Produtos;

import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Negocios.Moedas.ConsultaMoedas;
import com.challenge.Desafio.Negocios.Reinos.ConsultaReinos;
import com.challenge.Desafio.Repositories.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class CriaProduto {
    private final ProdutoRepository produtoRepository;
    private final ConsultaMoedas consultaMoedas;
    private final ConsultaReinos consultaReinos;

//    public CriaProduto(ProdutoRepository produtoRepository, MoedaRepository moedaRepository, ReinoRepository reinoRepository){
//        this.produtoRepository = produtoRepository;
//        this.moedaRepository = moedaRepository;
//        this.reinoRepository = reinoRepository;
//    }

    public ProdutosEntity salvarProduto(com.challenge.Desafio.model.PostProduto produto){
        ProdutosEntity produtosEntity = ProdutosEntity.builder()
                .Descricao(produto.getDescricao())
                .Valor(BigDecimal.valueOf(produto.getValor()))
                .Moeda(consultaMoedas.buscarMoedaAtivaPorID(produto.getIdMoeda().longValue()))
                .Reino(consultaReinos.buscarReinoAtivoPorID(produto.getIdReino().longValue()))
                .Ativo(true)
                .build();

        return salvarAlteracaoProduto(produtosEntity);
    }

    public ProdutosEntity salvarAlteracaoProduto(ProdutosEntity produto){
        return produtoRepository.save(produto);
    }
}
