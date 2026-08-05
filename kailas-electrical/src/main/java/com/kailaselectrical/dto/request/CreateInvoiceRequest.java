package com.kailaselectrical.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequest {
	
	@NotNull
	private Long bookingId;
	
	@DecimalMin("0.00")
	private BigDecimal discount = BigDecimal.ZERO;
	
	@DecimalMin("0.00")
	private BigDecimal gstPercentage = BigDecimal.valueOf(18);
	
	private String notes;
}
