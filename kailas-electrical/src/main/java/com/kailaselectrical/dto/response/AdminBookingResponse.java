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
public class AdminBookingResponse {
	
	private Long id;
	
	private String bookingNumber;
	
	private String customerName;
	
	private String mobileNumber;
	
	private LocalDate bookingDate;
	
	private LocalTime bookingTime;
	
	private BookingStatus status;
	
	private BigDecimal totalAmount;
	
	private List<String> services;
}
