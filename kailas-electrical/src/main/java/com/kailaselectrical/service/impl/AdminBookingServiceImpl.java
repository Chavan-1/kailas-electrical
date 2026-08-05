package com.kailaselectrical.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.AdminBookingSearchCriteria;
import com.kailaselectrical.dto.request.UpdateBookingStatusRequest;
import com.kailaselectrical.dto.response.AdminBookingResponse;
import com.kailaselectrical.dto.response.BookingResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.mapper.AdminBookingMapper;
import com.kailaselectrical.respository.BookingRepository;
import com.kailaselectrical.service.AdminBookingService;
import com.kailaselectrical.service.BookingService;
import com.kailaselectrical.specification.BookingSpecification;
import com.kailaselectrical.util.BookingConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminBookingServiceImpl implements AdminBookingService{
	
	private final BookingRepository bookingRepository;

	private final AdminBookingMapper mapper;
	
	private final BookingService bookingService;
	
	@Override
	public PageResponse<AdminBookingResponse> getAllBookings(AdminBookingSearchCriteria criteria) {
		
		String sortBy = criteria.getSortBy();
		
		if (sortBy == null || sortBy.isBlank()) {
			
			sortBy = BookingConstants.DEFAULT_SORT_FIELD;
		}
		
		if (!BookingConstants.ALLOWED_SORT_FIELDS.contains(sortBy)) {
			
			throw new IllegalArgumentException("Invalid sort field: "+ sortBy);
		}
		
		Sort sort = criteria.getDirection().equalsIgnoreCase("desc")
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(
				criteria.getPage(), 
				criteria.getSize(),
				sort);
		
		Specification<Booking> specification = Specification
				.where(BookingSpecification.hasKeyword(criteria.getKeyword()))
				.and(BookingSpecification.hasStatus(criteria.getStatus()))
				.and(BookingSpecification.hasBookingDate(criteria.getBookingDate()));
		
		Page<Booking> bookingPage = bookingRepository.findAll(specification, pageable);
		
		List<AdminBookingResponse> responses = bookingPage.getContent()
				.stream()
				.map(mapper::toResponse)
				.toList();
		
		return PageResponse.<AdminBookingResponse>builder()
				.content(responses)
				.pageNumber(bookingPage.getNumber())
				.pageSize(bookingPage.getSize())
				.totalElements(bookingPage.getTotalElements())
				.totalPage(bookingPage.getTotalPages())
				.last(bookingPage.isLast())
				.build();
	}

	@Override
	public AdminBookingResponse getBookingById(Long id) {
		
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with id : " + id));
		
		return mapper.toResponse(booking);
	}
	
	@Override
	public BookingResponse updateStatus(
	        Long id,
	        UpdateBookingStatusRequest request) {

	    return bookingService.updateBookingStatus(id, request);
	}

}
