package com.kailaselectrical.util.amountwords;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.ibm.icu.text.RuleBasedNumberFormat;

@Component
public class MarathiAmountInWordsConverter implements AmountInWordsConverter{

	@Override
	public String convert(double amount) {
		RuleBasedNumberFormat formatter =
                new RuleBasedNumberFormat(Locale.of("mr", "IN"),
                        RuleBasedNumberFormat.SPELLOUT);
		System.out.println(formatter.format(5450));
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

        sb.append(" फक्त");

        return sb.toString();
	}

}
