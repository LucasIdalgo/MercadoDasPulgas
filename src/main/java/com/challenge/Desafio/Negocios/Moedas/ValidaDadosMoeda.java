package com.challenge.Desafio.Negocios.Moedas;

import com.challenge.Desafio.Entities.MoedasEntity;
import org.apache.commons.lang3.StringUtils;

public class ValidaDadosMoeda {
    public static void DadosAlterarMoeda(com.challenge.Desafio.model.Moedas moedas, MoedasEntity moedaEntity){
        if (!StringUtils.isBlank(moedas.getNome()) && moedas.getNome().length() <= 25)
            moedaEntity.setNome(moedas.getNome());
        if (moedas.isAtivo() != null)
            moedaEntity.setAtivo(moedas.isAtivo());
    }

    public static Boolean ValidaMesmaMoeda(Integer idMoedaOrigem, Integer idMoedaDestino){
        return idMoedaOrigem.equals(idMoedaDestino);
    }
}
