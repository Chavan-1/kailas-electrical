package com.kailaselectrical.mapper;

import org.springframework.stereotype.Component;

import com.kailaselectrical.dto.response.BookingHistoryResponse;
import com.kailaselectrical.entity.BookingHistory;

@Component
public class BookingHistoryMapper {
	
	public BookingHistoryResponse toResponse(BookingHistory history) {
		
		return BookingHistoryResponse.builder()
				.oldStatus(history.getOldStatus())
				.newStatus(history.getNewStatus())
				.remarks(history.getRemarks())
				.changedBy(history.getChangedBy())
				.changedAt(history.getChangedAt())
				.build();
	}
}
