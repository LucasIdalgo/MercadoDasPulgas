package com.challenge.Desafio.Services;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.Mapper.MoedaMapper;
import com.challenge.Desafio.Negocios.Moedas.ConsultaMoedas;
import com.challenge.Desafio.Negocios.Moedas.CriaMoeda;
import com.challenge.Desafio.Negocios.Moedas.DeletaMoeda;
import com.challenge.Desafio.Negocios.Moedas.ValidaDadosMoeda;
import com.challenge.Desafio.Negocios.ValidaDadosPadrao;
import com.challenge.Desafio.api.MoedasApi;
import com.challenge.Desafio.model.Moedas;
import com.challenge.Desafio.model.PostNome;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoedasService implements MoedasApi {
    @Autowired
    private MoedaMapper moedaMapper;

    private final ConsultaMoedas consultaMoedas;
    private final CriaMoeda criaMoeda;
    private final DeletaMoeda deletaMoeda;

    @Override
    public List<Moedas> buscaTodasMoedas() {
        return moedaMapper.mapListMoedaEntityParaMoedas(consultaMoedas.buscarTodasMoedas());
    }

    @Override
    public Moedas buscaMoedaPorID(Integer id) {
        return moedaMapper.mapMoedaEntityParaMoeda(consultaMoedas.buscarMoedaPorID(id));
    }

    @Override
    @Transactional
    public Moedas criaMoedas(PostNome postNome) {
        ValidaDadosPadrao.Dados(postNome);

        return moedaMapper.mapMoedaEntityParaMoeda(criaMoeda.salvarMoeda(postNome));
    }

    @Override
    @Transactional
    public Moedas atualizaMoeda(Integer id, Moedas moedas) {
        MoedasEntity moedaEntity = moedaMapper.mapMoedasParaMoedaEntity(buscaMoedaPorID(id));

        ValidaDadosMoeda.DadosAlterarMoeda(moedas, moedaEntity);

        return moedaMapper.mapMoedaEntityParaMoeda(criaMoeda.salvarAlteracaoMoeda(moedaEntity));
    }

    @Override
    @Transactional
    public void deletaMoeda(Integer id) {
        MoedasEntity moedaEntity = moedaMapper.mapMoedasParaMoedaEntity(buscaMoedaPorID(id));

        deletaMoeda.deletarMoeda(moedaEntity);
    }
}
