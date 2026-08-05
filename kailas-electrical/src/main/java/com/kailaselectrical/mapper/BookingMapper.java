package com.kailaselectrical.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kailaselectrical.dto.request.CreateBookingRequest;
import com.kailaselectrical.dto.request.UpdateBookingRequest;
import com.kailaselectrical.dto.request.UpdateServiceRequest;
import com.kailaselectrical.dto.response.BookingResponse;
import com.kailaselectrical.dto.response.BookingServiceItemResponse;
import com.kailaselectrical.dto.response.CustomerResponse;
import com.kailaselectrical.dto.response.ServiceResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.service.TranslationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingMapper {
	
	private final TranslationService translationService;
	
	public Booking toEntity(CreateBookingRequest request) {
		
		Booking booking = new Booking();
		
		booking.setBookingDate(request.getBookingDate());
		booking.setBookingTime(request.getBookingTime());
		booking.setRemarks(request.getRemarks());
		
		return booking;
	}
	
	public BookingResponse toResponse(Booking booking) {
		
		List<BookingServiceItemResponse> services = 
				booking.getServiceItems()
					   .stream()
					   .map(item -> 
					           BookingServiceItemResponse.builder()
							   .serviceId(item.getElectricalService().getId())
							   .serviceName(translationService.getServiceName(item.getElectricalService()))
							   .priceAtBookingTime(item.getPriceAtBookingTime())
							   .build())
					    .toList();
		
		return BookingResponse.builder()
				.id(booking.getId())
				.bookingNumber(booking.getBookingNumber())
				.customerName(booking.getCustomer().getFullName())
				.phoneNumber(booking.getCustomer().getPhoneNumber())
				.services(services)
				.bookingDate(booking.getBookingDate())
				.bookingTime(booking.getBookingTime())
				.estimatedPrice(booking.getEstimatedPrice())
				.status(booking.getStatus())
				.remarks(booking.getRemarks())
				.build();
	}
	
	public void updateEntity(Booking booking, UpdateBookingRequest request) {
		
		booking.setBookingDate(request.getBookingDate());
		booking.setBookingTime(request.getBookingTime());
		booking.setRemarks(request.getRemarks());
	}
}
