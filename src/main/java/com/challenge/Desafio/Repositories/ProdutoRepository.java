package com.challenge.Desafio.Repositories;

import com.challenge.Desafio.Entities.ProdutosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutosEntity, Long> {
}
