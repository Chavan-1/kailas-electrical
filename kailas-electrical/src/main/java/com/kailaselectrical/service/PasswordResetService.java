package com.kailaselectrical.service;

import com.kailaselectrical.dto.request.ForgotPasswordRequest;
import com.kailaselectrical.dto.request.ResetPasswordRequest;

public interface PasswordResetService {

	void forgotPassword(ForgotPasswordRequest request);
	
	void resetPassword(ResetPasswordRequest request);
}
