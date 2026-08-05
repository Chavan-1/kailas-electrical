package com.kailaselectrical.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.AdminBookingSearchCriteria;
import com.kailaselectrical.dto.request.UpdateBookingStatusRequest;
import com.kailaselectrical.dto.response.AdminBookingResponse;
import com.kailaselectrical.dto.response.BookingResponse;
import com.kailaselectrical.service.AdminBookingService;
import com.kailaselectrical.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {
	
	private final AdminBookingService adminBookingService;
	
	private final BookingService bookingService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<AdminBookingResponse>>> getAllBookings(
			AdminBookingSearchCriteria criteria) {
		
		return ResponseEntity.ok(
				ApiResponse.<PageResponse<AdminBookingResponse>>builder()
				.success(true)
				.message("Bookings fetched successfully")
				.data(adminBookingService.getAllBookings(criteria))
				.build());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<AdminBookingResponse>> getBookingById(
			@PathVariable Long id) {
		
		return ResponseEntity.ok(
				ApiResponse.<AdminBookingResponse>builder()
				.success(true)
				.message("Booking fetched successfully")
				.data(adminBookingService.getBookingById(id))
				.build());
	}
	
	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<BookingResponse>> updateBookingStatus(
			@PathVariable Long id,
			@Valid @RequestBody UpdateBookingStatusRequest request) {
		
		BookingResponse response = bookingService.updateBookingStatus(id, request);
		
		return ResponseEntity.ok(
				ApiResponse.<BookingResponse>builder()
				.success(true)
				.message("Booking status updated successfully")
				.data(response)
				.build());
	}
}
