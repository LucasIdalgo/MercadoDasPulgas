package com.challenge.Desafio.Negocios.Transacoes;

import com.challenge.Desafio.Entities.*;
import com.challenge.Desafio.Enums.TipoTransacao;
import com.challenge.Desafio.Negocios.Moedas.ConsultaMoedas;
import com.challenge.Desafio.Negocios.Reinos.ConsultaReinos;
import com.challenge.Desafio.Repositories.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class CriaTransacao {
    private final TransacaoRepository transacaoRepository;
    private final ConsultaMoedas consultaMoedas;
    private final ConsultaReinos consultaReinos;

//    public CriaTransacao(TransacaoRepository transacaoRepository, MoedaRepository moedaRepository, ReinoRepository reinoRepository){
//        this.transacaoRepository = transacaoRepository;
//        this.moedaRepository = moedaRepository;
//        this.reinoRepository = reinoRepository;
//    }

    public TransacoesEntity salvarCambio(TaxaMoedaEntity taxa, com.challenge.Desafio.model.PostTransacaoCambioMoeda postTransacaoCambioMoeda){
        BigDecimal valorFinalTransacao = switch (taxa.getOperacao().ordinal()) {
            case 0 -> taxa.getTaxa().multiply(BigDecimal.valueOf(postTransacaoCambioMoeda.getQuantidade()));
            case 1 -> (BigDecimal.valueOf(postTransacaoCambioMoeda.getQuantidade())).divide(taxa.getTaxa(), RoundingMode.CEILING);
            default -> BigDecimal.ZERO;
        };

        TransacoesEntity transacoesEntity = TransacoesEntity.builder()
                .DataTransacao(LocalDate.now())
                .TipoTransacao(TipoTransacao.CAMBIO)
                .MoedaOrigem(consultaMoedas.buscarMoedaAtivaPorID(postTransacaoCambioMoeda.getIdMoedaOrigem().longValue()))
                .MoedaDestino(consultaMoedas.buscarMoedaAtivaPorID(postTransacaoCambioMoeda.getIdMoedaDestino().longValue()))
                .Reino(consultaReinos.buscarReinoAtivoPorID(postTransacaoCambioMoeda.getIdReino().longValue()))
                .Quantidade(BigDecimal.valueOf(postTransacaoCambioMoeda.getQuantidade()))
                .ValorTransacao(BigDecimal.valueOf(postTransacaoCambioMoeda.getQuantidade()))
                .ValorFinalTransacao(valorFinalTransacao)
                .build();

        return transacaoRepository.save(transacoesEntity);
    }

    public TransacoesEntity salvarCompra(TaxaMoedaEntity taxa, BigDecimal valorQuantidade, ProdutosEntity produto, com.challenge.Desafio.model.PostTransacaoCompra postTransacaoCompra){
        BigDecimal valorFinalTransacao = switch (taxa.getOperacao().ordinal()) {
            case 0 -> taxa.getTaxa().multiply(valorQuantidade);
            case 1 -> valorQuantidade.divide(taxa.getTaxa(), RoundingMode.CEILING);
            default -> BigDecimal.ZERO;
        };

        TransacoesEntity transacoesEntity = TransacoesEntity.builder()
                .DataTransacao(LocalDate.now())
                .TipoTransacao(TipoTransacao.COMPRA)
                .MoedaOrigem(produto.getMoeda())
                .MoedaDestino(consultaMoedas.buscarMoedaAtivaPorID(postTransacaoCompra.getIdMoedaDestino().longValue()))
                .Reino(consultaReinos.buscarReinoAtivoPorID(postTransacaoCompra.getIdReino().longValue()))
                .Produto(produto)
                .Quantidade(BigDecimal.valueOf(postTransacaoCompra.getQuantidade()))
                .ValorTransacao(valorQuantidade)
                .ValorFinalTransacao(valorFinalTransacao)
                .build();

        return transacaoRepository.save(transacoesEntity);
    }
}
