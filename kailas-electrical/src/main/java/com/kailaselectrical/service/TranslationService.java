package com.kailaselectrical.service;

import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.entity.ElectricalServiceTranslations;

public interface TranslationService {
	
	ElectricalServiceTranslations getTranslation(ElectricalService service);
	
	String getServiceName(ElectricalService service);
	
	String getServiceDescription(ElectricalService service);
}
