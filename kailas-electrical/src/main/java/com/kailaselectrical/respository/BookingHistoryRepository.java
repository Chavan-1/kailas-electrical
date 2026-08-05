package com.kailaselectrical.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.BookingHistory;

public interface BookingHistoryRepository  extends JpaRepository<BookingHistory, Long>{
	
	List<BookingHistory> findByBookingOrderByChangedAtDesc(Booking booking);
}