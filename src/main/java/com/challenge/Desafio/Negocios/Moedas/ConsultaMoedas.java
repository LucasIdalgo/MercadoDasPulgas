package com.challenge.Desafio.Negocios.Moedas;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.Repositories.MoedaRepository;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ConsultaMoedas {
    private final MoedaRepository moedaRepository;

    public List<MoedasEntity> buscarTodasMoedas() {
        return moedaRepository.findAll();
    }

    public MoedasEntity buscarMoedaPorID(Integer id) {
        if (id == 0)
            throw new InvalidException("ID inválido");

        return moedaRepository.findById(id.longValue()).orElseThrow(() -> new NotFoundException("Moeda não encontrada"));
    }

    public MoedasEntity buscarMoedaAtivaPorID(Long idMoeda) {
        return moedaRepository.findById(idMoeda)
                                .filter(MoedasEntity::getAtivo)
                                .orElseThrow(() -> new NotFoundException("Moeda não encontrada"));
    }
}
