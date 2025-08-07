package com.challenge.Desafio.Negocios.Taxas;

import com.challenge.Desafio.DTO.Taxas.FiltroTaxaDTO;
import com.challenge.Desafio.DTO.Taxas.TaxaSpecification;
import com.challenge.Desafio.Entities.TaxaMoedaEntity;
import com.challenge.Desafio.Enums.Operacao;
import com.challenge.Desafio.Negocios.Moedas.ValidaDadosMoeda;
import com.challenge.Desafio.Repositories.TaxaRepository;
import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.Utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ConsultaTaxas {
    private final TaxaRepository taxaRepository;

//    public ConsultaTaxas(TaxaRepository taxaRepository){
//        this.taxaRepository = taxaRepository;
//    }

    public List<TaxaMoedaEntity> buscarTodasTaxas(FiltroTaxaDTO filtro) {
//        filtro.setPeriodo(filtro.getDataInicial() == null && filtro.getDataFinal() == null ? 0 :
//                filtro.getDataInicial() != null && filtro.getDataFinal() == null ? 1 :
//                        filtro.getDataInicial() == null ? 2 : 3);

//        return taxaRepository.ListaTodasTaxasFiltros(filtro.getIdMoedaOrigem() == null ? 0 : filtro.getIdMoedaOrigem(),
//                                                        filtro.getIdMoedaDestino() == null ? 0 : filtro.getIdMoedaDestino(),
//                                                        filtro.getIdProduto() == null ? 0 : filtro.getIdProduto(),
//                                                        filtro.getPeriodo(),
//                                                        filtro.getDataInicial() == null ? LocalDate.now() : filtro.getDataInicial(),
//                                                        filtro.getDataFinal() == null ? LocalDate.now() : filtro.getDataFinal());

        var taxa = new TaxaSpecification(filtro);
        return taxaRepository.findAll(taxa);
    }

    public TaxaMoedaEntity buscarTaxaPorID(Integer id) {
        if (id == 0)
            throw new InvalidException("ID inválido");

        return taxaRepository.findById(id.longValue())
                .orElseThrow(() -> new NotFoundException("Taxa não encontrada"));
    }

    public TaxaMoedaEntity buscarTaxaPorMoedas(Integer idMoedaOrigem, Integer idMoedaDestino) {
        TaxaMoedaEntity taxa;
        if (ValidaDadosMoeda.ValidaMesmaMoeda(idMoedaOrigem, idMoedaDestino))
            taxa = TaxaMoedaEntity.builder().Taxa(BigDecimal.ONE).Operacao(Operacao.MULTIPLICACAO).build();
        else {
            taxa = taxaRepository.UltimaTaxaCambio(idMoedaOrigem, idMoedaDestino);
            if (taxa == null)
                throw new InvalidException("Não há taxa registrada para essa transação.\nConsulte o Conselho dos Mercadores.");
        }

        return taxa;
    }

    public TaxaMoedaEntity buscarTaxaPorMoedasEProduto(Integer idMoedaOrigem, Integer idMoedaDestino, Integer idProduto) {
        TaxaMoedaEntity taxa;
        if (ValidaDadosMoeda.ValidaMesmaMoeda(idMoedaOrigem, idMoedaDestino))
            taxa = TaxaMoedaEntity.builder().Taxa(BigDecimal.ONE).Operacao(Operacao.MULTIPLICACAO).build();
        else
            taxa = taxaRepository.UltimaTaxaProduto(idMoedaOrigem, idMoedaDestino, idProduto);

        return taxa;
    }
}
