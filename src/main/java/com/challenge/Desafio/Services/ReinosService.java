package com.challenge.Desafio.Services;

import com.challenge.Desafio.Entities.ReinosEntity;
import com.challenge.Desafio.Mapper.ReinoMapper;
import com.challenge.Desafio.Negocios.Reinos.ConsultaReinos;
import com.challenge.Desafio.Negocios.Reinos.CriaReino;
import com.challenge.Desafio.Negocios.Reinos.DeletaReino;
import com.challenge.Desafio.Negocios.Reinos.ValidaDadosReino;
import com.challenge.Desafio.Negocios.ValidaDadosPadrao;
import com.challenge.Desafio.api.ReinosApi;
import com.challenge.Desafio.model.PostNome;
import com.challenge.Desafio.model.Reinos;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReinosService implements ReinosApi {
    @Autowired
    private ReinoMapper reinoMapper;

    private final CriaReino criaReino;
    private final DeletaReino deletaReino;
    private final ConsultaReinos consultaReinos;

    @Override
    public List<Reinos> buscaTodosReinos() {
        return reinoMapper.mapListReinoEntityParaListReinos(consultaReinos.buscarTodosReinos());
    }

    @Override
    public Reinos buscaReinoPorID(Integer id) {
        return reinoMapper.mapReinoEntityParaReino(consultaReinos.buscarReinoPorID(id));
    }

    @Override
    @Transactional
    public Reinos criaReinos(PostNome postNome) {
        ValidaDadosPadrao.Dados(postNome);

        return reinoMapper.mapReinoEntityParaReino(criaReino.salvarReino(postNome));
    }

    @Override
    @Transactional
    public Reinos atualizaReino(Integer id, Reinos reino) {
        ReinosEntity reinoEntity = reinoMapper.mapReinosParaReinoEntity(buscaReinoPorID(id));

        ValidaDadosReino.DadosAlterarReino(reino, reinoEntity);

        return reinoMapper.mapReinoEntityParaReino(criaReino.salvarAlteracaoReino(reinoEntity));
    }

    @Override
    @Transactional
    public void deletaReino(Integer id) {
        ReinosEntity reinoEntity = reinoMapper.mapReinosParaReinoEntity(buscaReinoPorID(id));

        deletaReino.deletarReino(reinoEntity);
    }
}
