package com.kailaselectrical.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

	@NotBlank(message = "Full name is required")
	private String fullName;
	
	@Email(message = "Invalid email")
	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password must contain at least 8 characters")
	private String password;
	
	@NotBlank
	@Pattern(
	        regexp = "^[6-9]\\d{9}$",
	        message = "Invalid mobile number"
	    )
	private String mobileNumber;
	
	@NotBlank(message = "Address is required")
	private String address;
}
