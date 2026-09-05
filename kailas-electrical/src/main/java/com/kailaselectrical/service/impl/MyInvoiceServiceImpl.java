package com.kailaselectrical.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kailaselectrical.dto.response.InvoiceResponse;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.Invoice;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.mapper.InvoiceMapper;
import com.kailaselectrical.pdf.InvoicePdfGenerator;
import com.kailaselectrical.respository.InvoiceRepository;
import com.kailaselectrical.security.AuthenticatedUserService;
import com.kailaselectrical.service.MyInvoiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyInvoiceServiceImpl implements MyInvoiceService{
	
	private final InvoiceRepository invoiceRepository;
	private final InvoiceMapper mapper;
	private final InvoicePdfGenerator invoicePdfGenerator;
	private final AuthenticatedUserService authenticatedUserService;

	@Override
	public List<InvoiceResponse> getMyInvoices() {
		
		Customer customer = authenticatedUserService.getLoggedInCustomer();
		
		return invoiceRepository.findByBookingCustomer(customer)
				.stream()
				.map(mapper::toResponse)
				.toList();
	}

	@Override
	public InvoiceResponse getMyInvoice(Long invoiceId) {
		
		Customer customer = authenticatedUserService.getLoggedInCustomer();
		
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
		
		if (!invoice.getBooking().getCustomer().getId()
				.equals(customer.getId())) {
			
			throw new ResourceNotFoundException("Invoice not found");
		}
		
		return mapper.toResponse(invoice);
	}

	@Override
	public byte[] downloadMyInvoice(Long invoiceId) {
		
		Customer customer = authenticatedUserService.getLoggedInCustomer();
		
		Invoice invoice = invoiceRepository.findById(invoiceId)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
		
		if (!invoice.getBooking().getCustomer().getId()
				.equals(customer.getId())) {
			
			throw new ResourceNotFoundException("Invoice not found");
		}
		
		return invoicePdfGenerator.generate(invoice);
	}

}
