package com.kailaselectrical.util.amountwords;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.ibm.icu.text.RuleBasedNumberFormat;

@Component
public class HindiAmountInWordsConverter implements AmountInWordsConverter{

	@Override
	public String convert(double amount) {
		RuleBasedNumberFormat formatter =
                new RuleBasedNumberFormat(new Locale("hi", "IN"),
                        RuleBasedNumberFormat.SPELLOUT);

        long rupees = (long) amount;

        int paise = (int) Math.round((amount - rupees) * 100);

        StringBuilder sb = new StringBuilder();

        sb.append(formatter.format(rupees));

        sb.append(" रुपये");

        if (paise > 0) {
            sb.append(" ");
            sb.append(formatter.format(paise));
            sb.append(" पैसे");
        }

        sb.append(" मात्र");

        return sb.toString();
	}

}
