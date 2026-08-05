package com.kailaselectrical.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerSummaryResponse {

	private long totalBookings;
	
	private long completedBookings;
	
	private long cancelledBookings;
	
	private long pendingBookings;
	
	private BigDecimal totalSpent;
}
