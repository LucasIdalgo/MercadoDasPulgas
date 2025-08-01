package com.challenge.Desafio.Mapper;

import com.challenge.Desafio.Entities.ReinosEntity;
import com.challenge.Desafio.model.Reinos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReinoMapper {
    Reinos mapReinoEntityParaReino(ReinosEntity reinosEntity);

    List<Reinos> mapListReinoEntityParaListReinos(List<ReinosEntity> reinosEntity);

    @Mapping(target = "IdReino", source = "idReino")
    @Mapping(target = "Nome", source = "nome")
    @Mapping(target = "Ativo", source = "ativo")
    ReinosEntity mapReinosParaReinoEntity(Reinos reino);
}
