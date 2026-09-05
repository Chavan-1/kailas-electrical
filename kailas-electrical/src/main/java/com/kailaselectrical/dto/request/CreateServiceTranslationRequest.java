package com.kailaselectrical.dto.request;

import com.kailaselectrical.enums.Language;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateServiceTranslationRequest {
	
	@NotNull(message = "Language is required")
    private Language languageCode;

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}
