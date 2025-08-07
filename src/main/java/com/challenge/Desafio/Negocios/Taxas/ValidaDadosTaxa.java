package com.challenge.Desafio.Negocios.Taxas;

import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.model.PostTaxa;
import org.apache.commons.lang3.StringUtils;

public class ValidaDadosTaxa {
    public static void DadosTaxa(PostTaxa taxa){
        if (taxa.getIdMoedaOrigem() == 0)
            throw new InvalidException("ID da moeda base é inválido");
        if (taxa.getIdMoedaDestino() == 0)
            throw new InvalidException("ID da moeda final é inválido");
        if (taxa.getIdMoedaDestino().equals(taxa.getIdMoedaOrigem()))
            throw new InvalidException("ID da moeda final não pode ser o mesmo da moeda base");
        if(StringUtils.isBlank(taxa.getOperacao().toString()))
            throw new InvalidException("Operação inválida");
    }
}
