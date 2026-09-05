package com.kailaselectrical.service;

import java.util.List;

import com.kailaselectrical.dto.response.InvoiceResponse;

public interface MyInvoiceService {

	List<InvoiceResponse> getMyInvoices();
	
	InvoiceResponse getMyInvoice(Long invoiceId);
	
	byte[] downloadMyInvoice(Long invoiceId);
}
