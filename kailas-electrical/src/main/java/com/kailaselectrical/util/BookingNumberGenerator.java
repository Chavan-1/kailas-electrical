package com.kailaselectrical.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingNumberGenerator {

	public BookingNumberGenerator() {}

	public static String generate(long count) {
		
		String date = LocalDate.now()
				.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		return String.format("BK%s%04d", date, count);
	}
	
}
