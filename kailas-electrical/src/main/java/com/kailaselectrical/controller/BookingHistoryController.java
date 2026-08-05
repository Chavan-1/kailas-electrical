package com.kailaselectrical.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.dto.response.BookingHistoryResponse;
import com.kailaselectrical.service.BookingHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
public class BookingHistoryController {

	private final BookingHistoryService bookingHistoryService;
	
	@GetMapping("/{bookingId}/history")
	public ResponseEntity<ApiResponse<List<BookingHistoryResponse>>> getHistory(
			@PathVariable Long bookingId) {
		
		return ResponseEntity.ok(
				ApiResponse.<List<BookingHistoryResponse>>builder()
				.success(true)
				.message("Booking history fetched successfully")
				.data(bookingHistoryService.getHistory(bookingId))
				.build());
	}
}
