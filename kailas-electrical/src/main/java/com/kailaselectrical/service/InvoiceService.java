package com.kailaselectrical.service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.CreateInvoiceRequest;
import com.kailaselectrical.dto.request.InvoiceSearchCriteria;
import com.kailaselectrical.dto.request.UpdatePaymentStatusRequest;
import com.kailaselectrical.dto.response.InvoiceResponse;

public interface InvoiceService {
	
	InvoiceResponse generateInvoice(CreateInvoiceRequest request);
	
	InvoiceResponse getInvoiceById(Long id);
	
	PageResponse<InvoiceResponse> getAllInvoices(InvoiceSearchCriteria criteria);
	
	InvoiceResponse updatePaymentStatus(Long id, UpdatePaymentStatusRequest request);
	
	byte[] downloadInvoice(Long id);
}
