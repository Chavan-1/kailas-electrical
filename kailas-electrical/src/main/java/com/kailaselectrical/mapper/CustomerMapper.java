package com.kailaselectrical.mapper;

import org.springframework.stereotype.Component;

import com.kailaselectrical.dto.request.CreateCustomerRequest;
import com.kailaselectrical.dto.request.UpdateCustomerRequest;
import com.kailaselectrical.dto.request.UpdateServiceRequest;
import com.kailaselectrical.dto.response.CustomerResponse;
import com.kailaselectrical.dto.response.ServiceResponse;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.ElectricalService;

@Component
public class CustomerMapper {
	
	public Customer toEntity(CreateCustomerRequest request) {
		
		Customer customer = new Customer();
		
		customer.setFullName(request.getFullName());
		customer.setPhoneNumber(request.getPhoneNumber());
		customer.setEmail(request.getEmail());
		customer.setAddress(request.getAddress());
		
		return customer;
	}
	
	public CustomerResponse toResponse(Customer customer) {
		
		return CustomerResponse.builder()
				.id(customer.getId())
				.fullName(customer.getFullName())
				.phoneNumber(customer.getPhoneNumber())
				.email(customer.getEmail())
				.address(customer.getAddress())
				.active(customer.getActive())
				.build();
	}
	
	public void updateEntity(Customer customer, UpdateCustomerRequest request) {
		
		customer.setFullName(request.getFullName());
		customer.setPhoneNumber(request.getPhoneNumber());
		customer.setEmail(request.getEmail());
		customer.setAddress(request.getAddress());
	}
}
