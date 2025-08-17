package com.akacode.common;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;

public class B64 {

    private B64(){}
    public static String ofString(String s) {
        if (s == null) return null;
        return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }
    public static String ofBigDecimal(BigDecimal v) {
        return v == null ? null : ofString(v.toPlainString());
    }
    public static String ofLocalDate(LocalDate d) {
        return d == null ? null : ofString(d.toString()); // ISO yyyy-MM-dd
    }

}
