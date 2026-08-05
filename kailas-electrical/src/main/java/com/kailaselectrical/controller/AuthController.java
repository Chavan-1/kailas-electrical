package com.kailaselectrical.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.dto.request.LoginRequest;
import com.kailaselectrical.dto.request.RegisterRequest;
import com.kailaselectrical.dto.response.AuthResponse;
import com.kailaselectrical.dto.response.LoginResponse;
import com.kailaselectrical.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
		
		authService.register(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.<Void>builder()
						.success(true)
						.message("Customer registered successfully.")
						.build());
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
		
		LoginResponse response = authService.login(request);
		
		return ResponseEntity.ok(
				ApiResponse.<LoginResponse>builder()
				.success(true)
				.message("Login successful")
				.data(response)
				.build());
	}
	
}
