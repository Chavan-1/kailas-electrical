package com.kailaselectrical.service;

import com.kailaselectrical.dto.request.ChangePasswordRequest;
import com.kailaselectrical.dto.request.UpdateProfileRequest;
import com.kailaselectrical.dto.response.ProfileResponse;

public interface ProfileService {
	
	ProfileResponse getProfile();
	
	ProfileResponse updateProfile(UpdateProfileRequest request);
	
	ProfileResponse changePassword(ChangePasswordRequest request);
}
