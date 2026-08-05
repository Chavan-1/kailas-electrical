package com.kailaselectrical.service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.CreateCustomerRequest;
import com.kailaselectrical.dto.request.CustomerSearchCriteria;
import com.kailaselectrical.dto.request.SearchCriteria;
import com.kailaselectrical.dto.request.UpdateCustomerRequest;
import com.kailaselectrical.dto.request.UpdateCustomerStatusRequest;
import com.kailaselectrical.dto.response.CustomerDetailsResponse;
import com.kailaselectrical.dto.response.CustomerResponse;

public interface CustomerService {
	
	CustomerResponse createCustomer(CreateCustomerRequest request);
	
	PageResponse<CustomerResponse> getAllCustomers(SearchCriteria criteria);
	
	CustomerResponse getCustomerById(Long id);
	
	CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);
	
	void deleteCustomer(Long id);
	
	CustomerResponse updateStatus(Long id, UpdateCustomerStatusRequest request);
	
	CustomerDetailsResponse getCustomerDetails(Long id);
	
}
