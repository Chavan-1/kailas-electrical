package com.kailaselectrical.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kailaselectrical.dto.response.MyBookingResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.User;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.mapper.MyBookingMapper;
import com.kailaselectrical.respository.BookingRepository;
import com.kailaselectrical.respository.CustomerRepository;
import com.kailaselectrical.respository.UserRepository;
import com.kailaselectrical.security.AuthenticatedUserService;
import com.kailaselectrical.service.MyBookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyBookingServiceImpl implements MyBookingService{

	private final BookingRepository bookingRepository;
	
	private final CustomerRepository customerRepository;
	
	private final MyBookingMapper mapper;
	
	private final AuthenticatedUserService authenticatedUserService;
	
	@Override
	public List<MyBookingResponse> getMyBookings() {
		
		User user = authenticatedUserService.getLoggedInUser();
		
		Customer customer = customerRepository.findByEmail(user.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		
		return bookingRepository.findByCustomer(customer)
				.stream()
				.map(mapper::toResponse)
				.toList();
	}

	@Override
	public MyBookingResponse getMyBooking(Long bookingId) {
		
		User user = authenticatedUserService.getLoggedInUser();
		
		Customer customer = customerRepository.findByEmail(user.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		
		Booking booking = bookingRepository.findByIdAndCustomer(bookingId, customer)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
		
		return mapper.toResponse(booking);
	}

}
