package com.kailaselectrical.util;

import java.util.List;

public class BookingConstants {
	
	private BookingConstants() {}
	
	public static final String DEFAULT_SORT_FIELD = "bookingDate";
	
	public static final List<String> ALLOWED_SORT_FIELDS = List.of(
			"bookingDate",
			"bookingTime",
			"status",
			"estimatedPrice",
			"createdAt",
			"upatedAt"
	);
}
