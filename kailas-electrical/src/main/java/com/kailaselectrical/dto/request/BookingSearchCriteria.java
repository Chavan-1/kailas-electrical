package com.kailaselectrical.dto.request;

import java.time.LocalDate;

import com.kailaselectrical.enums.BookingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingSearchCriteria extends SearchCriteria{
	
	private BookingStatus status;
	
	private LocalDate bookingDate;
}
