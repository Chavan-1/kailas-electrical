package com.kailaselectrical.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerResponse {
	
	private Long id;
	
	private String fullName;
	
	private String phoneNumber;
	
	private String email;
	
	private String address;
	
	private Boolean active;
}
