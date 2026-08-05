package com.kailaselectrical.service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.AdminBookingSearchCriteria;
import com.kailaselectrical.dto.request.UpdateBookingStatusRequest;
import com.kailaselectrical.dto.response.AdminBookingResponse;
import com.kailaselectrical.dto.response.BookingResponse;

public interface AdminBookingService {
	
	PageResponse<AdminBookingResponse> getAllBookings(AdminBookingSearchCriteria criteria);
	
	AdminBookingResponse getBookingById(Long id);

	BookingResponse updateStatus(Long id, UpdateBookingStatusRequest request);
}
