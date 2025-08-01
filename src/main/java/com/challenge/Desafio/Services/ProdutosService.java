package com.challenge.Desafio.Services;

import com.challenge.Desafio.Entities.MoedasEntity;
import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Entities.ReinosEntity;
import com.challenge.Desafio.Mapper.ProdutoMapper;
import com.challenge.Desafio.Repositories.*;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import com.challenge.Desafio.api.ProdutosApi;
import com.challenge.Desafio.model.PatchProduto;
import com.challenge.Desafio.model.PostProduto;
import com.challenge.Desafio.model.Produtos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutosService implements ProdutosApi {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MoedaRepository moedaRepository;

    @Autowired
    private ReinoRepository reinoRepository;

    @Autowired
    private TaxaRepository taxaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    @Override
    public List<Produtos> buscaTodosProdutos() {
        return produtoMapper.mapListProdutoEntityParaListProduto(produtoRepository.findAll());
    }

    @Override
    public Produtos buscaProdutoPorID(Integer id) {
        if (id == 0)
            throw new InvalidException("ID inválido");

        return produtoMapper.mapProdutoEntityParaProduto(produtoRepository.findById(id.longValue()).orElseThrow(() -> new NotFoundException("Produto não encontrado")));
    }

    @Override
    @Transactional
    public Produtos criaProdutos(PostProduto postProduto) {
        if (StringUtils.isBlank(postProduto.getDescricao()) || postProduto.getDescricao().length() > 100)
            throw new InvalidException("Nome inválido");
        if (postProduto.getIdMoeda() == 0)
            throw new InvalidException("ID da moeda inválido");
        if (postProduto.getIdReino() == 0)
            throw new InvalidException("ID do reino inválido");
        if (postProduto.getValor() == 0)
            throw new InvalidException("Produto deve conter o valor base de seu reino");

        ProdutosEntity produtosEntity = ProdutosEntity.builder()
                .Descricao(postProduto.getDescricao())
                .Valor(BigDecimal.valueOf(postProduto.getValor()))
                .Moeda(moedaRepository.findById(postProduto.getIdMoeda().longValue())
                        .filter(MoedasEntity::getAtivo)
                        .orElseThrow(() -> new NotFoundException("Moeda não encontrada")))
                .Reino(reinoRepository.findById(postProduto.getIdReino().longValue())
                        .filter(ReinosEntity::getAtivo)
                        .orElseThrow(() -> new NotFoundException("Reino não encontrado")))
                .Ativo(true)
                .build();

        return produtoMapper.mapProdutoEntityParaProduto(produtoRepository.save(produtosEntity));
    }

    @Override
    @Transactional
    public Produtos atualizaProduto(Integer id, PatchProduto patchProduto) {
        ProdutosEntity produtosEntity = produtoMapper.mapProdutoParaProdutoEntity(buscaProdutoPorID(id));

        if (!StringUtils.isBlank(patchProduto.getDescricao()) && patchProduto.getDescricao().length() <= 100)
            produtosEntity.setDescricao(patchProduto.getDescricao());
        if (patchProduto.getValor() != null && patchProduto.getValor() > 0)
            produtosEntity.setValor(BigDecimal.valueOf(patchProduto.getValor()));
        if (patchProduto.isAtivo() != null)
            produtosEntity.setAtivo(patchProduto.isAtivo());

        return produtoMapper.mapProdutoEntityParaProduto(produtoRepository.save(produtosEntity));
    }

    @Override
    @Transactional
    public void deletaProduto(Integer id) {
        ProdutosEntity produtosEntity = produtoMapper.mapProdutoParaProdutoEntity(buscaProdutoPorID(id));

        if (!exclusaoValida(id.longValue())) {
            produtosEntity.setAtivo(false);
            produtoRepository.save(produtosEntity);
        } else
            produtoRepository.deleteById(id.longValue());
    }

    private Boolean exclusaoValida(Long id){
        var taxa = taxaRepository.findAll().stream().filter(t->t.getProduto().getIdProduto().equals(id)).count();
        if(taxa > 0)
            return false;

        var transacao = transacaoRepository.findAll().stream().filter(t->t.getProduto().getIdProduto().equals(id)).count();
        return transacao <= 0;
    }
}
