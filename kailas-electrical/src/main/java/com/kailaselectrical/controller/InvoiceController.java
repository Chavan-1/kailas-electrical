package com.kailaselectrical.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.CreateInvoiceRequest;
import com.kailaselectrical.dto.request.InvoiceSearchCriteria;
import com.kailaselectrical.dto.request.UpdatePaymentStatusRequest;
import com.kailaselectrical.dto.response.InvoiceResponse;
import com.kailaselectrical.service.InvoiceService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Invoice", description = "Invoice Management APIs")
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
	
	private final InvoiceService invoiceService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<InvoiceResponse>> generateInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
	
		InvoiceResponse response = invoiceService.generateInvoice(request);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.<InvoiceResponse>builder()
						.success(true)
						.message("Invoice generated successfully")
						.data(response)
						.build());
	}
	
	@GetMapping("/booking/{bookingId}")
	public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceByBookingId(@PathVariable Long bookingId) {
		
		return ResponseEntity.ok(
				ApiResponse.<InvoiceResponse>builder()
						.success(true)
						.message("Invoice fetched successfully")
						.data(invoiceService.getInvoiceByBookingId(bookingId))
						.build());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceById(@PathVariable Long id) {
		
		return ResponseEntity.ok(
				ApiResponse.<InvoiceResponse>builder()
						.success(true)
						.message("Invoice fetched successfully")
						.data(invoiceService.getInvoiceById(id))
						.build());
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> getAllInvoice(InvoiceSearchCriteria criteria) {
		
		return ResponseEntity.ok(
				ApiResponse.<PageResponse<InvoiceResponse>>builder()
						.success(true)
						.message("Invoice fetched successfully")
						.data(invoiceService.getAllInvoices(criteria))
						.build());
	}
	
	@PutMapping("/{id}/payment-status")
	public ResponseEntity<ApiResponse<InvoiceResponse>> updatePaymentStatus(
			@PathVariable Long id,
			@Valid @RequestBody UpdatePaymentStatusRequest request) {
		
		return ResponseEntity.ok(
				ApiResponse.<InvoiceResponse>builder()
						.success(true)
						.message("Payment status updated successfully")
						.data(invoiceService.updatePaymentStatus(id, request))
						.build());
	}
	
	@GetMapping("/{id}/download")
	public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {
		
		byte[] pdf = invoiceService.downloadInvoice(id);
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_PDF);
		
		headers.setContentDisposition(
				ContentDisposition.attachment()
					.filename("Invoice-" + id + ".pdf")
					.build());
		
		return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
	}
}
