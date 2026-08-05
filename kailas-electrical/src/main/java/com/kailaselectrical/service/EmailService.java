package com.kailaselectrical.service;

public interface EmailService {

	void sendBookingConfirmation(Long bookingId);
	
	void sendInvoice(Long invoiceId);
	
	void sendWelcomeEmail(Long customerId);
}
