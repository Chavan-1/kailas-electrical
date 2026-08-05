package com.kailaselectrical.dto.request;

import com.kailaselectrical.enums.BookingStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookingStatusRequest {
	
	@NotNull
	private BookingStatus status;
}
