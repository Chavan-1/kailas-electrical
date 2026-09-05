package com.kailaselectrical.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	
	@NotBlank(message = "Service name is required")
    private String serviceName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}
