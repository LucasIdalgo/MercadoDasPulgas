package com.challenge.Desafio.Services;

import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Entities.ReinosEntity;
import com.challenge.Desafio.Mapper.ReinoMapper;
import com.challenge.Desafio.Repositories.ProdutoRepository;
import com.challenge.Desafio.Repositories.ReinoRepository;
import com.challenge.Desafio.Repositories.TransacaoRepository;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import com.challenge.Desafio.api.ReinosApi;
import com.challenge.Desafio.model.PostNome;
import com.challenge.Desafio.model.Reinos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReinosService implements ReinosApi {

    @Autowired
    private ReinoRepository reinoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ReinoMapper reinoMapper;

    @Override
    public List<Reinos> buscaTodosReinos() {
        return reinoMapper.mapListReinoEntityParaListReinos(reinoRepository.findAll());
    }

    @Override
    public Reinos buscaReinoPorID(Integer id) {
        if (id == 0)
            throw new InvalidException("ID inválido");

        return reinoMapper.mapReinoEntityParaReino(reinoRepository.findById(id.longValue()).orElseThrow(() -> new NotFoundException("Reino não encontrado")));
    }

    @Override
    @Transactional
    public Reinos criaReinos(PostNome postNome) {
        if (StringUtils.isBlank(postNome.getNome()) || postNome.getNome().length() > 25)
            throw new InvalidException("Nome inválido");

        ReinosEntity reinoEntity = ReinosEntity.builder()
                .Nome(postNome.getNome())
                .Ativo(true)
                .build();

        return reinoMapper.mapReinoEntityParaReino(reinoRepository.save(reinoEntity));
    }

    @Override
    @Transactional
    public Reinos atualizaReino(Integer id, Reinos reino) {
        ReinosEntity reinoEntity = reinoMapper.mapReinosParaReinoEntity(buscaReinoPorID(id));

        if (!StringUtils.isBlank(reino.getNome()) && reino.getNome().length() <= 25)
            reinoEntity.setNome(reino.getNome());
        if (reino.isAtivo() != null)
            reinoEntity.setAtivo(reino.isAtivo());

        return reinoMapper.mapReinoEntityParaReino(reinoRepository.save(reinoEntity));
    }

    @Override
    @Transactional
    public void deletaReino(Integer id) {
        ReinosEntity reinoEntity = reinoMapper.mapReinosParaReinoEntity(buscaReinoPorID(id));

        if (!exclusaoValida(id.longValue())) {
            reinoEntity.setAtivo(false);
            reinoRepository.save(reinoEntity);

            List<ProdutosEntity> produtos = produtoRepository.findAll().stream().filter(p->p.getReino().getIdReino().equals(reinoEntity.getIdReino())).toList();
            produtos.forEach(p->p.setAtivo(false));
            produtoRepository.saveAll(produtos);
        } else
            reinoRepository.deleteById(id.longValue());
    }

    private Boolean exclusaoValida(Long id){
        var produto = produtoRepository.findAll().stream().filter(p->p.getReino().getIdReino().equals(id)).count();
        if(produto > 0)
            return false;

        var transacao = transacaoRepository.findAll().stream().filter(t->t.getReino().getIdReino().equals(id)).count();
        return transacao <= 0;
    }
}
