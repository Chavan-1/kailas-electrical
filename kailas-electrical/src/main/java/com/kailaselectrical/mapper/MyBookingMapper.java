package com.kailaselectrical.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.kailaselectrical.dto.response.MyBookingResponse;
import com.kailaselectrical.dto.response.MyBookingServiceItemResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.BookingServiceItem;
import com.kailaselectrical.service.TranslationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MyBookingMapper {
	
	private final TranslationService translationService;

	public MyBookingResponse toResponse(Booking booking) {
		
		List<MyBookingServiceItemResponse> services = 
				booking.getServiceItems()
					.stream()
					.map(this::toServiceItemResponse)
					.toList();
		
		BigDecimal totalAmount = booking.getServiceItems()
				.stream()
				.map(BookingServiceItem::getPriceAtBookingTime)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return MyBookingResponse.builder()
				.bookingId(booking.getId())
				.bookingNumber(booking.getBookingNumber())
				.bookingDate(booking.getBookingDate())
				.bookingTime(booking.getBookingTime())
				.status(booking.getStatus())
				.totalAmount(totalAmount)
				.services(services)
				.build();
	}
	
	public MyBookingServiceItemResponse toServiceItemResponse(BookingServiceItem item) {
		
		return MyBookingServiceItemResponse.builder()
				.serviceName(translationService.getServiceName(item.getElectricalService()))
				.price(item.getPriceAtBookingTime())
				.build();
	}
}
