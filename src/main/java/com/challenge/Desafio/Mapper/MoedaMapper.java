package com.challenge.Desafio.Mapper;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.model.Moedas;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MoedaMapper {
    Moedas mapMoedaEntityParaMoeda(MoedasEntity moedasEntity);

    List<Moedas> mapListMoedaEntityParaMoedas(List<MoedasEntity> moedasEntity);

    @Mapping(target = "IdMoeda", source = "idMoeda")
    @Mapping(target = "Nome", source = "nome")
    @Mapping(target = "Ativo", source = "ativo")
    MoedasEntity mapMoedasParaMoedaEntity(Moedas moeda);
}
