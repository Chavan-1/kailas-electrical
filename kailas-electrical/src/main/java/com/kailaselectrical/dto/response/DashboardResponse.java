package com.kailaselectrical.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

	private long totalCustomers;
	
	private long totalServices;
	
	private long totalBookings;
	
	private long pendingBookings;
	
	private long completedBookings;
	
	private long cancelledBookings;
	
	private long todayBookings;
	
	private long pendingPayments;
	
	private BigDecimal totalRevenue;
	
	private BigDecimal monthlyRevenue;
}
