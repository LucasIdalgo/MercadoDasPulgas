package com.challenge.Desafio.Negocios;

import com.challenge.Desafio.Utils.InvalidException;
import org.apache.commons.lang3.StringUtils;

public class ValidaDadosPadrao {
    public static void Dados(com.challenge.Desafio.model.PostNome postNome){
        if (StringUtils.isBlank(postNome.getNome()) || postNome.getNome().length() > 25)
            throw new InvalidException("Nome inválido");
    }
}
