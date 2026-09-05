package com.kailaselectrical.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kailaselectrical.entity.ElectricalServiceTranslations;
import com.kailaselectrical.enums.Language;

public interface ElectricalServiceTranslationRepository 
	extends JpaRepository<ElectricalServiceTranslations, Long>{
	
	Optional<ElectricalServiceTranslations> findByElectricalServiceIdAndLanguageCode(
	        Long serviceId,
	        Language languageCode);
	
	List<ElectricalServiceTranslations> findByElectricalServiceId(Long serviceId);
	
	boolean existsByElectricalServiceIdAndLanguageCode(Long serviceId, Language languageCode);
	
	Optional<ElectricalServiceTranslations> findByElectricalServiceIdAndLanguageCodeAndId(
	        Long serviceId,
	        Language languageCode,
	        Long id);
	
	Optional<ElectricalServiceTranslations> findByServiceNameIgnoreCase(String serviceName);
	
	boolean existsByServiceNameIgnoreCase(String serviceName);
}
