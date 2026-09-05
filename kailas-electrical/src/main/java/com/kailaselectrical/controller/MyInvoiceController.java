package com.kailaselectrical.controller;

import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kailaselectrical.common.ApiResponse;
import com.kailaselectrical.dto.response.InvoiceResponse;
import com.kailaselectrical.service.MyInvoiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/my/invoices")
@RequiredArgsConstructor
public class MyInvoiceController {
	
	private final MyInvoiceService myInvoiceService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getMyInvoices() {
		
		return ResponseEntity.ok(
				ApiResponse.<List<InvoiceResponse>>builder()
							.success(true)
							.message("Invoice fetched successfully.")
							.data(myInvoiceService.getMyInvoices())
							.build()
				);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<InvoiceResponse>> getMyInvoice(
			@PathVariable Long id) {
		
		return ResponseEntity.ok(
				ApiResponse.<InvoiceResponse>builder()
							.success(true)
							.message("Invoice fetched successfully.")
							.data(myInvoiceService.getMyInvoice(id))
							.build()
				);
	}
	
	@GetMapping("/{id}/download")
	public ResponseEntity<byte[]> downloadMyInvoice(
			@PathVariable Long id) {
		
		byte[] pdf = myInvoiceService.downloadMyInvoice(id);
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_PDF);
		
		headers.setContentDisposition(
				ContentDisposition.attachment()
								  .filename("Invoice-" + id + ".pdf")
								  .build());
		
		return new ResponseEntity<>(
				pdf,
				headers,
				HttpStatus.OK);
	}
}
