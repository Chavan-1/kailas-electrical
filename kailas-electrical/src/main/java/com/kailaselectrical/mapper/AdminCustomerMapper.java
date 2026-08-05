package com.kailaselectrical.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.kailaselectrical.dto.response.AdminCustomerResponse;
import com.kailaselectrical.entity.Customer;

@Component
public class AdminCustomerMapper {
	
	public AdminCustomerResponse toResponse(
			Customer customer,
			long totalBookings,
			BigDecimal totalSpent) {
		
		return AdminCustomerResponse.builder()
				.id(customer.getId())
				.fullName(customer.getFullName())
				.email(customer.getEmail())
				.mobileNumber(customer.getPhoneNumber())
				.active(customer.getActive())
				.totalBookings(totalBookings)
				.totalSpent(totalSpent==null?BigDecimal.ZERO:totalSpent)
				.build();
	}
}
