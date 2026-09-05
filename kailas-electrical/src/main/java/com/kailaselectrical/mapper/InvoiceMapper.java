package com.kailaselectrical.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kailaselectrical.dto.response.InvoiceItemResponse;
import com.kailaselectrical.dto.response.InvoiceResponse;
import com.kailaselectrical.entity.Invoice;
import com.kailaselectrical.service.TranslationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {
	
	private final TranslationService translationService;
	
	public InvoiceResponse toResponse(Invoice invoice) {
		
		List<InvoiceItemResponse> items =  invoice.getInvoiceItems()
				.stream()
				.map(item -> InvoiceItemResponse.builder()
						.serviceName(translationService.getServiceName(item.getElectricalService()))
						.quantity(item.getQuantity())
						.unitPrice(item.getUnitPrice())
						.lineTotal(item.getLineTotal())
						.build())
				.toList();
		
		return InvoiceResponse.builder()
				.id(invoice.getId())
				.invoiceNumber(invoice.getInvoiceNumber())
				.bookingNumber(invoice.getBooking().getBookingNumber())
				.customerName(invoice.getBooking().getCustomer().getFullName())
				.phoneNumber(invoice.getBooking().getCustomer().getPhoneNumber())
		        .email(invoice.getBooking().getCustomer().getEmail())
				.invoiceDate(invoice.getInvoiceDate())
				.items(items)
				.subtotal(invoice.getSubtotal())
				.gstPercentage(invoice.getGstPercentage())
				.gstAmount(invoice.getGstAmount())
				.discount(invoice.getDiscount())
				.totalAmount(invoice.getTotalAmount())
				.paymentStatus(invoice.getPaymentStatus())
				.notes(invoice.getNotes())
				.build();
	}
}
