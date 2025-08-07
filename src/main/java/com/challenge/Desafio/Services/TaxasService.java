package com.challenge.Desafio.Services;

import com.challenge.Desafio.DTO.Taxas.FiltroTaxaDTO;
import com.challenge.Desafio.Entities.TaxaMoedaEntity;
import com.challenge.Desafio.Mapper.TaxaMapper;
import com.challenge.Desafio.Negocios.Taxas.ConsultaTaxas;
import com.challenge.Desafio.Negocios.Taxas.CriaTaxa;
import com.challenge.Desafio.Negocios.Taxas.ValidaDadosTaxa;
import com.challenge.Desafio.api.TaxasApi;
import com.challenge.Desafio.model.PostTaxa;
import com.challenge.Desafio.model.TaxaCompleta;
import com.challenge.Desafio.model.Taxas;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxasService implements TaxasApi {
    @Autowired
    private TaxaMapper taxaMapper;

    private final ConsultaTaxas consultaTaxas;
    private final CriaTaxa criaTaxa;

    @Override
    public List<Taxas> buscaTodasTaxas(Integer idMoedaOrigem, Integer idMoedaDestino, Integer idProduto, LocalDate dataInicial, LocalDate dataFinal) {
        FiltroTaxaDTO filtro = FiltroTaxaDTO.builder()
                .dataInicial(dataInicial)
                .dataFinal(dataFinal)
                .idProduto(idProduto)
                .idMoedaOrigem(idMoedaOrigem)
                .idMoedaDestino(idMoedaDestino)
                .build();

        return taxaMapper.mapListTaxaEntityParaListTaxa(consultaTaxas.buscarTodasTaxas(filtro));
    }

    @Override
    public TaxaCompleta buscaTaxaPorID(Integer id) {
        return taxaMapper.mapTaxaEntityParaTaxaCompleta(consultaTaxas.buscarTaxaPorID(id));
    }

    @Override
    @Transactional
    public Taxas criaTaxas(PostTaxa postTaxa) {
        ValidaDadosTaxa.DadosTaxa(postTaxa);

        TaxaMoedaEntity taxaEntity = new TaxaMoedaEntity();
        TaxaMoedaEntity taxaEntity2 = new TaxaMoedaEntity();

        return taxaMapper.mapTaxaEntityParaTaxa(criaTaxa.SalvarTaxa(postTaxa, taxaEntity, taxaEntity2));
    }
}
