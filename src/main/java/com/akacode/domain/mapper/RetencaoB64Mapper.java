package com.akacode.domain.mapper;

import com.akacode.common.B64;
import com.akacode.domain.dto.RetencaoB64DTO;
import com.akacode.domain.dto.RetencaoDTO;

public class RetencaoB64Mapper {
    private RetencaoB64Mapper(){}

    public static RetencaoB64DTO toB64(RetencaoDTO d) {
        return new RetencaoB64DTO(
                B64.ofString(d.cpf()),
                B64.ofString(d.contratoNumero()),
                B64.ofBigDecimal(d.valorParcela()),
                B64.ofString(d.cidadeUF()),
                B64.ofLocalDate(d.dataDocumento()),
                B64.ofString(d.opcaoMarcada())
        );
    }
}
