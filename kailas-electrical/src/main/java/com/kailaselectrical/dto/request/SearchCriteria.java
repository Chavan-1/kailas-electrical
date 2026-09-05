package com.kailaselectrical.dto.request;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {
	
	private Integer page = 0;
	
	private Integer size = 10;
	
	private String sortBy;
	
	private String direction = "asc";
	
	private String keyword;
	
	private Boolean active = true;
	
	private BigDecimal minPrice;
	
	private BigDecimal maxPrice;
}
