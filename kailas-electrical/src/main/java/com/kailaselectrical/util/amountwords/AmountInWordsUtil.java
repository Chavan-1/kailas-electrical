package com.kailaselectrical.util.amountwords;

public class AmountInWordsUtil {

	private AmountInWordsUtil() {}
	
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
	
	private static String convert(int number) {
		if (number < 20)
            return units[number];

        if (number < 100)
            return tens[number / 10] +
                    ((number % 10 != 0) ? " " + convert(number % 10) : "");

        if (number < 1000)
            return convert(number / 100) + " Hundred" +
                    ((number % 100 != 0) ? " " + convert(number % 100) : "");

        if (number < 100000)
            return convert(number / 1000) + " Thousand" +
                    ((number % 1000 != 0) ? " " + convert(number % 1000) : "");

        if (number < 10000000)
            return convert(number / 100000) + " Lakh" +
                    ((number % 100000 != 0) ? " " + convert(number % 100000) : "");

        return convert(number / 10000000) + " Crore" +
                ((number % 10000000 != 0) ? " " + convert(number % 10000000) : "");
	}
	
	public static String toWords(double amount) {
		
		int rupees = (int) amount;
		
		int paise = (int) Math.round((amount - rupees) * 100);
		
		String result = convert(rupees) + " Rupees";
		
		if (paise > 0) {
			
			result += " and " + convert(paise) + " Paise";
		}
		
		return result + " Only";
		
	}
}
