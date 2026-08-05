package com.kailaselectrical.dto.request;

import java.time.LocalDate;

import com.kailaselectrical.enums.BookingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminBookingSearchCriteria {
	
	private String keyword;
	
	private BookingStatus status;
	
	private LocalDate bookingDate;
	
	private int page = 0;
	
	private int size = 10;
	
	private String sortBy;
	
	private String direction = "desc";
	
}
