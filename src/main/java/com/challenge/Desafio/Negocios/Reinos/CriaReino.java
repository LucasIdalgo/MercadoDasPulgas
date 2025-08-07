package com.challenge.Desafio.Negocios.Reinos;

import com.challenge.Desafio.Entities.ReinosEntity;
import com.challenge.Desafio.Repositories.ReinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CriaReino {
    private final ReinoRepository reinoRepository;

//    public CriaReino(ReinoRepository reinoRepository){
//        this.reinoRepository = reinoRepository;
//    }

    public ReinosEntity salvarReino(com.challenge.Desafio.model.PostNome postNome){
        ReinosEntity reinoEntity = ReinosEntity.builder()
                .Nome(postNome.getNome())
                .Ativo(true)
                .build();

        return salvarAlteracaoReino(reinoEntity);
    }

    public ReinosEntity salvarAlteracaoReino(ReinosEntity reino){
        return reinoRepository.save(reino);
    }
}
