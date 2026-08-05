package com.kailaselectrical.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kailaselectrical.common.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
		
		ApiResponse<Void> response = ApiResponse.<Void>builder()
												.success(false)
												.message(ex.getMessage())
												.build();
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
		
		Map<String, String> errors = new HashMap<>();
		
		ex.getBindingResult()
		  .getFieldErrors()
		  .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		
		ApiResponse<Map<String, String>> response = ApiResponse.<Map<String,String>>builder()
															   .success(false)
															   .message("Validation Failed")
															   .data(errors)
															   .build();
		
		return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		
		ApiResponse<Void> response = ApiResponse.<Void>builder()
												.success(false)
												.message(ex.getMessage())
												.build();
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
		
		ApiResponse<Void> response = ApiResponse.<Void>builder()
												.success(false)
												.message(ex.getMessage())
												.build();
		
		return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(BookingConflictException.class)
	public ResponseEntity<ApiResponse<Void>> handleBookingConflictException(BookingConflictException ex) {
		
		ApiResponse<Void> response = ApiResponse.<Void>builder()
												.success(false)
												.message(ex.getMessage())
												.build();
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	@ExceptionHandler(InvalidBookingStatusException.class)
	public ResponseEntity<ApiResponse<Void>> handleInvalidBookingStatusException(InvalidBookingStatusException ex) {
		
		ApiResponse<Void> response = ApiResponse.<Void>builder()
												.success(false)
												.message(ex.getMessage())
												.build();
		
		return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiResponse<Object>> handleIllegalStateException(
	        IllegalStateException ex) {

	    ApiResponse<Object> response = ApiResponse.builder()
	            .success(false)
	            .message(ex.getMessage())
	            .data(null)
	            .timestamp(LocalDateTime.now())
	            .build();

	    return ResponseEntity.badRequest().body(response);
	}
}
