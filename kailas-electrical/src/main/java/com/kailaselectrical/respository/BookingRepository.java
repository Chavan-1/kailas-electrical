package com.kailaselectrical.respository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.enums.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking>{
	
	Optional<Booking> findByBookingNumber(String bookingNumber);
	
	boolean existsByBookingDateAndBookingTimeAndStatusNot(
			LocalDate bookingDate,
			LocalTime bookingTime,
			BookingStatus status);
	
	long countByStatus(BookingStatus status);
	
	long countByBookingDate(LocalDate date);
	
	List<Booking> findByCustomer(Customer customer);
	
	Optional<Booking> findByIdAndCustomer(Long id, Customer customer);
	
	long countByCustomer(Customer customer);
	
	List<Booking> findByCustomerId(Long customerId);
	
	long countByCustomerAndStatus(
	        Customer customer,
	        BookingStatus status);

	Optional<Booking> findTopByCustomerOrderByBookingDateDesc(Customer customer);
}
