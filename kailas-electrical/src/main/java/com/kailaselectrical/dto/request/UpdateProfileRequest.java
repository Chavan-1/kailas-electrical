package com.kailaselectrical.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
	
	@NotBlank(message = "Full name is required")
	private String fullName;
	
	@NotBlank(message = "Mobile number is required")
	private String mobileNumber;
}
