package com.kailaselectrical.dto.request;

import com.kailaselectrical.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceSearchCriteria {
	
	private Integer page = 0;
	
	private Integer size = 5;
	
	private String sortBy;
	
	private String direction = "desc";
	
	private String keyword;
	
	private PaymentStatus paymentStatus;
}
