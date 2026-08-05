package com.kailaselectrical.dto.request;

import com.kailaselectrical.enums.Language;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateServiceTranslationRequest {
	
	private Language languageCode;
	
	private String serviceName;
	
	private String description;
}
