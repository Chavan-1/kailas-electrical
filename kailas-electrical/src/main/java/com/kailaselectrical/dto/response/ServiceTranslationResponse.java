package com.kailaselectrical.dto.response;

import com.kailaselectrical.enums.Language;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceTranslationResponse {

    private Long id;

    private Language languageCode;

    private String serviceName;

    private String description;
}