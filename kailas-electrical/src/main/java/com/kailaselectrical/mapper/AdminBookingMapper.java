package com.kailaselectrical.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.kailaselectrical.dto.response.AdminBookingResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.BookingServiceItem;

@Component
public class AdminBookingMapper {
	
	public AdminBookingResponse toResponse(Booking booking) {
		
		BigDecimal total = booking.getServiceItems()
				.stream()
				.map(BookingServiceItem::getPriceAtBookingTime)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		List<String> services = booking.getServiceItems()
				.stream()
				.map(item -> item.getElectricalService()
						.getTranslations()
						.stream()
						.findFirst()
						.map(t -> t.getServiceName())
						.orElse("Unknown"))
				.toList();
		
		return AdminBookingResponse.builder()
				.id(booking.getId())
				.bookingNumber(booking.getCustomer().getFullName())
				.mobileNumber(booking.getCustomer().getPhoneNumber())
				.bookingDate(booking.getBookingDate())
				.bookingTime(booking.getBookingTime())
				.status(booking.getStatus())
				.totalAmount(total)
				.services(services)
				.build();
	}
}
