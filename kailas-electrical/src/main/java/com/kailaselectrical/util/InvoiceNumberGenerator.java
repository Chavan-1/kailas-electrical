package com.kailaselectrical.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InvoiceNumberGenerator {

	public InvoiceNumberGenerator() {}

	public static String generate(long sequence) {
		
		String date = LocalDate.now()
				.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		return "INV" + date + String.format("%04d", sequence);
	}
	
}
