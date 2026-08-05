package com.kailaselectrical.util;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.kailaselectrical.enums.BookingStatus;

public class BookingStatusValidator {

	private BookingStatusValidator() {}
	
	private static final Map<BookingStatus, Set<BookingStatus>> ALLOWED_TRANSITIONS = 
			new EnumMap<>(BookingStatus.class);
	
	static {
		
		ALLOWED_TRANSITIONS.put(
				BookingStatus.PENDING,
				EnumSet.of(
						BookingStatus.CONFIRMED,
						BookingStatus.CANCELLED));
		
		ALLOWED_TRANSITIONS.put(
				BookingStatus.CONFIRMED,
				EnumSet.of(
						BookingStatus.IN_PROGRESS,
						BookingStatus.CANCELLED));
		
		ALLOWED_TRANSITIONS.put(
				BookingStatus.IN_PROGRESS,
				EnumSet.of(
						BookingStatus.COMPLETED,
						BookingStatus.CANCELLED));
		
		ALLOWED_TRANSITIONS.put(
				BookingStatus.IN_PROGRESS,
				EnumSet.of(
						BookingStatus.COMPLETED));
		
		ALLOWED_TRANSITIONS.put(
				BookingStatus.COMPLETED,
				EnumSet.noneOf(BookingStatus.class));
		
		ALLOWED_TRANSITIONS.put(
				BookingStatus.CANCELLED,
				EnumSet.noneOf(BookingStatus.class));
	}
	
	public static boolean isValidTransition(
			BookingStatus currentStatus,
			BookingStatus newStatus) {
		
		return ALLOWED_TRANSITIONS
				.getOrDefault(currentStatus, EnumSet.noneOf(BookingStatus.class))
				.contains(newStatus);
	}
}
