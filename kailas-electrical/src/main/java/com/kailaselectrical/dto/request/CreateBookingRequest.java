package com.kailaselectrical.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookingRequest {

	@NotNull
	private Long customerId;
	
	@NotEmpty(message = "At least one service is required")
	private List<Long> serviceIds;
	
	@NotNull
	private LocalDate bookingDate;
	
	@NotNull
	private LocalTime bookingTime;
	
	private String remarks;
}
