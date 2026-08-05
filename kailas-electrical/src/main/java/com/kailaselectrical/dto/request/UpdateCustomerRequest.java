package com.kailaselectrical.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerRequest {
	
	@NotBlank(message = "Full name is required")
	private String fullName;
	
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
	private String phoneNumber;
	
	@Email(message = "Invalid email")
	private String email;
	
	@NotBlank(message = "Address is required")
	private String address;
	
	private Boolean active;
}
