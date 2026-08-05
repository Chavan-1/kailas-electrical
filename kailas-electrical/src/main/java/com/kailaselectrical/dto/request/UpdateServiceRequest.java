package com.kailaselectrical.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class UpdateServiceRequest {
	
	@NotNull(message = "Base price is required")
	@Positive(message = "Base price must be greater than zero")
	private BigDecimal basePrice;
	
	@NotNull(message = "Estimated duration is required")
	@Positive(message = "Duration must be greater than zero")
	private Integer estimatedDuration;
	
	@NotNull(message = "Active status is required")
	private Boolean active;
	
	@Valid
	private List<UpdateServiceTranslationRequest> translations;
}
