package com.challenge.Desafio.Negocios.Reinos;

import com.challenge.Desafio.Entities.ReinosEntity;
import org.apache.commons.lang3.StringUtils;

public class ValidaDadosReino {
    public static void DadosAlterarReino(com.challenge.Desafio.model.Reinos reino, ReinosEntity reinoEntity){
        if (!StringUtils.isBlank(reino.getNome()) && reino.getNome().length() <= 25)
            reinoEntity.setNome(reino.getNome());
        if (reino.isAtivo() != null)
            reinoEntity.setAtivo(reino.isAtivo());
    }
}
