package com.kailaselectrical.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.dto.response.MyBookingResponse;
import com.kailaselectrical.service.MyBookingService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/my/bookings")
@AllArgsConstructor
public class MyBookingController {

	private final MyBookingService myBookingService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<MyBookingResponse>>> getMyBookings() {
		
		return ResponseEntity.ok(
				ApiResponse.<List<MyBookingResponse>>builder()
					.success(true)
					.message("Bookings fetched successfully")
					.data(myBookingService.getMyBookings())
					.build());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<MyBookingResponse>> getMyBooking(
			@PathVariable Long id) {
		
		return ResponseEntity.ok(
				ApiResponse.<MyBookingResponse>builder()
					.success(true)
					.message("Booking fetched successfully")
					.data(myBookingService.getMyBooking(id))
					.build());
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelMyBooking(
            @PathVariable Long id) {

        myBookingService.cancelMyBooking(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Booking cancelled successfully")
                        .build());
    }
}
