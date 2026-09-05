
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
public class InvoiceItemResponse {
	
	private String serviceName;
	
	private Integer quantity;
	
	private BigDecimal unitPrice;
	
	private BigDecimal lineTotal;
}
