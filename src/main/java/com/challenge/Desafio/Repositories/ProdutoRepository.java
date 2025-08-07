package com.challenge.Desafio.Repositories;

import com.challenge.Desafio.Entities.ProdutosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutosEntity, Long> {
    @Query(value = """
            SELECT	P.id_produto,
                  		P.ativo,
                  		P.descricao,
                  		P.valor,
                  		P.id_moeda,
                  		P.id_reino
                  FROM	Produtos	AS P WITH(NOLOCK)
                  WHERE	P.id_reino = :IdReino
            """, nativeQuery = true)
    List<ProdutosEntity> buscaTodosProdutosPorReino(@Param("IdReino") Long IdReino);

    @Query(value = """
            SELECT	P.id_produto,
                  		P.ativo,
                  		P.descricao,
                  		P.valor,
                  		P.id_moeda,
                  		P.id_reino
                  FROM	Produtos	AS P WITH(NOLOCK)
                  WHERE	P.id_moeda = :IdMoeda
            """, nativeQuery = true)
    List<ProdutosEntity> buscaTodosProdutosPorMoeda(@Param("IdMoeda") Long IdMoeda);

    @Query(value = "SELECT COUNT(P.id_produto) FROM Produto AS P WITH(NOLOCK) WHERE P.id_moeda = :IdMoeda", nativeQuery = true)
    Integer verificaProdutoPorMoeda(@Param("IdMoeda") Long IdMoeda);

    @Query(value = "SELECT COUNT(P.id_produto) FROM Produto AS P WITH(NOLOCK) WHERE P.id_reino = :IdReino", nativeQuery = true)
    Integer verificaProdutoPorReino(@Param("IdReino") Long IdReino);
}
