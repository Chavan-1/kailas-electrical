package com.kailaselectrical.service;

import java.util.List;

import com.kailaselectrical.dto.response.MyBookingResponse;

public interface MyBookingService {
	
	List<MyBookingResponse> getMyBookings();
	
	MyBookingResponse getMyBooking(Long bookingId);
	
	void cancelMyBooking(Long bookingId);
}
