package com.kailaselectrical.service;

import java.util.List;

import com.kailaselectrical.dto.response.BookingHistoryResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.enums.BookingStatus;

public interface BookingHistoryService {

	void saveHistory(
			Booking booking,
			BookingStatus oldStatus,
			BookingStatus newStatus,
			String remarks);
	
	List<BookingHistoryResponse> getHistory(Long bookingId);
}
