package com.kailaselectrical.service.impl;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.entity.ElectricalServiceTranslations;
import com.kailaselectrical.enums.Language;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.respository.ElectricalServiceTranslationRepository;
import com.kailaselectrical.service.ElectricalServiceService;
import com.kailaselectrical.service.TranslationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService{

	private final ElectricalServiceTranslationRepository translationRepository;
	
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
		
		Language language = getCurrentLanguage();
		
		return translationRepository
				.findByElectricalServiceIdAndLanguageCode(
						service.getId(), 
						language)
				.orElseGet(() -> 
						translationRepository
								.findByElectricalServiceIdAndLanguageCode(
										service.getId(), 
										Language.EN)
					    .orElseThrow(() -> 
					    	new ResourceNotFoundException("Translation not found for service id : " + service.getId()))
				);
				
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
