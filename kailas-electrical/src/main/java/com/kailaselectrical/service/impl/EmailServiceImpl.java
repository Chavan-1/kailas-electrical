package com.kailaselectrical.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.Invoice;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.pdf.InvoicePdfGenerator;
import com.kailaselectrical.respository.BookingRepository;
import com.kailaselectrical.respository.CustomerRepository;
import com.kailaselectrical.respository.InvoiceRepository;
import com.kailaselectrical.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{

	private final JavaMailSender mailSender;
	
	private final BookingRepository bookingRepository;
	
	private final InvoiceRepository invoiceRepository;
	
	private final CustomerRepository customerRepository;
	
	private final InvoicePdfGenerator pdfGenerator;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Override
	public void sendWelcomeEmail(Long customerId) {
		
		Customer customer = customerRepository.findById(customerId)
					.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		
		try {

			log.info("Sending welcome email to {}", customer.getEmail());
			System.out.println("Sending welcome email to "+ customer.getEmail());
			
			MimeMessage message = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
			helper.setFrom(fromEmail);
			
			helper.setTo(customer.getEmail());
			
			helper.setSubject("Welcome to Kailash Electrical Service");
			
			helper.setText("""
					Dear %s,
					
					Welcome to Kailash Electrical Service.
					
					Your customer account has been created successfulyy.
					
					We look forward to serving you.
					
					Regards,
					Kailash Electrical Service
					""");
			
			mailSender.send(message);
			
			log.info("Welcome email sent successfully to {}", customer.getEmail());
			System.out.println("Welcome email sent successfully to {}" + customer.getEmail());
			
		} catch (Exception e) {
			
			log.error("Failed to send welcome email", e);
			System.out.println("Failed to send welcome email " + e);
		}
	}
	
	@Override
	public void sendBookingConfirmation(Long bookingId) {
		
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
	
	try {

		log.info("Sending booking confirmation email for Booking {}", booking.getBookingNumber());
		System.out.println("Sending booking confirmation email for Booking {}" + booking.getBookingNumber());
		
		MimeMessage message = mailSender.createMimeMessage();
		
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		helper.setFrom(fromEmail);
		helper.setTo(booking.getCustomer().getEmail());
		helper.setSubject("Booking Confirmation - Kailash Electrical Service");
		helper.setText("""
				Dear %s,
				
				Your booking has been confirmed.

                Booking Number : %s

                Booking Date : %s

                Booking Time : %s

                Status : %s

                Thank you for choosing Kailash Electrical Service.

                Regards,
                Kailash Electrical Service
				"""
				.formatted(
						booking.getCustomer().getFullName(),
						booking.getBookingNumber(),
						booking.getBookingDate(),
						booking.getBookingTime(),
						booking.getStatus()));
		
		mailSender.send(message);
		log.info("Booking confirmation email sent successfully.");
		
		} catch (Exception e) {
			
			log.error("Unable to send booking confirmation email", e);
		}
		
	}

	@Override
	@Async
	public void sendInvoice(Long invoiceId) {
	
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
	
		try {
			
			log.info("Generating PDF for Invoice {}", invoice.getInvoiceNumber());
			System.out.println("Generating PDF for Invoice " + invoice.getInvoiceNumber());
			
			byte[] pdf = pdfGenerator.generate(invoice);
			
			MimeMessage message = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
			helper.setFrom(fromEmail);
			
			helper.setTo(invoice.getBooking().getCustomer().getEmail());
			
			helper.setSubject("Invoice - " + invoice.getInvoiceNumber());
			
			helper.setText("""
					Dear %s,
					
					Please find your invoice attached.

                    Invoice Number : %s

                    Booking Number : %s

                    Total Amount : ₹ %s

	                Thank you for choosing Kailash Electrical Service.

	                Regards,
	                Kailash Electrical Service
					"""
					.formatted(
							invoice.getBooking().getCustomer().getFullName(),
							invoice.getInvoiceNumber(),
							invoice.getBooking().getBookingNumber(),
							invoice.getTotalAmount()));
			
			helper.addAttachment(
					"Invoice_" + invoice.getInvoiceNumber() + ".pdf", 
					new InputStreamSource() {
						
						@Override
						public InputStream getInputStream() throws IOException {
							
							return new ByteArrayInputStream(pdf);
						}
					});
			
			log.info("PDF generated successfully.");
			System.out.println("PDF generated successfully.");
			
			mailSender.send(message);
			
			log.info("Invoice email sent successfully.");
			System.out.println("Invoice email sent successfully.");
			
			} catch (Exception e) {
				
				log.error("Unable to send invoice email for invoice {}: {}", invoice.getInvoiceNumber(), e.getMessage());
				throw new RuntimeException("Unable to send invoice email", e);
				
			}
			
	}

	@Override
	public void sendPasswordResetEmail(String toEmail, String fullName, String resetLink) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(toEmail);
		message.setSubject("Reset your password");
		
		message.setText(
				"Hello " + fullName + ",\n\n"
				+ "We received a request to reset your password.\n\n"
				+ "Use the link below to reset it:\n"
				+ resetLink
				+ "\n\nThis link will expire in 30 minutes."
		);
		
		mailSender.send(message);
		log.info("Password reset link: {}", resetLink);
		
	}
}
