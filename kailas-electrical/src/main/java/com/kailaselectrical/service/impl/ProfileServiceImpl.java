package com.kailaselectrical.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kailaselectrical.dto.request.ChangePasswordRequest;
import com.kailaselectrical.dto.request.UpdateProfileRequest;
import com.kailaselectrical.dto.response.ProfileResponse;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.User;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.respository.CustomerRepository;
import com.kailaselectrical.respository.UserRepository;
import com.kailaselectrical.security.AuthenticatedUserService;
import com.kailaselectrical.service.ProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticatedUserService authenticatedUserService;
	
	private final CustomerRepository customerRepository;

	@Override
	public ProfileResponse getProfile() {
		
		User user = authenticatedUserService.getLoggedInUser();
	
		Long customerId = null;
		
		if (user.getRole().name().equals("CUSTOMER")) {
			
			Customer customer = customerRepository.findByEmail(user.getEmail())
					.orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));
			
			customerId = customer.getId();
		}
		
		return ProfileResponse.builder()
				.id(user.getId())
				.customerId(customerId)
				.fullName(user.getFullName())
				.email(user.getEmail())
				.mobileNumber(user.getMobileNumber())
				.role(user.getRole().name())
				.build();
	}

	@Override
	public ProfileResponse updateProfile(UpdateProfileRequest request) {
		
		User user = authenticatedUserService.getLoggedInUser();
		
		user.setFullName(request.getFullName());
		
		user.setMobileNumber(request.getMobileNumber());
		
		User updatedUser = userRepository.save(user);
		
		Long customerId = null;
		
		if (user.getRole().name().equals("CUSTOMER")) {
			
			Customer customer = customerRepository.findByEmail(user.getEmail())
					.orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));
			
			customer.setFullName(request.getFullName());
			
			customer.setPhoneNumber(request.getMobileNumber());
			
			Customer updatedCustomer = customerRepository.save(customer);
			
			customerId = updatedCustomer.getId();
		}
		
		return ProfileResponse.builder()
				.id(updatedUser.getId())
				.customerId(customerId)
				.fullName(updatedUser.getFullName())
				.email(updatedUser.getEmail())
				.mobileNumber(updatedUser.getMobileNumber())
				.role(updatedUser.getRole().name())
				.build();
	}

	@Override
	public ProfileResponse changePassword(ChangePasswordRequest request) {
		
		User user = authenticatedUserService.getLoggedInUser();
		
		if (!passwordEncoder.matches(
				request.getCurrentPassword(), 
				user.getPassword())) {
		
			throw new IllegalArgumentException("Current password is incorrect..");
		}
		
		if (passwordEncoder.matches(
		        request.getNewPassword(),
		        user.getPassword())) {

		    throw new IllegalArgumentException(
		            "New password cannot be the same as the current password.");
		}
		
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		
		User updatedUser = userRepository.save(user);
		
		return ProfileResponse.builder()
				.id(updatedUser.getId())
				.fullName(updatedUser.getFullName())
				.email(updatedUser.getEmail())
				.mobileNumber(updatedUser.getMobileNumber())
				.role(updatedUser.getRole().name())
				.build();
	}
	
}
