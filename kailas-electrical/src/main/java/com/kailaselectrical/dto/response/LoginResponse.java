package com.kailaselectrical.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

	private String token;
	
	private String type;
	
	private Long userId;
	
	private String fullName;
	
	private String email;
	
	private String role;
}
