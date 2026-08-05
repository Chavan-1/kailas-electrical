package com.kailaselectrical.mapper;

import org.springframework.stereotype.Component;

import com.kailaselectrical.dto.request.CreateServiceRequest;
import com.kailaselectrical.dto.request.UpdateServiceRequest;
import com.kailaselectrical.dto.response.ServiceResponse;
import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.service.TranslationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ElectricalServiceMapper {
	
	private final TranslationService translationService;
	
	public ElectricalService toEntity(CreateServiceRequest request) {
		
		ElectricalService service = new ElectricalService();
		
		service.setBasePrice(request.getBasePrice());
		service.setEstimatedDuration(request.getEstimatedDuration());
		service.setActive(true);
		
		return service;
	}
	
	public ServiceResponse toResponse(ElectricalService service) {
		
		return ServiceResponse.builder()
				.id(service.getId())
				.serviceName(translationService.getServiceName(service))
				.description(translationService.getServiceDescription(service))
				.basePrice(service.getBasePrice())
				.estimatedDuration(service.getEstimatedDuration())
				.active(service.getActive())
				.build();
	}
	
	public void updateEntity(ElectricalService service, UpdateServiceRequest request) {
		
		service.setBasePrice(request.getBasePrice());
		service.setEstimatedDuration(request.getEstimatedDuration());
		service.setActive(request.getActive());
	}
}
