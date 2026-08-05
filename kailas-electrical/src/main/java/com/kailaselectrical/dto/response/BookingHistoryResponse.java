package com.kailaselectrical.dto.response;

import java.time.LocalDateTime;

import com.kailaselectrical.enums.BookingStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookingHistoryResponse {

	private BookingStatus oldStatus;
	
	private BookingStatus newStatus;
	
	private String remarks;
	
	private String changedBy;
	
	private LocalDateTime changedAt;
}
