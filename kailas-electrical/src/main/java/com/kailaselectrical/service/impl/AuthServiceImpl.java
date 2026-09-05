package com.kailaselectrical.service.impl;

import com.kailaselectrical.respository.CustomerRepository;
import com.kailaselectrical.security.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kailaselectrical.dto.request.LoginRequest;
import com.kailaselectrical.dto.request.RegisterRequest;
import com.kailaselectrical.dto.response.LoginResponse;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.User;
import com.kailaselectrical.enums.Role;
import com.kailaselectrical.exception.ResourceAlreadyExistsException;
import com.kailaselectrical.respository.UserRepository;
import com.kailaselectrical.security.JwtService;
import com.kailaselectrical.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	private final CustomerRepository customerRepository;

	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	@Override
	@Transactional
	public void register(RegisterRequest request) {
		
		if (userRepository.existsByEmail(request.getEmail())) {
			
			throw new ResourceAlreadyExistsException("Email already registered");
		}
		
		if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
	        
			throw new ResourceAlreadyExistsException("Mobile number already registered");
	    }
		
		User user = new User();
		
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setMobileNumber(request.getMobileNumber());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.CUSTOMER);
		user.setEnabled(true);
		
		User savedUser = userRepository.save(user);
		
		Customer customer = new Customer();
		
		customer.setUser(savedUser);
		customer.setFullName(savedUser.getFullName());
		customer.setEmail(savedUser.getEmail());
		customer.setPhoneNumber(savedUser.getMobileNumber());
		customer.setAddress(request.getAddress());
		customer.setActive(true);
		
		customerRepository.save(customer);
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		String token = jwtService.generateToken(userDetails);
		
		return LoginResponse.builder()
				.token(token)
				.type("Bearer")
				.userId(userDetails.getUser().getId())
				.fullName(userDetails.getUser().getFullName())
				.email(userDetails.getUser().getEmail())
				.role(userDetails.getUser().getRole().name())
				.build();
	}

	

}
