package com.kailaselectrical.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.dto.request.ChangePasswordRequest;
import com.kailaselectrical.dto.request.UpdateProfileRequest;
import com.kailaselectrical.dto.response.ProfileResponse;
import com.kailaselectrical.service.ProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Profile", description = "Profile APIs")
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;
	
	@GetMapping
	public ApiResponse<ProfileResponse> getProfile() {
		
		return ApiResponse.<ProfileResponse>builder()
				.success(true)
				.message("Profile fetched successfully")
				.data(profileService.getProfile())
				.build();
	}
	
	@PutMapping
	public ApiResponse<ProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
		
		return ApiResponse.<ProfileResponse>builder()
				.success(true)
				.message("Profile updated successfully")
				.data(profileService.updateProfile(request))
				.build();
	}
	
	@PutMapping("/change-password")
	public ResponseEntity<ApiResponse<ProfileResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		
		return ResponseEntity.ok(
				ApiResponse.<ProfileResponse>builder()
				.success(true)
				.message("Password changed successfully")
				.data(profileService.changePassword(request))
				.build());
	}
}
