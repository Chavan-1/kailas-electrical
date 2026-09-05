package com.kailaselectrical.service.impl;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.kailaselectrical.dto.response.MyBookingResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.enums.BookingStatus;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.mapper.MyBookingMapper;
import com.kailaselectrical.respository.BookingRepository;
import com.kailaselectrical.security.AuthenticatedUserService;
import com.kailaselectrical.service.MyBookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyBookingServiceImpl implements MyBookingService{

	private final BookingRepository bookingRepository;
	
	private final MyBookingMapper mapper;
	
	private final AuthenticatedUserService authenticatedUserService;
	
	@Override
	public List<MyBookingResponse> getMyBookings() {
		
		Customer customer = authenticatedUserService.getLoggedInCustomer();
		
		return bookingRepository.findByCustomer(customer)
				.stream()
				.map(mapper::toResponse)
				.toList();
	}

	@Override
	public MyBookingResponse getMyBooking(Long bookingId) {
		
		Customer customer = authenticatedUserService.getLoggedInCustomer();
		
		Booking booking = bookingRepository.findByIdAndCustomer(bookingId, customer)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
		
		return mapper.toResponse(booking);
	}

	@Override
	public void cancelMyBooking(Long bookingId) {
		
		Customer customer = authenticatedUserService.getLoggedInCustomer();

	    Booking booking = bookingRepository
	            .findByIdAndCustomer(bookingId, customer)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("Booking not found"));

	    if (booking.getStatus() == BookingStatus.COMPLETED ||
	        booking.getStatus() == BookingStatus.CANCELLED) {

	        throw new IllegalStateException(
	                "Booking cannot be cancelled");
	    }

	    booking.setStatus(BookingStatus.CANCELLED);

	    bookingRepository.save(booking);
		
	}

}
