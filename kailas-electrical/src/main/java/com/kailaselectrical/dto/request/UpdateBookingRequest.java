package com.kailaselectrical.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookingRequest {
	
	@NotNull(message = "Booking date is required")
	private LocalDate bookingDate;
	
	@NotNull(message = "Booking time is required")
	private LocalTime bookingTime;
	
	private String remarks;
}
