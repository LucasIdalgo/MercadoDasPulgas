package com.challenge.Desafio.Services;

import com.challenge.Desafio.Entities.*;
import com.challenge.Desafio.Enums.TipoTransacao;
import com.challenge.Desafio.Mapper.TransacaoMapper;
import com.challenge.Desafio.Repositories.*;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import com.challenge.Desafio.api.TransacoesApi;
import com.challenge.Desafio.model.PostTransacaoCambioMoeda;
import com.challenge.Desafio.model.PostTransacaoCompra;
import com.challenge.Desafio.model.TransacaoCompleta;
import com.challenge.Desafio.model.Transacoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransacoesService implements TransacoesApi {
    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ReinoRepository reinoRepository;

    @Autowired
    private MoedaRepository moedaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TaxaRepository taxaRepository;

    @Autowired
    private TransacaoMapper transacaoMapper;

    @Override
    public List<Transacoes> buscaTodasTransacoes(LocalDate dataInicial, LocalDate dataFinal, String tipoTransacao, Integer idProduto, Integer idReino, Integer idMoedaOrigem, Integer idMoedaDestino) {
        var periodo = dataInicial == null && dataFinal == null ? 0 :
                dataInicial != null && dataFinal == null ? 1 :
                        dataInicial == null ? 2 : 3;

        return transacaoMapper.mapListTransacaoEntityParaListTransacoes(transacaoRepository.ListaTodasTaxasFiltros(tipoTransacao == null ? -1 : TipoTransacao.valueOf(tipoTransacao.toUpperCase()).ordinal(),
                                                                                                                    idMoedaOrigem == null ? 0 : idMoedaOrigem,
                                                                                                                    idMoedaDestino == null ? 0 : idMoedaDestino,
                                                                                                                    idProduto == null ? 0 : idProduto,
                                                                                                                    idReino == null ? 0 : idReino,
                                                                                                                    periodo,
                                                                                                                    dataInicial == null ? LocalDate.now() : dataInicial,
                                                                                                                    dataFinal == null ? LocalDate.now() : dataFinal));
    }

    @Override
    public TransacaoCompleta buscaTransacaoPorID(Integer id) {
        if (id == 0)
            throw new InvalidException("ID inválido");

        return transacaoMapper.mapTransacaoEntityParaTransacaoCompleta(transacaoRepository.findById(id.longValue())
                                                                                            .orElseThrow(() -> new NotFoundException("Transação não encontrada")));
    }

    @Override
    @Transactional
    public Transacoes criaTransacaoCambioMoeda(PostTransacaoCambioMoeda postTransacaoCambioMoeda) {
        if(postTransacaoCambioMoeda.getIdReino() == 0)
            throw new InvalidException("ID do reino onde ocorreu a transação é inválido");
        if (postTransacaoCambioMoeda.getIdMoedaOrigem() == 0)
            throw new InvalidException("ID da moeda base é inválido");
        if (postTransacaoCambioMoeda.getIdMoedaDestino() == 0)
            throw new InvalidException("ID da moeda final é inválido");
        if(postTransacaoCambioMoeda.getQuantidade() == 0)
            throw new InvalidException("Quantidade do câmbio inválido");

        TaxaMoedaEntity taxa;
        if(postTransacaoCambioMoeda.getIdMoedaOrigem().equals(postTransacaoCambioMoeda.getIdMoedaDestino()))
            taxa = TaxaMoedaEntity.builder()
                    .Taxa(BigDecimal.ONE)
                    .Operacao(1L)
                    .build();
        else
            taxa = taxaRepository.UltimaTaxaCambio(postTransacaoCambioMoeda.getIdMoedaOrigem(), postTransacaoCambioMoeda.getIdMoedaDestino());

        if(taxa == null)
            throw new InvalidException("Não há taxa registrada para esse câmbio.\nConsulte o Conselho dos Mercadores.");

        return transacaoMapper.mapTransacaoEntityParaTransacoes(transacoes(taxa, postTransacaoCambioMoeda));
    }


    @Override
    @Transactional
    public Transacoes criaTransacaoCompra(PostTransacaoCompra postTransacaoCompra) {
        if(postTransacaoCompra.getIdProduto() == 0)
            throw new InvalidException("ID do produto comprado é inválido");
        if(postTransacaoCompra.getIdReino() == 0)
            throw new InvalidException("ID do reino onde ocorreu a transação é inválido");
        if (postTransacaoCompra.getIdMoedaOrigem() == 0)
            throw new InvalidException("ID da moeda base é inválido");
        if (postTransacaoCompra.getIdMoedaDestino() == 0)
            throw new InvalidException("ID da moeda final é inválido");
        if(postTransacaoCompra.getQuantidade() == 0)
            throw new InvalidException("Valor inicial da transação inválido");

        ProdutosEntity produto = produtoRepository.findById(postTransacaoCompra.getIdProduto().longValue())
                                                    .filter(ProdutosEntity::getAtivo)
                                                    .orElseThrow(()->new NotFoundException("Produto não encontrado"));

        if(produto.getMoeda().getIdMoeda() != postTransacaoCompra.getIdMoedaOrigem().longValue())
            throw new InvalidException("Moeda base é diferente da moeda base do produto");

        var valorQuantidade = produto.getValor().multiply(BigDecimal.valueOf(postTransacaoCompra.getQuantidade()));

        BigDecimal valorFinalTransacao = BigDecimal.ZERO;

        TaxaMoedaEntity taxaProduto;
        if(postTransacaoCompra.getIdMoedaOrigem().equals(postTransacaoCompra.getIdMoedaDestino()))
            taxaProduto = TaxaMoedaEntity.builder()
                    .Taxa(BigDecimal.ONE)
                    .Operacao(1L)
                    .build();
        else
            taxaProduto = taxaRepository.UltimaTaxaProduto(postTransacaoCompra.getIdMoedaOrigem(), postTransacaoCompra.getIdMoedaDestino(), postTransacaoCompra.getIdProduto());

        if(taxaProduto == null){
            TaxaMoedaEntity taxa = taxaRepository.UltimaTaxaCambio(postTransacaoCompra.getIdMoedaOrigem(), postTransacaoCompra.getIdMoedaDestino());

            if(taxa == null)
                throw new InvalidException("Não há taxa registrada para essa compra.\nConsulte o Conselho dos Mercadores.");

            PostTransacaoCambioMoeda cambio = new PostTransacaoCambioMoeda();
            cambio.setIdMoedaDestino(postTransacaoCompra.getIdMoedaDestino());
            cambio.setIdMoedaOrigem(postTransacaoCompra.getIdMoedaDestino());
            cambio.setQuantidade(postTransacaoCompra.getQuantidade() * produto.getValor().doubleValue());
            cambio.setIdReino(postTransacaoCompra.getIdReino());
            transacoes(taxa, cambio);

            valorFinalTransacao = switch (taxa.getOperacao().intValue()) {
                case 0 -> valorQuantidade.divide(taxa.getTaxa(), RoundingMode.CEILING);
                case 1 -> taxa.getTaxa().multiply(valorQuantidade);
                default -> valorFinalTransacao;
            };
        }else{
            PostTransacaoCambioMoeda cambio = new PostTransacaoCambioMoeda();
            cambio.setIdMoedaDestino(postTransacaoCompra.getIdMoedaDestino());
            cambio.setIdMoedaOrigem(postTransacaoCompra.getIdMoedaOrigem());
            cambio.setQuantidade(valorQuantidade.doubleValue());
            cambio.setIdReino(postTransacaoCompra.getIdReino());
            transacoes(taxaProduto, cambio);

            valorFinalTransacao = switch (taxaProduto.getOperacao().intValue()) {
                case 0 -> valorQuantidade.divide(taxaProduto.getTaxa(), RoundingMode.CEILING);
                case 1 -> taxaProduto.getTaxa().multiply(valorQuantidade);
                default -> valorFinalTransacao;
            };
        }

        TransacoesEntity transacoesEntity = TransacoesEntity.builder()
                .DataTransacao(LocalDate.now())
                .TipoTransacao(TipoTransacao.COMPRA)
                .MoedaOrigem(moedaRepository.findById(postTransacaoCompra.getIdMoedaOrigem().longValue())
                        .filter(MoedasEntity::getAtivo)
                        .orElseThrow(()-> new NotFoundException("Moeda de origem não encontrada")))
                .MoedaDestino(moedaRepository.findById(postTransacaoCompra.getIdMoedaDestino().longValue())
                        .filter(MoedasEntity::getAtivo)
                        .orElseThrow(() -> new NotFoundException("Moeda de Destino não encontrada")))
                .Reino(reinoRepository.findById(postTransacaoCompra.getIdReino().longValue())
                        .filter(ReinosEntity::getAtivo)
                        .orElseThrow(()->new NotFoundException("Reino não encontrado")))
                .Produto(produto)
                .Quantidade(BigDecimal.valueOf(postTransacaoCompra.getQuantidade()))
                .ValorTransacao(valorQuantidade)
                .ValorFinalTransacao(valorFinalTransacao)
                .build();

        return transacaoMapper.mapTransacaoEntityParaTransacoes(transacaoRepository.save(transacoesEntity));
    }

    private TransacoesEntity transacoes(TaxaMoedaEntity taxa, PostTransacaoCambioMoeda postTransacaoCambioMoeda){
        BigDecimal valorFinalTransacao = switch (taxa.getOperacao().intValue()) {
            case 0 ->
                    (BigDecimal.valueOf(postTransacaoCambioMoeda.getQuantidade())).divide(taxa.getTaxa(), RoundingMode.CEILING);
            case 1 -> taxa.getTaxa().multiply(BigDecimal.valueOf(postTransacaoCambioMoeda.getQuantidade()));
            default -> BigDecimal.ZERO;
        };

        TransacoesEntity transacoesEntity = TransacoesEntity.builder()
                .DataTransacao(LocalDate.now())
                .TipoTransacao(TipoTransacao.CAMBIO)
                .MoedaOrigem(moedaRepository.findById(postTransacaoCambioMoeda.getIdMoedaOrigem().longValue())
                        .filter(MoedasEntity::getAtivo)
                        .orElseThrow(() -> new NotFoundException("Moeda de origem não encontrada")))
                .MoedaDestino(moedaRepository.findById(postTransacaoCambioMoeda.getIdMoedaDestino().longValue())
                        .filter(MoedasEntity::getAtivo)
                        .orElseThrow(() -> new NotFoundException("Moeda de Destino não encontrada")))
                .Reino(reinoRepository.findById(postTransacaoCambioMoeda.getIdReino().longValue())
                        .filter(ReinosEntity::getAtivo)
                        .orElseThrow(() -> new NotFoundException("Reino não encontrado")))
                .Quantidade(BigDecimal.valueOf(postTransacaoCambioMoeda.getQuantidade()))
                .ValorTransacao(BigDecimal.valueOf(postTransacaoCambioMoeda.getQuantidade()))
                .ValorFinalTransacao(valorFinalTransacao)
                .build();

        return transacaoRepository.save(transacoesEntity);
    }
}
