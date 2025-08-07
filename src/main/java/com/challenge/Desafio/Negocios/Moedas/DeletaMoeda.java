package com.challenge.Desafio.Negocios.Moedas;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.Negocios.Produtos.DeletaProduto;
import com.challenge.Desafio.Repositories.MoedaRepository;
import com.challenge.Desafio.Repositories.ProdutoRepository;
import com.challenge.Desafio.Repositories.TaxaRepository;
import com.challenge.Desafio.Repositories.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeletaMoeda {
    private final MoedaRepository moedaRepository;
    private final ProdutoRepository produtoRepository;
    private final TaxaRepository taxaRepository;
    private final TransacaoRepository transacaoRepository;
    private final DeletaProduto deletaProduto;

//    public DeletaMoeda(MoedaRepository moedaRepository, ProdutoRepository produtoRepository, TaxaRepository taxaRepository, TransacaoRepository transacaoRepository){
//        this.moedaRepository = moedaRepository;
//        this.produtoRepository = produtoRepository;
//        this.transacaoRepository = transacaoRepository;
//        this.taxaRepository = taxaRepository;
//    }

    public void deletarMoeda(MoedasEntity moeda){
        if (!validaExclusao(moeda.getIdMoeda())) {
            moeda.setAtivo(false);
            moedaRepository.save(moeda);

            deletaProduto.desativarProdutosPorMoeda(moeda);
        } else
            moedaRepository.deleteById(moeda.getIdMoeda());
    }

    private Boolean validaExclusao(Long id){
//        var produto = produtoRepository.findAll().stream().filter(p-> p.getMoeda().getIdMoeda().equals(id)).count();
        var produto = produtoRepository.verificaProdutoPorMoeda(id);
        if(produto > 0)
            return false;

//        var taxa = taxaRepository.findAll().stream().filter(t-> t.getMoedaOrigem().getIdMoeda().equals(id) || t.getMoedaDestino().getIdMoeda().equals(id)).count();
        var taxa = taxaRepository.verificaTaxaPorMoeda(id);
        if(taxa > 0)
            return false;

//        var transacao = transacaoRepository.findAll().stream().filter(t->t.getMoedaOrigem().getIdMoeda().equals(id) || t.getMoedaDestino().getIdMoeda().equals(id)).count();
        var transacao = transacaoRepository.verificaTransacaoPorMoeda(id);
        return transacao <= 0;
    }
}
