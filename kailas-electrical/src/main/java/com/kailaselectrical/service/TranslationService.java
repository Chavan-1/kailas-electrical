package com.kailaselectrical.service;

import java.util.List;

import com.kailaselectrical.dto.request.CreateServiceTranslationRequest;
import com.kailaselectrical.dto.request.UpdateServiceTranslationRequest;
import com.kailaselectrical.dto.response.ServiceTranslationResponse;
import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.entity.ElectricalServiceTranslations;
import com.kailaselectrical.enums.Language;

public interface TranslationService {
	
	ElectricalServiceTranslations getTranslation(ElectricalService service);
	
	String getServiceName(ElectricalService service);
	
	String getServiceDescription(ElectricalService service);
	
	List<ServiceTranslationResponse> getTranslations(Long serviceId);
	
	ServiceTranslationResponse addTranslation(Long serviceId, CreateServiceTranslationRequest request);
	
	ServiceTranslationResponse updateTranslation(Long serviceId, Language language, UpdateServiceTranslationRequest request);
	
}
