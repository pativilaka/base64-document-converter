package com.akacode.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RetencaoDTO(
        String cpf,
        String contratoNumero,
        BigDecimal valorParcela,
        String cidadeUF,
        LocalDate dataDocumento,
        String opcaoMarcada
) {}
