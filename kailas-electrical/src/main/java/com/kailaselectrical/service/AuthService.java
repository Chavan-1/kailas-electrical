package com.kailaselectrical.service;

import com.kailaselectrical.dto.request.LoginRequest;
import com.kailaselectrical.dto.request.RegisterRequest;
import com.kailaselectrical.dto.response.LoginResponse;

public interface AuthService {
	
	void register(RegisterRequest request);
	
	LoginResponse login(LoginRequest request);
}
