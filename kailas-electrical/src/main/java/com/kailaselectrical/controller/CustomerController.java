package com.kailaselectrical.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.CreateCustomerRequest;
import com.kailaselectrical.dto.request.SearchCriteria;
import com.kailaselectrical.dto.request.UpdateCustomerRequest;
import com.kailaselectrical.dto.request.UpdateCustomerStatusRequest;
import com.kailaselectrical.dto.response.CustomerDetailsResponse;
import com.kailaselectrical.dto.response.CustomerResponse;
import com.kailaselectrical.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Customer", description = "Customer Management APIs")
@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

	private final CustomerService customerService;
	
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@Operation(summary = "Create Customer")
	@PostMapping
	public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
		
		CustomerResponse response = customerService.createCustomer(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.<CustomerResponse>builder()
						.success(true)
						.message("Customer created successfully")
						.data(response)
						.build());
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getAllCustomers(SearchCriteria criteria) {
		
		PageResponse<CustomerResponse> response = customerService.getAllCustomers(criteria);
		
		return ResponseEntity.ok(
				ApiResponse.<PageResponse<CustomerResponse>>builder()
					.success(true)
					.message("Customer created successfully")
					.data(response)
					.build());
	}
	
	@Operation(summary = "Get Customer By Id")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
		
		CustomerResponse response = customerService.getCustomerById(id);
		
		return ResponseEntity.ok(
				ApiResponse.<CustomerResponse>builder()
						.success(true)
						.message("Customer fetched successfully")
						.data(response)
						.build());
	}
	
	@Operation(summary = "Update Customer")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
			@PathVariable Long id,
			@Valid @RequestBody UpdateCustomerRequest request) {
		
		CustomerResponse response = customerService.updateCustomer(id, request);
		
		return ResponseEntity.ok(
				ApiResponse.<CustomerResponse>builder()
				.success(true)
				.message("Customer updated successfully")
				.data(response)
				.build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
		
		customerService.deleteCustomer(id);
		
		return ResponseEntity.ok(
				ApiResponse.<Void>builder()
				.success(true)
				.message("Customer deleted successfully")
				.build());
	}
	
	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<CustomerResponse>> updateStatus(
			@PathVariable Long id, 
			@Valid @RequestBody UpdateCustomerStatusRequest request) {
		
		CustomerResponse response = customerService.updateStatus(id, request);
		
		return ResponseEntity.ok(
				ApiResponse.<CustomerResponse>builder()
						.success(true)
						.message("Customer status updated successfully")
						.data(response)
						.build());
	}
	
	@GetMapping("/{id}/details")
	public ResponseEntity<ApiResponse<CustomerDetailsResponse>> getCustomerDetails(@PathVariable Long id) {
		
		CustomerDetailsResponse response = customerService.getCustomerDetails(id);
		
		return ResponseEntity.ok(
				ApiResponse.<CustomerDetailsResponse>builder()
					.success(true)
					.message("Customer details fetched successfully")
					.data(response)
					.build());
	}
}
