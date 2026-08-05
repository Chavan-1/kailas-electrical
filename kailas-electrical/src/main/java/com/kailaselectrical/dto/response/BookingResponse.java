package com.kailaselectrical.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.kailaselectrical.enums.BookingStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingResponse {
	
	private Long id;
	
	private String bookingNumber;
	
	private String customerName;
	
	private String phoneNumber;
	
	private LocalDate bookingDate;
	
	private LocalTime bookingTime;
	
	private BigDecimal estimatedPrice;
	
	private BookingStatus status;
	
	private List<BookingServiceItemResponse> services;
	
	private String remarks;
	
	
}
