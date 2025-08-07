package com.challenge.Desafio.Services;

import com.challenge.Desafio.DTO.Transacoes.FiltroTransacaoDTO;
import com.challenge.Desafio.Entities.*;
import com.challenge.Desafio.Mapper.TransacaoMapper;
import com.challenge.Desafio.Negocios.Produtos.ConsultaProdutos;
import com.challenge.Desafio.Negocios.Taxas.ConsultaTaxas;
import com.challenge.Desafio.Negocios.Transacoes.ConsultaTransacoes;
import com.challenge.Desafio.Negocios.Transacoes.CriaTransacao;
import com.challenge.Desafio.Negocios.Transacoes.ValidaDadosTransacao;
import com.challenge.Desafio.api.TransacoesApi;
import com.challenge.Desafio.model.PostTransacaoCambioMoeda;
import com.challenge.Desafio.model.PostTransacaoCompra;
import com.challenge.Desafio.model.TransacaoCompleta;
import com.challenge.Desafio.model.Transacoes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacoesService implements TransacoesApi {
    @Autowired
    private TransacaoMapper transacaoMapper;

    private final ConsultaTransacoes consultaTransacoes;
    private final CriaTransacao criaTransacao;
    private final ConsultaTaxas consultaTaxas;
    private final ConsultaProdutos consultaProdutos;

    @Override
    public List<Transacoes> buscaTodasTransacoes(LocalDate dataInicial, LocalDate dataFinal, String tipoTransacao, Integer idProduto, Integer idReino, Integer idMoedaOrigem, Integer idMoedaDestino) {
        FiltroTransacaoDTO filtro = FiltroTransacaoDTO.builder()
                .dataInicial(dataInicial)
                .dataFinal(dataFinal)
                .tipoTransacao(tipoTransacao)
                .idProduto(idProduto)
                .idReino(idReino)
                .idMoedaOrigem(idMoedaOrigem)
                .idMoedaDestino(idMoedaDestino)
                .build();

        return transacaoMapper.mapListTransacaoEntityParaListTransacoes(consultaTransacoes.buscaTodasTransacoes(filtro));
    }

    @Override
    public TransacaoCompleta buscaTransacaoPorID(Integer id) {
        return transacaoMapper.mapTransacaoEntityParaTransacaoCompleta(consultaTransacoes.buscaTransacaoPorID(id));
    }

    @Override
    @Transactional
    public Transacoes criaTransacaoCambioMoeda(PostTransacaoCambioMoeda postTransacaoCambioMoeda) {
        ValidaDadosTransacao.DadosCambio(postTransacaoCambioMoeda);

        TaxaMoedaEntity taxa = consultaTaxas.buscarTaxaPorMoedas(postTransacaoCambioMoeda.getIdMoedaOrigem(), postTransacaoCambioMoeda.getIdMoedaDestino());

        return transacaoMapper.mapTransacaoEntityParaTransacoes(criaTransacao.salvarCambio(taxa, postTransacaoCambioMoeda));
    }

    @Override
    @Transactional
    public Transacoes criaTransacaoCompra(PostTransacaoCompra postTransacaoCompra) {
        ValidaDadosTransacao.DadosCompra(postTransacaoCompra);

        ProdutosEntity produto = consultaProdutos.buscarProdutoAtivoPorID(postTransacaoCompra.getIdProduto().longValue());

//        if(produto.getMoeda().getIdMoeda() != postTransacaoCompra.getIdMoedaOrigem().longValue())
//            throw new InvalidException("Moeda base é diferente da moeda base do produto");

        var valorQuantidade = produto.getValor().multiply(BigDecimal.valueOf(postTransacaoCompra.getQuantidade()));

        TaxaMoedaEntity taxaProduto = consultaTaxas.buscarTaxaPorMoedasEProduto(produto.getMoeda().getIdMoeda().intValue(),
                                                                                postTransacaoCompra.getIdMoedaDestino(),
                                                                                postTransacaoCompra.getIdProduto());
        if(taxaProduto == null){
            TaxaMoedaEntity taxa = consultaTaxas.buscarTaxaPorMoedas(produto.getMoeda().getIdMoeda().intValue(),
                                                                             postTransacaoCompra.getIdMoedaDestino());

            PostTransacaoCambioMoeda cambio = new PostTransacaoCambioMoeda();
            cambio.setIdMoedaOrigem(produto.getMoeda().getIdMoeda().intValue());
            cambio.setIdMoedaDestino(postTransacaoCompra.getIdMoedaDestino());
            cambio.setQuantidade(postTransacaoCompra.getQuantidade() * produto.getValor().doubleValue());
            cambio.setIdReino(postTransacaoCompra.getIdReino());

            criaTransacao.salvarCambio(taxa, cambio);
            return transacaoMapper.mapTransacaoEntityParaTransacoes(criaTransacao.salvarCompra(taxa, valorQuantidade, produto, postTransacaoCompra));
        }else{
            PostTransacaoCambioMoeda cambio = new PostTransacaoCambioMoeda();
            cambio.setIdMoedaOrigem(produto.getMoeda().getIdMoeda().intValue());
            cambio.setIdMoedaDestino(postTransacaoCompra.getIdMoedaDestino());
            cambio.setQuantidade(valorQuantidade.doubleValue());
            cambio.setIdReino(postTransacaoCompra.getIdReino());

            criaTransacao.salvarCambio(taxaProduto, cambio);
            return transacaoMapper.mapTransacaoEntityParaTransacoes(criaTransacao.salvarCompra(taxaProduto, valorQuantidade, produto, postTransacaoCompra));
        }
    }
}
