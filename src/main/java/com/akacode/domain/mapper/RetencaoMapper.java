package com.akacode.domain.mapper;

import com.akacode.common.Normalizers;
import com.akacode.domain.dto.RetencaoDTO;

import java.util.Map;

public class RetencaoMapper {
    private RetencaoMapper(){}

    public static RetencaoDTO toDto(Map<String,String> raw) {
        String cpf = Normalizers.onlyDigits(raw.get("cpfCliente"));
        String contrato = raw.get("contratoNumero");
        var valor = Normalizers.moneyPtBrToBigDecimal(raw.get("valorParcelaRaw"));
        String cidadeUF = raw.get("cidadeUF");
        var data = Normalizers.datePorExtenso(raw.get("dataDocumentoRaw"));
        String opcao = raw.get("opcaoMarcada"); // já vem das regex

        return new RetencaoDTO(cpf, contrato, valor, cidadeUF, data, opcao);
    }
}
