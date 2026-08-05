package com.kailaselectrical.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.kailaselectrical.enums.BookingStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyBookingResponse {

	private Long bookingId;
	
	private String bookingNumber;
	
	private LocalDate bookingDate;
	
	private LocalTime bookingTime;
	
	private BookingStatus status;
	
	private BigDecimal totalAmount;
	
	List<MyBookingServiceItemResponse> services;
}
