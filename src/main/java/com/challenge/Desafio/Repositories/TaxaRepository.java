package com.challenge.Desafio.Repositories;

import com.challenge.Desafio.Entities.TaxaMoedaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaxaRepository extends JpaRepository<TaxaMoedaEntity, Long> {
    @Query(value = """           
            SELECT	T.id_taxa_moeda,
                    T.data,
                    T.id_moeda_origem,
                    T.taxa,
                    T.id_moeda_destino,
                    T.id_produto,
                    T.operacao
            FROM Taxa_Moeda		AS T WITH(NOLOCK)
            WHERE	(:IdMoedaOrigem = 0 OR T.id_moeda_origem = :IdMoedaOrigem) AND
                     (:IdMoedaDestino = 0 OR T.id_moeda_destino = :IdMoedaDestino) AND
                     (:IdProduto = 0 OR T.id_produto = :IdProduto) AND
                     ((:Periodo = 0) OR
                     (:Periodo = 1 AND (T.data >= :DataInicial)) OR
                     (:Periodo = 2 AND (T.data <= :DataFinal)) OR
                     (:Periodo = 3 AND (T.data >= :DataInicial AND T.data <= :DataFinal)))""", nativeQuery = true)
    List<TaxaMoedaEntity> ListaTodasTaxasFiltros(
            @Param("IdMoedaOrigem") Integer IdMoedaOrigem,
            @Param("IdMoedaDestino") Integer IdMoedaDestino,
            @Param("IdProduto") Integer IdProduto,
            @Param("Periodo") Integer Periodo,
            @Param("DataInicial") LocalDate DataInicial,
            @Param("DataFinal") LocalDate DataFinal);

    @Query(value = """
            SELECT	TOP(1) T.id_taxa_moeda,
                    T.data,
                    T.id_moeda_origem,
                    T.taxa,
                    T.id_moeda_destino,
                    T.id_produto,
                    T.operacao
            FROM Taxa_Moeda		AS T WITH(NOLOCK)
            WHERE	T.id_moeda_origem = 2 AND
                    T.id_moeda_destino = 1 AND
                    T.id_produto IS NULL
            ORDER BY T.id_taxa_moeda DESC""", nativeQuery = true)
    TaxaMoedaEntity UltimaTaxaCambio(
            @Param("IdMoedaOrigem") Integer IdMoedaOrigem,
            @Param("IdMoedaDestino") Integer IdMoedaDestino
    );

    @Query(value = """
            SELECT	TOP(1) T.id_taxa_moeda,
                    T.data,
                    T.id_moeda_origem,
                    T.taxa,
                    T.id_moeda_destino,
                    T.id_produto,
                    T.operacao
            FROM Taxa_Moeda		AS T WITH(NOLOCK)
            WHERE	T.id_moeda_origem = :IdMoedaOrigem AND
                    T.id_moeda_destino = :IdMoedaDestino AND
                    T.id_produto = :IdProduto
            ORDER BY T.id_taxa_moeda DESC""", nativeQuery = true)
    TaxaMoedaEntity UltimaTaxaProduto(
            @Param("IdMoedaOrigem") Integer IdMoedaOrigem,
            @Param("IdMoedaDestino") Integer IdMoedaDestino,
            @Param("IdProduto") Integer IdProduto
    );
}
