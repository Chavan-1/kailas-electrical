package com.kailaselectrical.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kailaselectrical.entity.ElectricalServiceTranslations;
import com.kailaselectrical.enums.Language;

public interface ElectricalServiceTranslationRepository 
	extends JpaRepository<ElectricalServiceTranslations, Long>{
	
	Optional<ElectricalServiceTranslations> findByElectricalServiceIdAndLanguageCode(
	        Long serviceId,
	        Language languageCode);
	
	Optional<ElectricalServiceTranslations> findByServiceNameIgnoreCase(String serviceName);
	
	boolean existsByServiceNameIgnoreCase(String serviceName);
}
