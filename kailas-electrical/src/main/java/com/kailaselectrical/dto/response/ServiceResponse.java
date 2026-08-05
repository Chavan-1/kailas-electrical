package com.kailaselectrical.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceResponse {
	
	private Long id;
	
	private String serviceName;
	
	private String description;
	
	private BigDecimal basePrice;
	
	private Integer estimatedDuration;
	
	private Boolean active;
}
