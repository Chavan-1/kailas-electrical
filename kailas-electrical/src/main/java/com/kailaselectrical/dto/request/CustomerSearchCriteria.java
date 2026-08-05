package com.kailaselectrical.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchCriteria {
	
	private String keyword;
	
	private int page = 0;

    private int size = 10;

    private String sortBy;

    private String direction = "asc";
}
