package com.kailaselectrical.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.BookingServiceItem;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.entity.ElectricalServiceTranslations;
import com.kailaselectrical.enums.BookingStatus;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class BookingSpecification {

	public BookingSpecification() {}

	public static Specification<Booking> hasKeyword(String keyword) {
		
		return (root, query, cb) -> {
			
			if (keyword == null || keyword.isBlank()) {
				return cb.conjunction();
			}
			
			query.distinct(true);
			
			String search = "%" + keyword.toLowerCase() + "%";
			
			Join<Booking, Customer> customer = root.join("customer", JoinType.LEFT);
			
			Join<Booking, BookingServiceItem> bookingServiceItem = root.join("serviceItems", JoinType.LEFT);
			
			Join<BookingServiceItem, ElectricalService> electricalService = bookingServiceItem.join("electricalService", JoinType.LEFT);
			
			Join<ElectricalService, ElectricalServiceTranslations> translationJoin = electricalService.join("translations", JoinType.LEFT);
			
			return cb.or(
				cb.like(cb.lower(root.get("bookingNumber")), search),
				cb.like(cb.lower(root.get("remarks")), search),
				cb.like(cb.lower(customer.get("fullName")), search),
				cb.like(cb.lower(customer.get("phoneNumber")), search),
				cb.like(cb.lower(translationJoin.get("serviceName")), search)
				
			);
		};
	}
	
	public static Specification<Booking> hasStatus(BookingStatus status) {
		
		return (root, query, cb) -> {
			
			if (status == null) {
				return cb.conjunction();
			}
			
			return cb.equal(root.get("status"), status);
		};
	}
	
	public static Specification<Booking> hasBookingDate(LocalDate bookingDate) {
		
		return (root, query, cb) -> {
			
			if (bookingDate == null) {
				return cb.conjunction();
			}
			
			return cb.equal(root.get("bookingDate"), bookingDate);
		};
	}
}
