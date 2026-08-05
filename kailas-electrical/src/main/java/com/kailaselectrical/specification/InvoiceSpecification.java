package com.kailaselectrical.specification;

import org.springframework.data.jpa.domain.Specification;

import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.Invoice;
import com.kailaselectrical.enums.PaymentStatus;

import jakarta.persistence.criteria.Join;

public class InvoiceSpecification {

	public InvoiceSpecification() {}

	public static Specification<Invoice> hasKeyword(String keyword) {
		
		return (root, query, cb) -> {
			
			if (keyword == null || keyword.isBlank()) {
				return cb.conjunction();
			}
			
			query.distinct(true);
			
			String search = "%" + keyword.toLowerCase() + "%";
			
			Join<Invoice, Booking> booking = root.join("booking");
			
			Join<Booking, Customer> customer = booking.join("customer");
			
			return cb.or(
				cb.like(cb.lower(root.get("invoiceNumber")), search),
				cb.like(cb.lower(booking.get("bookingNumber")), search),
				cb.like(cb.lower(customer.get("fullName")), search)
			);
		};
	}
	
	public static Specification<Invoice> hasPaymentStatus(PaymentStatus paymentStatus) {
		
		return (root, query, cb) -> {
			
			if (paymentStatus == null) {
				return cb.conjunction();
			}
			
			return cb.equal(root.get("paymentStatus"), paymentStatus);
		};
	}
}
