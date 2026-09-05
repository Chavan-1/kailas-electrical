package com.kailaselectrical.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.kailaselectrical.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
	
	private Long id;
	
	private String invoiceNumber;
	
	private String bookingNumber;
	
	private String customerName;
	
	private String phoneNumber;

    private String email;
	
	private LocalDate invoiceDate;
	
	private List<InvoiceItemResponse> items;
	
	private BigDecimal subtotal;
	
	private BigDecimal gstPercentage;
	
	private BigDecimal gstAmount;
	
	private BigDecimal discount;
	
	private BigDecimal totalAmount;
	
	private PaymentStatus paymentStatus;
	
	private String notes;
}
