package com.challenge.Desafio.Mapper;

import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.model.Produtos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MoedaMapper.class, ReinoMapper.class})
public interface ProdutoMapper {
    Produtos mapProdutoEntityParaProduto(ProdutosEntity produtosEntity);

    List<Produtos> mapListProdutoEntityParaListProduto(List<ProdutosEntity> produtosEntity);

    @Mapping(target = "IdProduto", source = "idProduto")
    @Mapping(target = "Descricao", source = "descricao")
    @Mapping(target = "Valor", source = "valor")
    @Mapping(target = "Moeda", source = "moeda")
    @Mapping(target = "Reino", source = "reino")
    @Mapping(target = "Ativo", source = "ativo")
    ProdutosEntity mapProdutoParaProdutoEntity(Produtos produto);
}
