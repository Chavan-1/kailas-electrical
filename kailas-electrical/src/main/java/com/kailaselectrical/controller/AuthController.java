package com.kailaselectrical.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.dto.request.ForgotPasswordRequest;
import com.kailaselectrical.dto.request.LoginRequest;
import com.kailaselectrical.dto.request.RegisterRequest;
import com.kailaselectrical.dto.request.ResetPasswordRequest;
import com.kailaselectrical.dto.response.AuthResponse;
import com.kailaselectrical.dto.response.LoginResponse;
import com.kailaselectrical.service.AuthService;
import com.kailaselectrical.service.PasswordResetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	private final PasswordResetService passwordResetService;

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
	
	@PostMapping("/forgot-password")
	public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		
		passwordResetService.forgotPassword(request);
		
		return ResponseEntity.ok(
				ApiResponse.<Void>builder()
				.success(true)
				.message("If an account exists for this email. a password reset link has been sent.")
				.build());
	}
	
	
	@PostMapping("/reset-password")
	public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		
		passwordResetService.resetPassword(request);
		
		return ResponseEntity.ok(
				ApiResponse.<Void>builder()
				.success(true)
				.message("Password reset successfully.")
				.build());
	}
	
	
}
