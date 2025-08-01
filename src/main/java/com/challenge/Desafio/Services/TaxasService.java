package com.challenge.Desafio.Services;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Entities.TaxaMoedaEntity;
import com.challenge.Desafio.Mapper.TaxaMapper;
import com.challenge.Desafio.Repositories.MoedaRepository;
import com.challenge.Desafio.Repositories.ProdutoRepository;
import com.challenge.Desafio.Repositories.TaxaRepository;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import com.challenge.Desafio.api.TaxasApi;
import com.challenge.Desafio.model.PostTaxa;
import com.challenge.Desafio.model.TaxaCompleta;
import com.challenge.Desafio.model.Taxas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaxasService implements TaxasApi {
    @Autowired
    private TaxaRepository taxaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MoedaRepository moedaRepository;

    @Autowired
    private TaxaMapper taxaMapper;

    @Override
    public List<Taxas> buscaTodasTaxas(Integer idMoedaOrigem, Integer idMoedaDestino, Integer idProduto, LocalDate dataInicial, LocalDate dataFinal) {
//        var listaTaxas = taxaRepository.findAll();
//
//        if (idMoedaOrigem != null && idMoedaOrigem > 0)
//            listaTaxas = listaTaxas.stream().filter(t -> t.getMoedaOrigem().getIdMoeda() == Long.parseLong(idMoedaOrigem.toString())).toList();
//        if (idMoedaDestino != null && idMoedaDestino > 0)
//            listaTaxas = listaTaxas.stream().filter(t -> t.getMoedaDestino().getIdMoeda() == Long.parseLong(idMoedaDestino.toString())).toList();
//        if (idProduto != null && idProduto > 0)
//            listaTaxas = listaTaxas.stream().filter(t -> t.getProduto().getIdProduto() != null ?
//                                                                        t.getProduto().getIdProduto() == Long.parseLong(idProduto.toString()) :
//                                                                        0 == Long.parseLong(idProduto.toString())).toList();
//        if (data != null)
//            listaTaxas = listaTaxas.stream().filter(t -> t.getData().isAfter(data)).toList();

        var periodo = dataInicial == null && dataFinal == null ? 0 :
                        dataInicial != null && dataFinal == null ? 1 :
                            dataInicial == null ? 2 : 3;

        return taxaMapper.mapListTaxaEntityParaListTaxa(taxaRepository.ListaTodasTaxasFiltros(idMoedaOrigem == null ? 0 : idMoedaOrigem,
                                                                                                idMoedaDestino == null ? 0 : idMoedaDestino,
                                                                                                idProduto == null ? 0 : idProduto,
                                                                                                periodo,
                                                                                                dataInicial == null ? LocalDate.now() : dataInicial,
                                                                                                dataFinal == null ? LocalDate.now() : dataFinal));
    }

    @Override
    public TaxaCompleta buscaTaxaPorID(Integer id) {
        if (id == 0)
            throw new InvalidException("ID inválido");

        return taxaMapper.mapTaxaEntityParaTaxaCompleta(taxaRepository.findById(id.longValue())
                                                                        .orElseThrow(() -> new NotFoundException("Taxa não encontrada")));
    }

    @Override
    @Transactional
    public Taxas criaTaxas(PostTaxa postTaxa) {
        if (postTaxa.getIdMoedaOrigem() == 0)
            throw new InvalidException("ID da moeda base é inválido");
        if (postTaxa.getIdMoedaDestino() == 0)
            throw new InvalidException("ID da moeda final é inválido");
        if (postTaxa.getIdMoedaDestino().equals(postTaxa.getIdMoedaOrigem()))
            throw new InvalidException("ID da moeda final não pode ser o mesmo da moeda base");

        TaxaMoedaEntity taxaEntity;
        TaxaMoedaEntity taxaEntity2;
        if (postTaxa.getIdProduto() != null && postTaxa.getIdProduto() > 0) {
            taxaEntity = TaxaMoedaEntity.builder()
                    .Data(LocalDate.now())
                    .Taxa(BigDecimal.valueOf(postTaxa.getTaxa()))
                    .MoedaOrigem(moedaRepository.findById(postTaxa.getIdMoedaOrigem().longValue())
                            .filter(MoedasEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Moeda de Origem não encontrada")))
                    .MoedaDestino(moedaRepository.findById(postTaxa.getIdMoedaDestino().longValue())
                            .filter(MoedasEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Moeda de Destino não encontrada")))
                    .Produto(produtoRepository.findById(postTaxa.getIdProduto().longValue())
                            .filter(ProdutosEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Produto não encontrado")))
                    .Operacao(0L)
                    .build();

            taxaEntity2 = TaxaMoedaEntity.builder()
                    .Data(LocalDate.now())
                    .Taxa(BigDecimal.valueOf(postTaxa.getTaxa()))
                    .MoedaOrigem(moedaRepository.findById(postTaxa.getIdMoedaDestino().longValue())
                            .filter(MoedasEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Moeda de Destino não encontrada")))
                    .MoedaDestino(moedaRepository.findById(postTaxa.getIdMoedaOrigem().longValue())
                            .filter(MoedasEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Moeda de Origem não encontrada")))
                    .Produto(produtoRepository.findById(postTaxa.getIdProduto().longValue())
                            .filter(ProdutosEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Produto não encontrado")))
                    .Operacao(1L)
                    .build();
        } else {
            taxaEntity = TaxaMoedaEntity.builder()
                    .Data(LocalDate.now())
                    .Taxa(BigDecimal.valueOf(postTaxa.getTaxa()))
                    .MoedaOrigem(moedaRepository.findById(postTaxa.getIdMoedaOrigem().longValue())
                            .filter(MoedasEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Moeda de Origem não encontrada")))
                    .MoedaDestino(moedaRepository.findById(postTaxa.getIdMoedaDestino().longValue())
                            .filter(MoedasEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Moeda de Destino não encontrada")))
                    .Operacao(0L)
                    .build();

            taxaEntity2 = TaxaMoedaEntity.builder()
                    .Data(LocalDate.now())
                    .Taxa(BigDecimal.valueOf(postTaxa.getTaxa()))
                    .MoedaOrigem(moedaRepository.findById(postTaxa.getIdMoedaDestino().longValue())
                            .filter(MoedasEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Moeda de Destino não encontrada")))
                    .MoedaDestino(moedaRepository.findById(postTaxa.getIdMoedaOrigem().longValue())
                            .filter(MoedasEntity::getAtivo)
                            .orElseThrow(() -> new NotFoundException("Moeda de Origem não encontrada")))
                    .Operacao(1L)
                    .build();
        }

        taxaRepository.save(taxaEntity);
        taxaRepository.save(taxaEntity2);

        return taxaMapper.mapTaxaEntityParaTaxa(taxaEntity);
    }
}
