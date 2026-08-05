package com.kailaselectrical.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import org.springframework.stereotype.Service;

import com.kailaselectrical.dto.response.DashboardResponse;
import com.kailaselectrical.enums.BookingStatus;
import com.kailaselectrical.enums.PaymentStatus;
import com.kailaselectrical.respository.BookingRepository;
import com.kailaselectrical.respository.CustomerRepository;
import com.kailaselectrical.respository.InvoiceRepository;
import com.kailaselectrical.respository.ServiceRespository;
import com.kailaselectrical.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{

	private final CustomerRepository customerRepository;
	
	private final ServiceRespository serviceRespository;
	
	private final BookingRepository bookingRepository;
	
	private final InvoiceRepository invoiceRepository;
	
	@Override
	public DashboardResponse getDashboard() {
		
		LocalDate today = LocalDate.now();
		
		LocalDate startOfMonth = today.withDayOfMonth(1);
		
		LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
		
		BigDecimal montlyRevenue = 
				invoiceRepository.getRevenueBetween(
						PaymentStatus.PAID,
						startOfMonth,
						endOfMonth);
		
		return DashboardResponse.builder()
				.totalCustomers(customerRepository.count())
				.totalServices(serviceRespository.count())
				.totalBookings(bookingRepository.count())
				.pendingBookings(bookingRepository.countByStatus(BookingStatus.PENDING))
				.completedBookings(bookingRepository.countByStatus(BookingStatus.COMPLETED))
				.cancelledBookings(bookingRepository.countByStatus(BookingStatus.CANCELLED))
				.todayBookings(bookingRepository.countByBookingDate(LocalDate.now()))
				.pendingPayments(invoiceRepository.countByPaymentStatus(PaymentStatus.PENDING))
				.totalRevenue(defaultValue(invoiceRepository.getTotalRevenue(null)))
				.monthlyRevenue(defaultValue(montlyRevenue))
				.build();
	}
	
	private BigDecimal defaultValue(BigDecimal value) {
		
		return value == null ? BigDecimal.ZERO : value;
	}

}
