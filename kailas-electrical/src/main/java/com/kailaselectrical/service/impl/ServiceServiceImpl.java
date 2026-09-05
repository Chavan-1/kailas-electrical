package com.kailaselectrical.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.CreateServiceRequest;
import com.kailaselectrical.dto.request.CreateServiceTranslationRequest;
import com.kailaselectrical.dto.request.SearchCriteria;
import com.kailaselectrical.dto.request.UpdateServiceRequest;
import com.kailaselectrical.dto.request.UpdateServiceTranslationRequest;
import com.kailaselectrical.dto.response.ServiceResponse;
import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.entity.ElectricalServiceTranslations;
import com.kailaselectrical.exception.ResourceAlreadyExistsException;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.mapper.ElectricalServiceMapper;
import com.kailaselectrical.respository.ElectricalServiceTranslationRepository;
import com.kailaselectrical.respository.ServiceRespository;
import com.kailaselectrical.service.ElectricalServiceService;
import com.kailaselectrical.specification.ElectricalServiceSpecification;
import com.kailaselectrical.util.ServiceConstants;

import lombok.RequiredArgsConstructor;

@Service
@EnableWebSecurity
@RequiredArgsConstructor
public class ServiceServiceImpl implements ElectricalServiceService{
	
	private final ServiceRespository serviceRespository;
	
	private final ElectricalServiceTranslationRepository translationRepository;
	
	private final ElectricalServiceMapper mapper;
	
	@Override
	public ServiceResponse createService(CreateServiceRequest request) {
		
		List<CreateServiceTranslationRequest> translations = Optional.ofNullable(request.getTranslations()).orElse(Collections.emptyList());
		
		if(translations.size() != 1) {
			throw new IllegalArgumentException("Exactly one translation is required when creating a service");
		}
		
		for (CreateServiceTranslationRequest translation : translations) {
			
			if (translationRepository.existsByServiceNameIgnoreCase(
					translation.getServiceName())) {
				throw new ResourceAlreadyExistsException("Service already exists : " + translation.getServiceName());
			}
		}
		
		ElectricalService service = mapper.toEntity(request);
		
		for (CreateServiceTranslationRequest translationRequest : translations) {
			
			ElectricalServiceTranslations translation = ElectricalServiceTranslations.builder()
					.languageCode(translationRequest.getLanguageCode())
					.serviceName(translationRequest.getServiceName())
					.description(translationRequest.getDescription())
					.electricalService(service)
					.build();
			
			service.getTranslations().add(translation);
		}
		
		ElectricalService saved = serviceRespository.save(service);
		
		return mapper.toResponse(saved);
	}
	
	@Override
	public PageResponse<ServiceResponse> getAllServices(SearchCriteria criteria) {
		
		String sortBy = criteria.getSortBy();
		
		if (sortBy == null || sortBy.isBlank()) {
			sortBy = ServiceConstants.DEFAULT_SORT_FIELD;
		}
		
		if (!ServiceConstants.ALLOWED_SORT_FIELDS.contains(sortBy)) {
			throw new IllegalArgumentException("Invalid sort field: " + sortBy);
		}
		
		Sort sort = criteria.getDirection().equalsIgnoreCase("desc")
		        ? Sort.by(sortBy).descending()
		        : Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
		
		Specification<ElectricalService> specification = 
				Specification
						.where(ElectricalServiceSpecification.hasActive(criteria.getActive()))
						.and(ElectricalServiceSpecification.hasKeyword(criteria.getKeyword()))
						.and(ElectricalServiceSpecification.hasPriceBetween(criteria.getMinPrice(), criteria.getMaxPrice()));
		
		Page<ElectricalService> servicePage = serviceRespository.findAll(specification, pageable);
		
		List<ServiceResponse> responses = 
				servicePage.getContent()
					       .stream()
					       .map(mapper::toResponse)
					       .toList();
				
		return PageResponse.<ServiceResponse>builder()
				.content(responses)
				.pageNumber(servicePage.getNumber())
				.pageSize(servicePage.getSize())
				.totalElements(servicePage.getTotalElements())
				.totalPage(servicePage.getTotalPages())
				.last(servicePage.isLast())
				.build();
	}
	
	@Override
	public ServiceResponse getServiceById(Long id) {
		
		ElectricalService service = serviceRespository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Service not found with id : " + id));
		
		return mapper.toResponse(service);
	}
	
	@Override
	public ServiceResponse updateService(Long id, UpdateServiceRequest request) {
		
		ElectricalService service = serviceRespository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Service not found with id : "+ id));
		
		mapper.updateEntity(service, request);

		ElectricalService updated = serviceRespository.save(service);
		
		return mapper.toResponse(updated);
	}
	
	@Override
	public void deleteService(Long id) {
		
		ElectricalService service = serviceRespository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Service not found with id : " + id));
		
		service.setActive(false);
		
		serviceRespository.save(service);
	}
}
