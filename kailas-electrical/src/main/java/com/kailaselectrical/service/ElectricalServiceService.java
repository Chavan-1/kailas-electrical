package com.kailaselectrical.service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.CreateServiceRequest;
import com.kailaselectrical.dto.request.SearchCriteria;
import com.kailaselectrical.dto.request.UpdateServiceRequest;
import com.kailaselectrical.dto.response.ServiceResponse;

public interface ElectricalServiceService {
	
	ServiceResponse createService(CreateServiceRequest request);
	
	PageResponse<ServiceResponse> getAllServices(SearchCriteria criteria);
	
	ServiceResponse getServiceById(Long id);
	
	ServiceResponse updateService(Long id, UpdateServiceRequest request);
	
	void deleteService(Long id);
}
