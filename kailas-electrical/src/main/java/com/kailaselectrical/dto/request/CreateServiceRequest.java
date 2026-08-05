package com.kailaselectrical.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateServiceRequest {
	
	@NotNull(message = "Base price is required")
	@Positive(message = "Base price must be greater than zero")
	private BigDecimal basePrice;
	
	@NotNull(message = "Estimated duration is required")
	@Positive(message = "Duration must be greater than zero")
	private Integer estimatedDuration;
	
	@Valid
	@NotEmpty(message = "Translations are required")
	private List<CreateServiceTranslationRequest> translations;
	
}
