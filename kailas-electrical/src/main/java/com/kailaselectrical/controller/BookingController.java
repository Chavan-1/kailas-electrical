package com.kailaselectrical.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.BookingSearchCriteria;
import com.kailaselectrical.dto.request.CreateBookingRequest;
import com.kailaselectrical.dto.request.UpdateBookingRequest;
import com.kailaselectrical.dto.response.BookingResponse;
import com.kailaselectrical.service.BookingService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Booking", description = "Booking Management APIs")
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
	
	private final BookingService bookingService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody CreateBookingRequest request) {
		
		BookingResponse response = bookingService.createBooking(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.<BookingResponse>builder()
						.success(true)
						.message("Booking created successfully.")
						.data(response)
						.build());
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> getAllBookings(BookingSearchCriteria criteria) {
		
		PageResponse<BookingResponse> response = bookingService.getAllBookings(criteria);
		
		return ResponseEntity.ok(
				ApiResponse.<PageResponse<BookingResponse>>builder()
				.success(true)
				.message("Bookings feteched successfully")
				.data(response)
				.build());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id) {
		
		BookingResponse response = bookingService.getBookingById(id);
		
		return ResponseEntity.ok(
				ApiResponse.<BookingResponse>builder()
				.success(true)
				.message("Booking feteched successfully")
				.data(response)
				.build());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<BookingResponse>> updateBooking(
			@PathVariable Long id,
			@Valid @RequestBody UpdateBookingRequest request) {
		
		BookingResponse response = bookingService.updateBooking(id, request);
		
		return ResponseEntity.ok(
				ApiResponse.<BookingResponse>builder()
				.success(true)
				.message("Booking updated successfully")
				.data(response)
				.build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> cancelBooking(@PathVariable Long id) {
		
		bookingService.cancelBooking(id);
		
		return ResponseEntity.ok(
				ApiResponse.<Void>builder()
				.success(true)
				.message("Booking cancelled successfully")
				.build());
	}
}
