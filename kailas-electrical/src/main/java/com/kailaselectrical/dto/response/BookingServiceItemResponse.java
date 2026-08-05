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
public class BookingServiceItemResponse {
	
	private Long serviceId;
	
	private String serviceName;
	
	private BigDecimal priceAtBookingTime;
}
