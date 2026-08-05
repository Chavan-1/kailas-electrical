package com.kailaselectrical.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.CreateServiceRequest;
import com.kailaselectrical.dto.request.SearchCriteria;
import com.kailaselectrical.dto.request.UpdateServiceRequest;
import com.kailaselectrical.dto.response.ServiceResponse;
import com.kailaselectrical.service.ElectricalServiceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/services")
@Validated
public class ServiceController {
	
	private final ElectricalServiceService serviceService;
	
	public ServiceController(ElectricalServiceService serviceService) {
		this.serviceService = serviceService;
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<ServiceResponse>> createService(@Valid @RequestBody CreateServiceRequest request) {
		
		ServiceResponse response = serviceService.createService(request);
		
		ApiResponse<ServiceResponse> apiResponse = ApiResponse.<ServiceResponse>builder()
															  .success(true)
															  .message("Service created successfully")
															  .data(response)
															  .build();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<ServiceResponse>>> getAllServices(SearchCriteria criteria) {
		
		PageResponse<ServiceResponse> response = serviceService.getAllServices(criteria);
		
		return ResponseEntity.ok(
				ApiResponse.<PageResponse<ServiceResponse>>builder()
					       .success(true)
					       .message("Services fetched successfully")
					       .data(response)
					       .build());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(@PathVariable Long id) {
		
		ServiceResponse response = serviceService.getServiceById(id);
		
		return ResponseEntity.ok(
				ApiResponse.<ServiceResponse>builder()
					       .success(true)
					       .message("Services fetched successfully")
					       .data(response)
					       .build());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
			@PathVariable Long id,
			@Valid @RequestBody UpdateServiceRequest request) {
		
		ServiceResponse response = serviceService.updateService(id, request);
		
		return ResponseEntity.ok(
				ApiResponse.<ServiceResponse>builder()
				.success(true)
				.message("Service updated successfully")
				.data(response)
				.build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
		
		serviceService.deleteService(id);
		
		return ResponseEntity.ok(
				ApiResponse.<Void>builder()
				.success(true)
				.message("Service deleted successfully")
				.build());
	}
}
