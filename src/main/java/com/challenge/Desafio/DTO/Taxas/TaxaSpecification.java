package com.challenge.Desafio.DTO.Taxas;

import com.challenge.Desafio.Entities.TaxaMoedaEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaxaSpecification implements Specification<TaxaMoedaEntity>{
    public FiltroTaxaDTO filtro;

    public TaxaSpecification(FiltroTaxaDTO filtro){
        this.filtro = filtro;
    }

    @Override
    public Predicate toPredicate(Root<TaxaMoedaEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(filtro.getIdMoedaOrigem() != null)
            predicates.add(criteriaBuilder.equal(root.get("MoedaOrigem").get("IdMoeda"), filtro.getIdMoedaOrigem()));
        if(filtro.getIdMoedaDestino() != null)
            predicates.add(criteriaBuilder.equal(root.get("MoedaDestino").get("IdMoeda"), filtro.getIdMoedaDestino()));
        if(filtro.getIdProduto() != null)
            predicates.add(criteriaBuilder.equal(root.get("Produto").get("IdProduto"), filtro.getIdProduto()));
        if(filtro.getDataInicial() != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("Data"), filtro.getDataInicial()));
        if(filtro.getDataFinal() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("Data"), filtro.getDataFinal()));

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
