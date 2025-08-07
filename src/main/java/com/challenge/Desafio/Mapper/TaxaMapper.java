package com.challenge.Desafio.Mapper;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Entities.TaxaMoedaEntity;
import com.challenge.Desafio.model.TaxaCompleta;
import com.challenge.Desafio.model.Taxas;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MoedaMapper.class, ProdutoMapper.class})
public interface TaxaMapper {
    @Mapping(target = "idMoedaOrigem", source = "moedaOrigem", qualifiedByName = "mapIdMoeda")
    @Mapping(target = "idMoedaDestino", source = "moedaDestino", qualifiedByName = "mapIdMoeda")
    @Mapping(target = "idProduto", source = "produto", qualifiedByName = "mapIdProduto")
    Taxas mapTaxaEntityParaTaxa(TaxaMoedaEntity taxaEntity);

    List<Taxas> mapListTaxaEntityParaListTaxa(List<TaxaMoedaEntity> taxaMoedasEntity);

    TaxaCompleta mapTaxaEntityParaTaxaCompleta(TaxaMoedaEntity taxaMoedaEntity);

    @Named("mapIdMoeda")
    default Integer mapIdMoeda(MoedasEntity moeda){
        if(moeda == null) return null;

        return moeda.getIdMoeda().intValue();
    }

    @Named("mapIdProduto")
    default Integer mapIdProduto(ProdutosEntity produto){
        if(produto == null) return null;

        return produto.getIdProduto().intValue();
    }
}
