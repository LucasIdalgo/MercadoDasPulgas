package com.challenge.Desafio.Services;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Mapper.MoedaMapper;
import com.challenge.Desafio.Repositories.MoedaRepository;
import com.challenge.Desafio.Repositories.ProdutoRepository;
import com.challenge.Desafio.Repositories.TaxaRepository;
import com.challenge.Desafio.Repositories.TransacaoRepository;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import com.challenge.Desafio.api.MoedasApi;
import com.challenge.Desafio.model.Moedas;
import com.challenge.Desafio.model.PostNome;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MoedasService implements MoedasApi {
    @Autowired
    private MoedaRepository moedaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TaxaRepository taxaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private MoedaMapper moedaMapper;

    @Override
    public List<Moedas> buscaTodasMoedas() {
        return moedaMapper.mapListMoedaEntityParaMoedas(moedaRepository.findAll());
    }

    @Override
    public Moedas buscaMoedaPorID(Integer id) {
        if (id == 0)
            throw new InvalidException("ID inválido");

        return moedaMapper.mapMoedaEntityParaMoeda(moedaRepository.findById(id.longValue()).orElseThrow(() -> new NotFoundException("Moeda não encontrada")));
    }

    @Override
    @Transactional
    public Moedas criaMoedas(PostNome postNome) {
        if (StringUtils.isBlank(postNome.getNome()) || postNome.getNome().length() > 25)
            throw new InvalidException("Nome inválido");

        MoedasEntity moedasEntity = MoedasEntity.builder()
                .Nome(postNome.getNome())
                .Ativo(true)
                .build();

        return moedaMapper.mapMoedaEntityParaMoeda(moedaRepository.save(moedasEntity));
    }

    @Override
    @Transactional
    public Moedas atualizaMoeda(Integer id, Moedas moedas) {
        MoedasEntity moedaEntity = moedaMapper.mapMoedasParaMoedaEntity(buscaMoedaPorID(id));

        if (!StringUtils.isBlank(moedas.getNome()) && moedas.getNome().length() <= 25)
            moedaEntity.setNome(moedas.getNome());
        if (moedas.isAtivo() != null)
            moedaEntity.setAtivo(moedas.isAtivo());

        return moedaMapper.mapMoedaEntityParaMoeda(moedaRepository.save(moedaEntity));
    }

    @Override
    @Transactional
    public void deletaMoeda(Integer id) {
        MoedasEntity moedaEntity = moedaMapper.mapMoedasParaMoedaEntity(buscaMoedaPorID(id));

        if (!exclusaoValida(id.longValue())) {
            moedaEntity.setAtivo(false);
            moedaRepository.save(moedaEntity);

            List<ProdutosEntity> produtos = produtoRepository.findAll().stream().filter(p->p.getMoeda().getIdMoeda().equals(moedaEntity.getIdMoeda())).toList();
            produtos.forEach(p->p.setAtivo(false));
            produtoRepository.saveAll(produtos);
        } else
            moedaRepository.deleteById(id.longValue());
    }

    private Boolean exclusaoValida(Long id){
        var produto = produtoRepository.findAll().stream().filter(p-> p.getMoeda().getIdMoeda().equals(id)).count();
        if(produto > 0)
            return false;

        var taxa = taxaRepository.findAll().stream().filter(t-> t.getMoedaOrigem().getIdMoeda().equals(id) || t.getMoedaDestino().getIdMoeda().equals(id)).count();
        if(taxa > 0)
            return false;

        var transacao = transacaoRepository.findAll().stream().filter(t->t.getMoedaOrigem().getIdMoeda().equals(id) || t.getMoedaDestino().getIdMoeda().equals(id)).count();
        return transacao <= 0;
    }
}
