package com.challenge.Desafio.Negocios.Produtos;

import com.challenge.Desafio.Entities.*;
import com.challenge.Desafio.Repositories.ProdutoRepository;
import com.challenge.Desafio.Repositories.TaxaRepository;
import com.challenge.Desafio.Repositories.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DeletaProduto {
    private final ProdutoRepository produtoRepository;
    private final TaxaRepository taxaRepository;
    private final TransacaoRepository transacaoRepository;

//    public DeletaProduto(ProdutoRepository produtoRepository, TaxaRepository taxaRepository, TransacaoRepository transacaoRepository){
//        this.produtoRepository = produtoRepository;
//        this.transacaoRepository = transacaoRepository;
//        this.taxaRepository = taxaRepository;
//    }

    public void deletarProduto(ProdutosEntity produto) {
        if (!validaExclusao(produto.getIdProduto())) {
            produto.setAtivo(false);
            produtoRepository.save(produto);
        } else
            produtoRepository.deleteById(produto.getIdProduto());
    }

    private Boolean validaExclusao(Long id) {
//        var taxa = taxaRepository.findAll().stream().filter(t -> t.getProduto() != null &&
//                                                                                t.getProduto().getIdProduto().equals(id)).count();
//        if (taxa > 0)
//            return false;

        var taxa = taxaRepository.verificaTaxaPorProduto(id);
        if (taxa > 0)
            return false;

//        var transacao = transacaoRepository.findAll().stream().filter(t -> t.getProduto().getIdProduto().equals(id)).count();
        var transacao = transacaoRepository.verificaTransacaoPorProduto(id);
        return transacao <= 0;
    }

    public void desativarProdutosPorReino(ReinosEntity reino) {
//        List<ProdutosEntity> produtos = produtoRepository.findAll().stream().filter(p->p.getReino().getIdReino().equals(reino.getIdReino())).toList();
        List<ProdutosEntity> produtos = produtoRepository.buscaTodosProdutosPorReino(reino.getIdReino());
        produtos.forEach(p -> p.setAtivo(false));
        produtoRepository.saveAll(produtos);
    }

    public void desativarProdutosPorMoeda(MoedasEntity moeda) {
//        List<ProdutosEntity> produtos = produtoRepository.findAll().stream().filter(p->p.getMoeda().getIdMoeda().equals(moeda.getIdMoeda())).toList();
        List<ProdutosEntity> produtos = produtoRepository.buscaTodosProdutosPorMoeda(moeda.getIdMoeda());
        produtos.forEach(p -> p.setAtivo(false));
        produtoRepository.saveAll(produtos);
    }
}
