package com.kailaselectrical.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.User;
import com.kailaselectrical.enums.Role;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.respository.CustomerRepository;
import com.kailaselectrical.respository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final UserRepository userRepository;
    
    private final CustomerRepository customerRepository;

    public User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not authenticated.");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }
    
    public Customer getLoggedInCustomer() {
    	
    	User user = getLoggedInUser();
    	
    	if (user.getRole() != Role.CUSTOMER) {
    		
    		throw new IllegalStateException("Logged-in user is not a customer");
    	}

        return customerRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer profile not found."));
    }
}
