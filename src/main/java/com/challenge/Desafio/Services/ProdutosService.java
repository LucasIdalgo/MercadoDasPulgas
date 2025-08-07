package com.challenge.Desafio.Services;

import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Mapper.ProdutoMapper;
import com.challenge.Desafio.Negocios.Produtos.ConsultaProdutos;
import com.challenge.Desafio.Negocios.Produtos.CriaProduto;
import com.challenge.Desafio.Negocios.Produtos.DeletaProduto;
import com.challenge.Desafio.Negocios.Produtos.ValidaDadosProduto;
import com.challenge.Desafio.api.ProdutosApi;
import com.challenge.Desafio.model.PatchProduto;
import com.challenge.Desafio.model.PostProduto;
import com.challenge.Desafio.model.Produtos;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutosService implements ProdutosApi {
    @Autowired
    private ProdutoMapper produtoMapper;

    private final DeletaProduto deletaProduto;
    private final ConsultaProdutos consultaProdutos;
    private final CriaProduto criaProduto;

    @Override
    public List<Produtos> buscaTodosProdutos() {
        return produtoMapper.mapListProdutoEntityParaListProduto(consultaProdutos.buscarTodosProdutos());
    }

    @Override
    public Produtos buscaProdutoPorID(Integer id) {
        return produtoMapper.mapProdutoEntityParaProduto(consultaProdutos.buscarProdutoPorID(id));
    }

    @Override
    @Transactional
    public Produtos criaProdutos(PostProduto postProduto) {
        ValidaDadosProduto.DadosCriarProduto(postProduto);

        return produtoMapper.mapProdutoEntityParaProduto(criaProduto.salvarProduto(postProduto));
    }

    @Override
    @Transactional
    public Produtos atualizaProduto(Integer id, PatchProduto patchProduto) {
        ProdutosEntity produtosEntity = produtoMapper.mapProdutoParaProdutoEntity(buscaProdutoPorID(id));

        ValidaDadosProduto.DadosAlterarProduto(patchProduto, produtosEntity);

        return produtoMapper.mapProdutoEntityParaProduto(criaProduto.salvarAlteracaoProduto(produtosEntity));
    }

    @Override
    @Transactional
    public void deletaProduto(Integer id) {
        ProdutosEntity produtosEntity = produtoMapper.mapProdutoParaProdutoEntity(buscaProdutoPorID(id));

        deletaProduto.deletarProduto(produtosEntity);
    }
}
