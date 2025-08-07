package com.challenge.Desafio.Mapper;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Entities.ReinosEntity;
import com.challenge.Desafio.Entities.TransacoesEntity;
import com.challenge.Desafio.model.TransacaoCompleta;
import com.challenge.Desafio.model.Transacoes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MoedaMapper.class, ReinoMapper.class, ProdutoMapper.class})
public interface TransacaoMapper {
    @Mapping(target = "idMoedaOrigem", source = "moedaOrigem", qualifiedByName = "mapIdMoeda")
    @Mapping(target = "idMoedaDestino", source = "moedaDestino", qualifiedByName = "mapIdMoeda")
    @Mapping(target = "idProduto", source = "produto", qualifiedByName = "mapIdProduto")
    @Mapping(target = "idReino", source = "reino", qualifiedByName = "mapIdReino")
    Transacoes mapTransacaoEntityParaTransacoes(TransacoesEntity transacoesEntity);

    List<Transacoes> mapListTransacaoEntityParaListTransacoes(List<TransacoesEntity> transacoesEntity);

    TransacaoCompleta mapTransacaoEntityParaTransacaoCompleta(TransacoesEntity transacoesEntity);

    @Named("mapIdMoeda")
    default Integer mapIdMoeda(MoedasEntity moeda){
        if(moeda == null) return null;

        return Integer.parseInt(moeda.getIdMoeda().toString());
    }

    @Named("mapIdProduto")
    default Integer mapIdProduto(ProdutosEntity produto){
        if(produto == null) return null;

        return Integer.parseInt(produto.getIdProduto().toString());
    }

    @Named("mapIdReino")
    default Integer mapIdReino(ReinosEntity reino){
        if(reino == null) return null;

        return Integer.parseInt(reino.getIdReino().toString());
    }
}
