package com.challenge.Desafio.Negocios.Transacoes;

import com.challenge.Desafio.DTO.Transacoes.FiltroTransacaoDTO;
import com.challenge.Desafio.DTO.Transacoes.TransacaoSpecification;
import com.challenge.Desafio.Entities.TransacoesEntity;
import com.challenge.Desafio.Repositories.TransacaoRepository;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ConsultaTransacoes {
    private final TransacaoRepository transacaoRepository;

//    public ConsultaTransacoes(TransacaoRepository transacaoRepository){
//        this.transacaoRepository = transacaoRepository;
//    }

    public List<TransacoesEntity> buscaTodasTransacoes(FiltroTransacaoDTO filtro) {
//        filtro.setPeriodo(filtro.getDataInicial() == null && filtro.getDataFinal() == null ? 0 :
//                filtro.getDataInicial() != null && filtro.getDataFinal() == null ? 1 :
//                        filtro.getDataInicial() == null ? 2 : 3);
//
//        return transacaoRepository.ListaTodasTaxasFiltros(filtro.getTipoTransacao() == null ? -1 : TipoTransacao.valueOf(filtro.getTipoTransacao().toUpperCase()).ordinal(),
//                                                            filtro.getIdMoedaOrigem() == null ? 0 : filtro.getIdMoedaOrigem(),
//                                                            filtro.getIdMoedaDestino() == null ? 0 : filtro.getIdMoedaDestino(),
//                                                            filtro.getIdProduto() == null ? 0 : filtro.getIdProduto(),
//                                                            filtro.getIdReino() == null ? 0 : filtro.getIdReino(),
//                                                            filtro.getPeriodo(),
//                                                            filtro.getDataInicial() == null ? LocalDate.now() : filtro.getDataInicial(),
//                                                            filtro.getDataFinal() == null ? LocalDate.now() : filtro.getDataFinal());

        var transacao = new TransacaoSpecification(filtro);
        return transacaoRepository.findAll(transacao);
    }

    public TransacoesEntity buscaTransacaoPorID(Integer id){
        if (id == 0)
            throw new InvalidException("ID inválido");

        return transacaoRepository.findById(id.longValue())
                                    .orElseThrow(() -> new NotFoundException("Transação não encontrada"));
    }
}
