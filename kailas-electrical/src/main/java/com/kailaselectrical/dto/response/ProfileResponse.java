package com.kailaselectrical.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {
	
	private Long id;
	
	private String fullName;
	
	private String email;
	
	private String mobileNumber;
	
	private String role;
}
