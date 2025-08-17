package com.akacode.common;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class Normalizers {

    private Normalizers(){}

    public static String onlyDigits(String s) {
        return s == null ? null : s.replaceAll("\\D", "");
    }

    public static BigDecimal moneyPtBrToBigDecimal(String s) {
        if (s == null || s.isBlank()) return null;
        String canon = s.replace(".", "").replace(",", ".");
        return new BigDecimal(canon);
    }

    public static LocalDate datePorExtenso(String s) {
        if (s == null) return null;
        var fmt = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d 'de' MMMM 'de' uuuu")
                .toFormatter(new Locale("pt", "BR"))
                .withResolverStyle(ResolverStyle.STRICT);
        return LocalDate.parse(s, fmt);
    }
}
