package com.kailaselectrical.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.kailaselectrical.dto.request.CreateServiceTranslationRequest;
import com.kailaselectrical.dto.request.UpdateServiceTranslationRequest;
import com.kailaselectrical.dto.response.ServiceTranslationResponse;
import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.entity.ElectricalServiceTranslations;
import com.kailaselectrical.enums.Language;
import com.kailaselectrical.exception.ResourceAlreadyExistsException;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.respository.ElectricalServiceTranslationRepository;
import com.kailaselectrical.respository.ServiceRespository;
import com.kailaselectrical.service.TranslationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService{

	private final ElectricalServiceTranslationRepository translationRepository;
	
	private final ServiceRespository serviceRespository;
	
	@Override
	public String getServiceName(ElectricalService service) {
		
		return getTranslation(service).getServiceName();
	}

	@Override
	public String getServiceDescription(ElectricalService service) {
		
		return getTranslation(service).getDescription();
	}

	@Override
	public ElectricalServiceTranslations getTranslation(ElectricalService service) {

	    Language currentLanguage = getCurrentLanguage();

	    Optional<ElectricalServiceTranslations> translation = translationRepository
	                    .findByElectricalServiceIdAndLanguageCode(
	                            service.getId(),
	                            currentLanguage);

	    if (translation.isPresent()) {
	        return translation.get();
	    }

	    if (currentLanguage != Language.EN) {

	        translation = translationRepository
	                        .findByElectricalServiceIdAndLanguageCode(
	                                service.getId(),
	                                Language.EN);

	        if (translation.isPresent()) {
	            return translation.get();
	        }
	    }
	    
	    List<ElectricalServiceTranslations> translations = translationRepository.findByElectricalServiceId(service.getId());

	    if (!translations.isEmpty()) {
	        return translations.get(0);
	    }
	    
	    throw new ResourceNotFoundException(
	            "Translation not found for service id : " + service.getId());
	}

	@Override
	public List<ServiceTranslationResponse> getTranslations(Long serviceId) {
		
		serviceRespository.findById(serviceId)
				.orElseThrow(() -> new ResourceNotFoundException("Service not found with id : " + serviceId));
		
		return translationRepository.findByElectricalServiceId(serviceId)
				.stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	@Transactional
	public ServiceTranslationResponse addTranslation(Long serviceId, CreateServiceTranslationRequest request) {
		
		ElectricalService service = serviceRespository.findById(serviceId)
				.orElseThrow(() -> new ResourceNotFoundException("Service not found with id : " + serviceId));
		
		if (translationRepository.existsByElectricalServiceIdAndLanguageCode(serviceId, request.getLanguageCode())) {
			
			throw new ResourceAlreadyExistsException("Translation already exists for language : " + request.getLanguageCode());
		}
		
		ElectricalServiceTranslations translations = ElectricalServiceTranslations.builder()
						.languageCode(request.getLanguageCode())
						.serviceName(request.getServiceName())
						.description(request.getDescription())
						.electricalService(service)
						.build();
		
		ElectricalServiceTranslations saved = translationRepository.save(translations);
		
		return toResponse(saved);
	}

	@Override
	@Transactional
	public ServiceTranslationResponse updateTranslation(
	        Long serviceId,
	        Language language,
	        UpdateServiceTranslationRequest request) {

	    serviceRespository.findById(serviceId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException(
	                            "Service not found with id : " + serviceId));

	    ElectricalServiceTranslations translation =
	            translationRepository
	                    .findByElectricalServiceIdAndLanguageCode(
	                            serviceId,
	                            language)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Translation not found for service id : "
	                                    + serviceId
	                                    + " and language : "
	                                    + language));

	    translationRepository
	            .findByServiceNameIgnoreCase(request.getServiceName())
	            .filter(existing -> !existing.getId().equals(translation.getId()))
	            .ifPresent(existing -> {
	                throw new ResourceAlreadyExistsException(
	                        "Service already exists : "
	                        + request.getServiceName());
	            });

	    translation.setServiceName(request.getServiceName());
	    translation.setDescription(request.getDescription());

	    ElectricalServiceTranslations updated =
	            translationRepository.save(translation);

	    return toResponse(updated);
	}
	
	private ServiceTranslationResponse toResponse(ElectricalServiceTranslations translation) {

        return ServiceTranslationResponse.builder()
                .id(translation.getId())
                .languageCode(translation.getLanguageCode())
                .serviceName(translation.getServiceName())
                .description(translation.getDescription())
                .build();
    }
	
	private Language getCurrentLanguage() {
		
		String language = LocaleContextHolder
				.getLocale()
				.getLanguage();
		
		return switch (language) {
			case "hi" -> Language.HI;
			case "mr" -> Language.MR;
			default -> Language.EN;
		};
	}

}
