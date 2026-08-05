package com.kailaselectrical.dto.request;

import com.kailaselectrical.enums.Language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateServiceTranslationRequest {
	
	private Language languageCode;
	
	private String serviceName;
	
	private String description;
}
