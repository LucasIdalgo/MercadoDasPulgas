package com.challenge.Desafio.Repositories;

import com.challenge.Desafio.Entities.TransacoesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacoesEntity, Long> {
    @Query(value = """           
            SELECT	T.id_transacao,
                    T.data_transacao,
                    T.tipo_transacao,
                    T.id_moeda_origem,
                    T.id_moeda_destino,
                    T.id_produto,
                    T.id_reino,
                    T.quantidade,
                    T.valor_transacao,
                    T.valor_final_transacao
            FROM Transacoes		AS T WITH(NOLOCK)
            WHERE	(:TipoTransacao = -1 OR T.tipo_transacao = :TipoTransacao) AND
                    (:IdMoedaOrigem = 0 OR T.id_moeda_origem = :IdMoedaOrigem) AND
                    (:IdMoedaDestino = 0 OR T.id_moeda_destino = :IdMoedaDestino) AND
                    (:IdProduto = 0 OR T.id_produto = :IdProduto) AND
                    (:IdReino = 0 OR T.id_reino = :IdReino) AND
                    ((:Periodo = 0) OR
                     (:Periodo = 1 AND T.data_transacao >= :DataInicial) OR
                     (:Periodo = 2 AND T.data_transacao <= :DataFinal) OR
                     (:Periodo = 3 AND T.data_transacao BETWEEN :DataInicial AND :DataFinal))""", nativeQuery = true)
    List<TransacoesEntity> ListaTodasTaxasFiltros(
            @Param("TipoTransacao") Integer TipoTransacao,
            @Param("IdMoedaOrigem") Integer IdMoedaOrigem,
            @Param("IdMoedaDestino") Integer IdMoedaDestino,
            @Param("IdProduto") Integer IdProduto,
            @Param("IdReino") Integer IdReino,
            @Param("Periodo") Integer Periodo,
            @Param("DataInicial") LocalDate DataInicial,
            @Param("DataFinal") LocalDate DataFinal);
}
