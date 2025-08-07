package com.challenge.Desafio.DTO.Transacoes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class FiltroTransacaoDTO {
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private String tipoTransacao;
    private Integer idProduto;
    private Integer idReino;
    private Integer idMoedaOrigem;
    private Integer idMoedaDestino;
    private Integer periodo;
}
