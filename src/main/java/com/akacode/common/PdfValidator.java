package com.akacode.common;

public class PdfValidator {
    private PdfValidator() {}

    public static void ensurePdf(String contentType, byte[] bytes) {
        if (contentType == null || !contentType.equalsIgnoreCase("application/pdf")) {
            throw new IllegalArgumentException("Content-Type inválido. Esperado application/pdf.");
        }
        if (bytes == null || bytes.length < 5) {
            throw new IllegalArgumentException("Arquivo vazio ou corrompido.");
        }
        // Assinatura %PDF-
        if (!(bytes[0] == 0x25 && bytes[1] == 0x50 && bytes[2] == 0x44 && bytes[3] == 0x46 && bytes[4] == 0x2D)) {
            throw new IllegalArgumentException("Assinatura do arquivo não parece PDF.");
        }
    }
}
