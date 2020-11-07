package com.github.eokasta.economy.utils.provider;

import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@RequiredArgsConstructor
public class NumberFormatter {

    private static final DecimalFormat FORMAT = new DecimalFormat("#,###.##", new DecimalFormatSymbols(new Locale("pt", "BR")));
    private final Settings settings;

    public String format(double number) {
        if (number == 0)
            return FORMAT.format(number);

        int index = (int) Math.floor((Math.log(number) / Math.log(1000)));
        return FORMAT.format(number / Math.pow(1000, index)) + settings.getNumberFormatChars().get(index);
    }

}
