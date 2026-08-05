package com.kailaselectrical.service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.BookingSearchCriteria;
import com.kailaselectrical.dto.request.CreateBookingRequest;
import com.kailaselectrical.dto.request.UpdateBookingRequest;
import com.kailaselectrical.dto.request.UpdateBookingStatusRequest;
import com.kailaselectrical.dto.response.BookingResponse;

public interface BookingService {
	
	BookingResponse createBooking(CreateBookingRequest request);
	
	PageResponse<BookingResponse> getAllBookings(BookingSearchCriteria criteria);
	
	BookingResponse getBookingById(Long id);
	
	BookingResponse updateBooking(Long id, UpdateBookingRequest request);
	
	BookingResponse updateBookingStatus(Long id, UpdateBookingStatusRequest request);
	
	void cancelBooking(Long id);
}
