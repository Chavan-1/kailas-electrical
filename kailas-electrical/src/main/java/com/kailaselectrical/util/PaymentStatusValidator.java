package com.kailaselectrical.util;

import com.kailaselectrical.enums.PaymentStatus;

public class PaymentStatusValidator {

	private PaymentStatusValidator() {}
	
	public static boolean isValidTransition(
			PaymentStatus current,
			PaymentStatus next) {
		
		switch (current) {
		
		case PENDING: 
			
			return next == PaymentStatus.PARTIALLY_PAID || next == PaymentStatus.PAID;

		case PARTIALLY_PAID:
			
			return next == PaymentStatus.PAID;
		
		case PAID:
			
			return false;
			
		default:
			
			return false;
		}
	}
}
