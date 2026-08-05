package com.kailaselectrical.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.BookingSearchCriteria;
import com.kailaselectrical.dto.request.CreateBookingRequest;
import com.kailaselectrical.dto.request.UpdateBookingRequest;
import com.kailaselectrical.dto.request.UpdateBookingStatusRequest;
import com.kailaselectrical.dto.response.BookingResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.BookingServiceItem;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.enums.BookingStatus;
import com.kailaselectrical.exception.BookingConflictException;
import com.kailaselectrical.exception.InvalidBookingStatusException;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.mapper.BookingMapper;
import com.kailaselectrical.respository.BookingRepository;
import com.kailaselectrical.respository.CustomerRepository;
import com.kailaselectrical.respository.ServiceRespository;
import com.kailaselectrical.service.BookingHistoryService;
import com.kailaselectrical.service.BookingService;
import com.kailaselectrical.service.EmailService;
import com.kailaselectrical.service.TranslationService;
import com.kailaselectrical.specification.BookingSpecification;
import com.kailaselectrical.util.BookingConstants;
import com.kailaselectrical.util.BookingNumberGenerator;
import com.kailaselectrical.util.BookingStatusValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

	private final BookingRepository bookingRepository;
	
	private final CustomerRepository customerRepository;
	
	private final ServiceRespository serviceRespository;
	
	private final BookingMapper mapper;
	
	private final TranslationService translationService;
	
	private final BookingHistoryService bookingHistoryService;
	
	private final EmailService emailService;
	
	@Override
	public BookingResponse createBooking(CreateBookingRequest request) {
		
		Customer customer = customerRepository.findById(request.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		
		List<ElectricalService> services = serviceRespository.findAllById(request.getServiceIds());
		
		if (services.size() != request.getServiceIds().size()) {
			throw new ResourceNotFoundException("One or more services not found");
		}
		
		if (request.getServiceIds() == null || request.getServiceIds().isEmpty()) {
			throw new IllegalArgumentException("At least one service must be selected.");
		}
		
		boolean alreadyBooked =
		        bookingRepository.existsByBookingDateAndBookingTimeAndStatusNot(
		                request.getBookingDate(),
		                request.getBookingTime(),
		                BookingStatus.CANCELLED);

		if (alreadyBooked) {
		    throw new BookingConflictException(
		            "Selected date and time slot is already booked.");
		}
		
		Booking booking = mapper.toEntity(request);
		
		booking.setCustomer(customer);
		
		booking.setStatus(BookingStatus.PENDING);
		
		booking.setBookingNumber(BookingNumberGenerator.generate(bookingRepository.count() + 1));
		
		BigDecimal total = BigDecimal.ZERO;
		
		for (ElectricalService electricalService : services) {
			
			if (!electricalService.getActive()) {
				throw new IllegalArgumentException(translationService.getServiceName(electricalService) + " is inactive.");
			}
			
			BookingServiceItem item = new BookingServiceItem();
			
			item.setBooking(booking);
			item.setElectricalService(electricalService);
			item.setPriceAtBookingTime(electricalService.getBasePrice());
			
			booking.getServiceItems().add(item);
			
			total = total.add(electricalService.getBasePrice());
		}
		
		booking.setEstimatedPrice(total);
		
		Booking saved = bookingRepository.save(booking);
		
		emailService.sendBookingConfirmation(saved.getId());
		
		return mapper.toResponse(saved);
	}

	@Override
	public PageResponse<BookingResponse> getAllBookings(BookingSearchCriteria criteria) {
		
		String sortBy = criteria.getSortBy();
		
		if (sortBy == null || sortBy.isBlank()) {
			sortBy = BookingConstants.DEFAULT_SORT_FIELD;
		}
		
		if (!BookingConstants.ALLOWED_SORT_FIELDS.contains(sortBy)) {
			throw new IllegalArgumentException("Invalid sort field : " + sortBy);
		}
		
		Sort sort = criteria.getDirection().equalsIgnoreCase("desc")
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
		
		Specification<Booking> specification = 
				Specification.where(
						BookingSpecification.hasKeyword(criteria.getKeyword()))
				.and(
						BookingSpecification.hasStatus(criteria.getStatus()))
				.and(
						BookingSpecification.hasBookingDate(criteria.getBookingDate()));
		
		Page<Booking> bookingPage = bookingRepository.findAll(specification, pageable);
		
		List<BookingResponse> responses = bookingPage.getContent()
													 .stream()
													 .map(mapper::toResponse)
													 .toList();
		
		return PageResponse.<BookingResponse>builder()
				.content(responses)
				.pageNumber(bookingPage.getNumber())
				.pageSize(bookingPage.getSize())
				.totalElements(bookingPage.getTotalElements())
				.totalPage(bookingPage.getTotalPages())
				.last(bookingPage.isLast())
				.build();
	}

	@Override
	public BookingResponse getBookingById(Long id) {
		
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with id : " + id));
		
		return mapper.toResponse(booking);
	}

	@Override
	public BookingResponse updateBooking(Long id, UpdateBookingRequest request) {
		
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found with id : " + id));
		
		mapper.updateEntity(booking, request);
		
		Booking updated = bookingRepository.save(booking);
		
		return mapper.toResponse(updated);
	}

	@Override
	public BookingResponse updateBookingStatus(Long id, UpdateBookingStatusRequest request) {
		
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
		
		BookingStatus currentStatus = booking.getStatus();
		
		BookingStatus newStatus = request.getStatus();
		
		if (!BookingStatusValidator.isValidTransition(currentStatus, newStatus)) {
			
			throw new InvalidBookingStatusException(
					"Cannot change booking status from " + currentStatus + " to " + newStatus);
		}
		
		booking.setStatus(newStatus);
		
		Booking updated = bookingRepository.save(booking);
		
		bookingHistoryService.saveHistory(updated, currentStatus, newStatus, "Booking status updated");
		
		return mapper.toResponse(updated);
	}

	@Override
	public void cancelBooking(Long id) {
		
		Booking booking = bookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
		
		BookingStatus currentStatus = booking.getStatus();
		
		if (!BookingStatusValidator.isValidTransition(currentStatus, BookingStatus.CANCELLED)) {
			
			throw new InvalidBookingStatusException(
					"Booking cannot be cancelled because it is  " + currentStatus);
		}
		
		booking.setStatus(BookingStatus.CANCELLED);
		
		bookingRepository.save(booking);
		
	}
}
