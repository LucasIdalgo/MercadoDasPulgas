package com.challenge.Desafio.Negocios.Transacoes;

import com.challenge.Desafio.Utils.InvalidException;
import com.challenge.Desafio.model.PostTransacaoCambioMoeda;
import com.challenge.Desafio.model.PostTransacaoCompra;

public class ValidaDadosTransacao {
        public static void DadosCambio(PostTransacaoCambioMoeda cambioMoeda){
                if(cambioMoeda.getIdReino() == 0)
                        throw new InvalidException("ID do reino onde ocorreu a transação é inválido");
                if (cambioMoeda.getIdMoedaOrigem() == 0)
                        throw new InvalidException("ID da moeda base é inválido");
                if (cambioMoeda.getIdMoedaDestino() == 0)
                        throw new InvalidException("ID da moeda final é inválido");
                if(cambioMoeda.getQuantidade() == 0)
                        throw new InvalidException("Quantidade do câmbio inválido");

        }

        public static void DadosCompra(PostTransacaoCompra compra){
                if(compra.getIdProduto() == 0)
                        throw new InvalidException("ID do produto comprado é inválido");
                if(compra.getIdReino() == 0)
                        throw new InvalidException("ID do reino onde ocorreu a transação é inválido");
//                if (compra.getIdMoedaOrigem() == 0)
//                        throw new InvalidException("ID da moeda base é inválido");
                if (compra.getIdMoedaDestino() == 0)
                        throw new InvalidException("ID da moeda final é inválido");
                if(compra.getQuantidade() == 0)
                        throw new InvalidException("Quantidade da compra inválida");

        }
}
