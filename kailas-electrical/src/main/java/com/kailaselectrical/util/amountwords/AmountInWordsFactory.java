package com.kailaselectrical.util.amountwords;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AmountInWordsFactory {

    private final EnglishAmountInWordsConverter english;
    private final HindiAmountInWordsConverter hindi;
    private final MarathiAmountInWordsConverter marathi;

    public String convert(BigDecimal amount) {

        Locale locale = LocaleContextHolder.getLocale();

        switch (locale.getLanguage()) {

            case "hi":
                return hindi.convert(amount.doubleValue());

            case "mr":
                return marathi.convert(amount.doubleValue());

            default:
                return english.convert(amount.doubleValue());
        }
    }
}