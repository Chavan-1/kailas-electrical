package com.kailaselectrical.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminCustomerResponse {

	private Long id;
	
	private String fullName;
	
	private String email;
	
	private String mobileNumber;
	
	private boolean active;
	
	private long totalBookings;
	
	private BigDecimal totalSpent;
}
