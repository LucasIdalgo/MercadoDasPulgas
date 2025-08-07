package com.challenge.Desafio.Negocios.Moedas;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.Repositories.MoedaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CriaMoeda {
    private final MoedaRepository moedaRepository;

//    public CriaMoeda(MoedaRepository moedaRepository){
//        this.moedaRepository = moedaRepository;
//    }

    public MoedasEntity salvarMoeda(com.challenge.Desafio.model.PostNome postNome){
        MoedasEntity moedasEntity = MoedasEntity.builder()
                                                .Nome(postNome.getNome())
                                                .Ativo(true)
                                                .build();

        return salvarAlteracaoMoeda(moedasEntity);
    }

    public MoedasEntity salvarAlteracaoMoeda(MoedasEntity moedaEntity){
        return moedaRepository.save(moedaEntity);
    }
}
