package com.akacode.application;

import com.akacode.domain.DocType;

import java.util.Map;

public interface PdfFieldExtractor {

    DocType kind();
    Map<String, String> extractFields(byte[] pdfBytes);
}
