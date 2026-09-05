package com.kailaselectrical.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.dto.request.CreateServiceTranslationRequest;
import com.kailaselectrical.dto.request.UpdateServiceTranslationRequest;
import com.kailaselectrical.dto.response.ServiceTranslationResponse;
import com.kailaselectrical.enums.Language;
import com.kailaselectrical.service.TranslationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/services/{serviceId}/translations")
@RequiredArgsConstructor
public class ServiceTranslationController {
	
	private final TranslationService translationService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<ServiceTranslationResponse>>> getTranslations(@PathVariable Long serviceId) {
		
		List<ServiceTranslationResponse> translations = translationService.getTranslations(serviceId);
		
		return ResponseEntity.ok(
				ApiResponse.<List<ServiceTranslationResponse>>builder()
				.success(true)
				.message("Translations fetched successfully")
				.data(translations)
				.build()
				);
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<ServiceTranslationResponse>> addTranslation(
			@PathVariable Long serviceId,
			@Valid @RequestBody CreateServiceTranslationRequest request) {
		
		ServiceTranslationResponse response = translationService.addTranslation(serviceId, request);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(
						ApiResponse.<ServiceTranslationResponse>builder()
							.success(true)
							.message("Translation added successfully")
							.data(response)
							.build()
				);
	}
	
	@PutMapping("/{language}")
    public ResponseEntity<
            ApiResponse<ServiceTranslationResponse>
            > updateTranslation(
                    @PathVariable Long serviceId,
                    @PathVariable Language language,
                    @Valid @RequestBody
                    UpdateServiceTranslationRequest request) {

        ServiceTranslationResponse response = translationService.updateTranslation(
                        serviceId,
                        language,
                        request);

        return ResponseEntity.ok(
                ApiResponse.<ServiceTranslationResponse>builder()
                        .success(true)
                        .message("Translation updated successfully")
                        .data(response)
                        .build()
        );
    }
	
}
