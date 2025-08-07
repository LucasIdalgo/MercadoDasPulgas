package com.challenge.Desafio.Negocios.Produtos;

import com.challenge.Desafio.Entities.ProdutosEntity;
import com.challenge.Desafio.Repositories.ProdutoRepository;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ConsultaProdutos {
    private final ProdutoRepository produtoRepository;

//    public ConsultaProdutos(ProdutoRepository produtoRepository){
//        this.produtoRepository = produtoRepository;
//    }

    public List<ProdutosEntity> buscarTodosProdutos(){
        return produtoRepository.findAll();
    }

    public ProdutosEntity buscarProdutoPorID(Integer id){
        if (id == 0)
            throw new InvalidException("ID inválido");

        return produtoRepository.findById(id.longValue()).orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    public ProdutosEntity buscarProdutoAtivoPorID(Long idProduto){
        return produtoRepository.findById(idProduto)
                .filter(ProdutosEntity::getAtivo)
                .orElseThrow(()->new NotFoundException("Produto não encontrado"));
    }
}
