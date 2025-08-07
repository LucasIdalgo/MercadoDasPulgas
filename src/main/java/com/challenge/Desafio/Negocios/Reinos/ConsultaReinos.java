package com.challenge.Desafio.Negocios.Reinos;

import com.challenge.Desafio.Entities.ReinosEntity;
import com.challenge.Desafio.Repositories.ReinoRepository;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConsultaReinos {
    private final ReinoRepository reinoRepository;

//    public ConsultaReinos(ReinoRepository reinoRepository) {
//        this.reinoRepository = reinoRepository;
//    }

    public List<ReinosEntity> buscarTodosReinos() {
        return reinoRepository.findAll();
    }

    public ReinosEntity buscarReinoPorID(Integer id) {
        if (id == 0)
            throw new InvalidException("ID inválido");

        return reinoRepository.findById(id.longValue()).orElseThrow(() -> new NotFoundException("Reino não encontrado"));
    }

    public ReinosEntity buscarReinoAtivoPorID(Long idReino) {
        return reinoRepository.findById(idReino)
                .filter(ReinosEntity::getAtivo)
                .orElseThrow(() -> new NotFoundException("Reino não encontrado"));
    }
}
