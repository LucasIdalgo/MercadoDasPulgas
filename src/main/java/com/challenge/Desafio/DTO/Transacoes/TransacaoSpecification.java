package com.challenge.Desafio.DTO.Transacoes;

import com.challenge.Desafio.Entities.TransacoesEntity;
import com.challenge.Desafio.Enums.TipoTransacao;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransacaoSpecification implements Specification<TransacoesEntity> {
    public FiltroTransacaoDTO filtro;

    public TransacaoSpecification(FiltroTransacaoDTO filtro){
        this.filtro = filtro;
    }

    @Override
    public Predicate toPredicate(Root<TransacoesEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(filtro.getTipoTransacao() != null)
            predicates.add(criteriaBuilder.equal(root.get("TipoTransacao"), TipoTransacao.valueOf(filtro.getTipoTransacao().toUpperCase()).ordinal()));
        if(filtro.getIdMoedaOrigem() != null)
            predicates.add(criteriaBuilder.equal(root.get("MoedaOrigem").get("IdMoeda"), filtro.getIdMoedaOrigem()));
        if(filtro.getIdMoedaDestino() != null)
            predicates.add(criteriaBuilder.equal(root.get("MoedaDestino").get("IdMoeda"), filtro.getIdMoedaDestino()));
        if(filtro.getIdProduto() != null)
            predicates.add(criteriaBuilder.equal(root.get("Produto").get("IdProduto"), filtro.getIdProduto()));
        if(filtro.getIdReino() != null)
            predicates.add(criteriaBuilder.equal(root.get("Reino").get("IdReino"), filtro.getIdReino()));
        if(filtro.getDataInicial() != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("DataTransacao"), filtro.getDataInicial()));
        if(filtro.getDataFinal() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("DataTransacao"), filtro.getDataFinal()));

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
