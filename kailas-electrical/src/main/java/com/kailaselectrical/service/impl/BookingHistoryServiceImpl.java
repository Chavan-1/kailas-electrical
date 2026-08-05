package com.kailaselectrical.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kailaselectrical.dto.response.BookingHistoryResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.BookingHistory;
import com.kailaselectrical.enums.BookingStatus;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.mapper.BookingHistoryMapper;
import com.kailaselectrical.respository.BookingHistoryRepository;
import com.kailaselectrical.respository.BookingRepository;
import com.kailaselectrical.service.BookingHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingHistoryServiceImpl implements BookingHistoryService{

	private final BookingHistoryRepository bookingHistoryRepository;
	
	private final BookingRepository bookingRepository;
	
	private final BookingHistoryMapper mapper;
	
	@Override
	public void saveHistory(Booking booking, BookingStatus oldStatus, BookingStatus newStatus, String remarks) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String changedBy = authentication.getName();
		
		BookingHistory history = BookingHistory.builder()
				.booking(booking)
				.oldStatus(oldStatus)
				.newStatus(newStatus)
				.remarks(remarks)
				.changedBy(changedBy)
				.changedAt(LocalDateTime.now())
				.build();
		
		bookingHistoryRepository.save(history);
		
	}

	@Override
	public List<BookingHistoryResponse> getHistory(Long bookingId) {
		
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
		
		return bookingHistoryRepository
				.findByBookingOrderByChangedAtDesc(booking)
				.stream()
				.map(mapper::toResponse)
				.toList();
	}

}
