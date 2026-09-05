package com.kailaselectrical.service;

public interface EmailService {

	void sendBookingConfirmation(Long bookingId);
	
	void sendInvoice(Long invoiceId);
	
	void sendWelcomeEmail(Long customerId);
	
	void sendPasswordResetEmail(String toEmail, String fullName, String resetLink);
}
