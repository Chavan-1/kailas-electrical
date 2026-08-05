package com.kailaselectrical.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailsResponse {

	private CustomerResponse customer;
	
	private List<BookingResponse> bookings;
	
	private List<InvoiceResponse> invoices;
	
	private Integer totalBookings;
	
	private Integer completedBookings;
	
	private Integer cancelledBookings;
	
	private BigDecimal totalSpent;
	
	private BigDecimal pendingAmount;
	
	private LocalDate lastBookingDate;	
}
