package com.kailaselectrical.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyBookingServiceItemResponse {
	
	private String serviceName;
	
	private BigDecimal price;
}
