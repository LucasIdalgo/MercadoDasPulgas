package com.challenge.Desafio.DTO.Taxas;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class FiltroTaxaDTO {
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private Integer idProduto;
    private Integer idMoedaOrigem;
    private Integer idMoedaDestino;
    private Integer periodo;
}
