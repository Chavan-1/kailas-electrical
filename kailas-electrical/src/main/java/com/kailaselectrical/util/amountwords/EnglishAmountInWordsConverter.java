package com.kailaselectrical.util.amountwords;

import org.springframework.stereotype.Component;

@Component
public class EnglishAmountInWordsConverter implements AmountInWordsConverter{

	private static final String[] units = {
			"", "One", "Two", "Three", "Four", "Five",
            "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen",
            "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
	};
	
	private static final String[] tens = {
			"", "", "Twenty", "Thirty", "Forty",
            "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
	};
	
	@Override
	public String convert(double amount) {
		
		int rupees = (int) amount;
		
		int paise = (int) Math.round((amount - rupees) * 100);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(convertNumber(rupees));
		
		if (paise > 0) {
			
			sb.append(" and ");
			sb.append(convertNumber(paise));
			sb.append(" Paise ");
		}
		
		sb.append(" Only ");
		
		return sb.toString();
	}

	private String convertNumber(int number) {

        if (number == 0) {
            return "Zero";
        }

        if (number < 20) {
            return units[number];
        }

        if (number < 100) {
            return tens[number / 10]
                    + ((number % 10 != 0)
                    ? " " + convertNumber(number % 10)
                    : "");
        }

        if (number < 1000) {
            return convertNumber(number / 100)
                    + " Hundred"
                    + ((number % 100 != 0)
                    ? " " + convertNumber(number % 100)
                    : "");
        }

        if (number < 100000) {
            return convertNumber(number / 1000)
                    + " Thousand"
                    + ((number % 1000 != 0)
                    ? " " + convertNumber(number % 1000)
                    : "");
        }

        if (number < 10000000) {
            return convertNumber(number / 100000)
                    + " Lakh"
                    + ((number % 100000 != 0)
                    ? " " + convertNumber(number % 100000)
                    : "");
        }

        return convertNumber(number / 10000000)
                + " Crore"
                + ((number % 10000000 != 0)
                ? " " + convertNumber(number % 10000000)
                : "");
    }
}
