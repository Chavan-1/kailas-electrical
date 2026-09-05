package com.kailaselectrical.respository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.Invoice;
import com.kailaselectrical.enums.PaymentStatus;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice>{

	Optional<Invoice> findByBookingId(Long bookingId);
	
	boolean existsByBookingId(Long bookingId);
	
	Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
	
	long countByPaymentStatus(PaymentStatus status);
	
	@Query("""
			SELECT COALESCE(SUM(i.totalAmount), 0)
			FROM Invoice i
			WHERE i.paymentStatus= :paymentStatus
			""")
	BigDecimal getTotalRevenue(@Param("paymentStatus") PaymentStatus paymentStatus);
	
	@Query("""
			SELECT COALESCE(SUM(i.totalAmount), 0)
			FROM Invoice i
			WHERE i.paymentStatus= :paymentStatus
			AND i.invoiceDate BETWEEN :startDate AND :endDate
			""")
	BigDecimal getRevenueBetween(
			@Param("paymentStatus") PaymentStatus paymentStatus,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
	
	@Query("""
			SELECT COALESCE(SUM(i.totalAmount), 0)
			FROM Invoice i
			WHERE i.booking.customer.id = :customerId
			and (:paymentStatus IS NULL OR i.paymentStatus= :paymentStatus)
			""")
	BigDecimal getTotalSpent(
			Long customerId,
			PaymentStatus paymentStatus);
	
	List<Invoice> findByBookingIn(List<Booking> bookings);
	
	List<Invoice> findByBookingCustomer(Customer customer);
	
	
}
	
