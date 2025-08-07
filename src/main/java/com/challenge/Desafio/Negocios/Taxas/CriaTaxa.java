package com.challenge.Desafio.Negocios.Taxas;

import com.challenge.Desafio.Entities.TaxaMoedaEntity;
import com.challenge.Desafio.Enums.Operacao;
import com.challenge.Desafio.Negocios.Moedas.ConsultaMoedas;
import com.challenge.Desafio.Negocios.Produtos.ConsultaProdutos;
import com.challenge.Desafio.Repositories.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class CriaTaxa {
    private final TaxaRepository taxaRepository;
    private final ConsultaMoedas consultaMoedas;
    private final ConsultaProdutos consultaProdutos;

//    public CriaTaxa(TaxaRepository taxaRepository, MoedaRepository moedaRepository, ProdutoRepository produtoRepository){
//        this.taxaRepository = taxaRepository;
//        this.moedaRepository = moedaRepository;
//        this.produtoRepository = produtoRepository;
//    }

    public TaxaMoedaEntity SalvarTaxa(com.challenge.Desafio.model.PostTaxa postTaxa, TaxaMoedaEntity taxaEntity, TaxaMoedaEntity taxaEntity2){
        var operacao = Operacao.valueOf(postTaxa.getOperacao().toString().toUpperCase());

        if (postTaxa.getIdProduto() != null && postTaxa.getIdProduto() > 0) {
            taxaEntity = TaxaMoedaEntity.builder()
                    .Data(LocalDate.now())
                    .Taxa(BigDecimal.valueOf(postTaxa.getTaxa()))
                    .MoedaOrigem(consultaMoedas.buscarMoedaAtivaPorID(postTaxa.getIdMoedaOrigem().longValue()))
                    .MoedaDestino(consultaMoedas.buscarMoedaAtivaPorID(postTaxa.getIdMoedaDestino().longValue()))
                    .Produto(consultaProdutos.buscarProdutoAtivoPorID(postTaxa.getIdProduto().longValue()))
                    .Operacao(operacao)
                    .build();

            taxaEntity2 = TaxaMoedaEntity.builder()
                    .Data(LocalDate.now())
                    .Taxa(BigDecimal.valueOf(postTaxa.getTaxa()))
                    .MoedaOrigem(consultaMoedas.buscarMoedaAtivaPorID(postTaxa.getIdMoedaDestino().longValue()))
                    .MoedaDestino(consultaMoedas.buscarMoedaAtivaPorID(postTaxa.getIdMoedaOrigem().longValue()))
                    .Produto(consultaProdutos.buscarProdutoAtivoPorID(postTaxa.getIdProduto().longValue()))
                    .Operacao(operacao.inverso())
                    .build();
        } else {
            taxaEntity = TaxaMoedaEntity.builder()
                    .Data(LocalDate.now())
                    .Taxa(BigDecimal.valueOf(postTaxa.getTaxa()))
                    .MoedaOrigem(consultaMoedas.buscarMoedaAtivaPorID(postTaxa.getIdMoedaOrigem().longValue()))
                    .MoedaDestino(consultaMoedas.buscarMoedaAtivaPorID(postTaxa.getIdMoedaDestino().longValue()))
                    .Operacao(operacao)
                    .build();

            taxaEntity2 = TaxaMoedaEntity.builder()
                    .Data(LocalDate.now())
                    .Taxa(BigDecimal.valueOf(postTaxa.getTaxa()))
                    .MoedaOrigem(consultaMoedas.buscarMoedaAtivaPorID(postTaxa.getIdMoedaDestino().longValue()))
                    .MoedaDestino(consultaMoedas.buscarMoedaAtivaPorID(postTaxa.getIdMoedaDestino().longValue()))
                    .Operacao(operacao.inverso())
                    .build();
        }

        taxaRepository.save(taxaEntity);
        taxaRepository.save(taxaEntity2);

        return taxaEntity;
    }
}
