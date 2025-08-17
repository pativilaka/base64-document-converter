package com.akacode.infrastructure.extractor;

import com.akacode.application.PdfFieldExtractor;
import com.akacode.domain.DocType;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

@ApplicationScoped
public class RetencaoExtractor implements PdfFieldExtractor {

    private static final Pattern P_CPF =
            Pattern.compile("CPF\\s+([0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}|\\d{11})");

    private static final Pattern P_CIDADE_DATA =
            Pattern.compile("([A-ZÇÃÉÍÓÚ ]+\\s+[A-Z]{2}),\\s+([0-9]{1,2}\\s+de\\s+[A-ZÇÃÉÍÓÚ]+\\s+de\\s+[0-9]{4})\\.");

    private static final Pattern P_VALOR_PARCELA =
            Pattern.compile("Valor\\s+da\\s+parcela\\s*\\R?\\s*([0-9.,]+)");

    private static final Pattern P_NUM_CONTRATO =
            Pattern.compile("\\b([0-9]{2,3}\\.[0-9]{3}\\.[0-9]{3})\\b");

    private static final Pattern P_OPCAO_NAO =
            Pattern.compile("X\\s*\\R?\\s*Eu\\s+n[aã]o\\s+solicitei\\s+a\\s+portabilidade", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_OPCAO_DESISTI =
            Pattern.compile("X\\s*\\R?\\s*Solicitei\\s+a\\s+portabilidade.*desisti", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_OPCAO_PROPOSTA =
            Pattern.compile("X\\s*\\R?\\s*Recebi\\s+do\\(a\\)\\s+BANCO\\s+DO\\s+BRASIL", Pattern.CASE_INSENSITIVE);

    @Override
    public DocType kind() {
        return DocType.RETENCAO;
    }

    @Override
    public Map<String, String> extractFields(byte[] pdfBytes) {
        try(var in = new ByteArrayInputStream(pdfBytes);
            var doc = PDDocument.load(in)){

            String txt = new PDFTextStripper().getText(doc);
            Map<String,String> out = new LinkedHashMap<>();
            out.put("cpfCliente", match(txt, P_CPF));
            var m = P_CIDADE_DATA.matcher(txt);
            if (m.find()) {
                out.put("cidadeUF", m.group(1).trim());
                out.put("dataDocumentoRaw", m.group(2).trim());
            }
            out.put("valorParcelaRaw", match(txt, P_VALOR_PARCELA));
            out.put("contratoNumero", match(txt, P_NUM_CONTRATO));

            if (P_OPCAO_NAO.matcher(txt).find()) out.put("opcaoMarcada", "NAO_SOLICITEI");
            else if (P_OPCAO_DESISTI.matcher(txt).find()) out.put("opcaoMarcada", "DESISTI");
            else if (P_OPCAO_PROPOSTA.matcher(txt).find()) out.put("opcaoMarcada", "RECEBI_PROPOSTA");

            return out;

        } catch (Exception e){
            throw new RuntimeException("Falha ao extrair campos da Retenção", e);
        }
    }

    private static String match(String text, Pattern p) {
        var mm = p.matcher(text);
        return mm.find() ? mm.group(1).trim() : null;
    }
}
