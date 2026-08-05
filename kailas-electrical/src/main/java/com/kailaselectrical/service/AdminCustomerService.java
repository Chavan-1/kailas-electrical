package com.kailaselectrical.service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.CustomerSearchCriteria;
import com.kailaselectrical.dto.response.AdminCustomerResponse;
import com.kailaselectrical.dto.response.CustomerSummaryResponse;

public interface AdminCustomerService {

	PageResponse<AdminCustomerResponse> getAllCustomers(CustomerSearchCriteria criteria);
	
	AdminCustomerResponse getCustomer(Long id);
	
	CustomerSummaryResponse getSummary(Long customerId);
}
