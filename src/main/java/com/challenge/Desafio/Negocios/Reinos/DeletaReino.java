package com.challenge.Desafio.Negocios.Reinos;

import com.challenge.Desafio.Entities.ReinosEntity;
import com.challenge.Desafio.Negocios.Produtos.DeletaProduto;
import com.challenge.Desafio.Repositories.ProdutoRepository;
import com.challenge.Desafio.Repositories.ReinoRepository;
import com.challenge.Desafio.Repositories.TaxaRepository;
import com.challenge.Desafio.Repositories.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletaReino {
    private final ReinoRepository reinoRepository;
    private final TransacaoRepository transacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final DeletaProduto deletaProduto;

//    public DeletaReino(ReinoRepository reinoRepository, TransacaoRepository transacaoRepository, ProdutoRepository produtoRepository, TaxaRepository taxaRepository){
//        this.reinoRepository = reinoRepository;
//        this.transacaoRepository = transacaoRepository;
//        this.produtoRepository = produtoRepository;
//        this.taxaRepository = taxaRepository;
//    }

    public void deletarReino(ReinosEntity reino) {
        if (!validaExclusao(reino.getIdReino())) {
            reino.setAtivo(false);
            reinoRepository.save(reino);

            deletaProduto.desativarProdutosPorReino(reino);
        } else
            reinoRepository.deleteById(reino.getIdReino());
    }

    private Boolean validaExclusao(Long id) {
//        var produto = produtoRepository.findAll().stream().filter(p -> p.getReino().getIdReino().equals(id)).count();
        var produto = produtoRepository.verificaProdutoPorReino(id);
        if (produto > 0)
            return false;

//        var transacao = transacaoRepository.findAll().stream().filter(t -> t.getReino().getIdReino().equals(id)).count();
        var transacao = transacaoRepository.verificaTransacaoPorReino(id);
        return transacao <= 0;
    }
}
