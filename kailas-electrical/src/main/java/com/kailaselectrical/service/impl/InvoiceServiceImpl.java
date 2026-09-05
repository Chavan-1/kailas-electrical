package com.kailaselectrical.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.kailaselectrical.common.PageResponse;
import com.kailaselectrical.dto.request.CreateInvoiceRequest;
import com.kailaselectrical.dto.request.InvoiceSearchCriteria;
import com.kailaselectrical.dto.request.UpdatePaymentStatusRequest;
import com.kailaselectrical.dto.response.InvoiceResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.BookingServiceItem;
import com.kailaselectrical.entity.Invoice;
import com.kailaselectrical.entity.InvoiceItem;
import com.kailaselectrical.enums.BookingStatus;
import com.kailaselectrical.enums.PaymentStatus;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.mapper.InvoiceMapper;
import com.kailaselectrical.pdf.InvoicePdfGenerator;
import com.kailaselectrical.respository.BookingRepository;
import com.kailaselectrical.respository.InvoiceRepository;
import com.kailaselectrical.service.EmailService;
import com.kailaselectrical.service.InvoiceService;
import com.kailaselectrical.specification.InvoiceSpecification;
import com.kailaselectrical.util.InvoiceConstants;
import com.kailaselectrical.util.InvoiceNumberGenerator;
import com.kailaselectrical.util.PaymentStatusValidator;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService{
	
	private final InvoiceRepository invoiceRepository;
	
	private final BookingRepository bookingRepository;
	
	private final InvoiceMapper mapper;
	
	private final InvoicePdfGenerator invoicePdfGenerator;
	
	private final EmailService emailService;

	@Override
	@Transactional
	public InvoiceResponse generateInvoice(CreateInvoiceRequest request) {
		
		Booking booking = bookingRepository.findById(request.getBookingId())
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
		
		if (booking.getStatus() != BookingStatus.COMPLETED) {
			
			throw new IllegalStateException("Invoice can only be generated for completed bookings.");
		}
		
		if (invoiceRepository.existsByBookingId(booking.getId())) {
			
			throw new IllegalStateException("Invoice already exists for this booking.");
		}
		
		Invoice invoice = new Invoice();
		
		invoice.setBooking(booking);
		invoice.setInvoiceDate(LocalDate.now());
		invoice.setInvoiceNumber(InvoiceNumberGenerator.generate(invoiceRepository.count() + 1));
		invoice.setDiscount(request.getDiscount());
		invoice.setGstPercentage(request.getGstPercentage());
		invoice.setPaymentStatus(PaymentStatus.PENDING);
		invoice.setNotes(request.getNotes());
		
		BigDecimal subtotal = BigDecimal.ZERO;
		
		for (BookingServiceItem bookingItem : booking.getServiceItems()) {
			
			InvoiceItem invoiceItem = new InvoiceItem();
			
			invoiceItem.setInvoice(invoice);
			invoiceItem.setElectricalService(bookingItem.getElectricalService());
			invoiceItem.setQuantity(1);
			invoiceItem.setUnitPrice(bookingItem.getPriceAtBookingTime());
			invoiceItem.setLineTotal(bookingItem.getPriceAtBookingTime());
			
			invoice.getInvoiceItems().add(invoiceItem);
			
			subtotal = subtotal.add(bookingItem.getPriceAtBookingTime());
		}
		
		invoice.setSubtotal(subtotal);
		
		BigDecimal gstAmount = subtotal
				.multiply(request.getGstPercentage())
				.divide(BigDecimal.valueOf(100));
		
		invoice.setGstAmount(gstAmount);
		
		BigDecimal total = subtotal
				.add(gstAmount)
				.subtract(request.getDiscount());
		
		invoice.setTotalAmount(total);
		
		Invoice saved = invoiceRepository.save(invoice);
		
		try {
			
			emailService.sendInvoice(saved.getId());
			
		} catch (Exception e) {
			
			System.out.println("========== INVOICE EMAIL FAILED ==========");
			throw new RuntimeException("Unable to send invoice email", e);
			
		}
		
		return mapper.toResponse(saved);
	}

	@Override
	public InvoiceResponse getInvoiceById(Long id) {
		
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
		
		return mapper.toResponse(invoice);
	}

	@Override
	public PageResponse<InvoiceResponse> getAllInvoices(InvoiceSearchCriteria criteria) {
		
		String sortBy = criteria.getSortBy();
		
		if (sortBy == null || sortBy.isBlank()) {
			sortBy = InvoiceConstants.DEFAULT_SORT_FIELD;
		}
		
		if (!InvoiceConstants.ALLOWED_SORT_FIELDS.contains(sortBy)) {
			throw new IllegalArgumentException("Invalid sort field : " + sortBy);
		}
		
		Sort sort = criteria.getDirection().equalsIgnoreCase("desc")
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
		
		Specification<Invoice> specification = Specification
				.where(InvoiceSpecification.hasKeyword(criteria.getKeyword()))
				.and(InvoiceSpecification.hasPaymentStatus(criteria.getPaymentStatus()));
		
		Page<Invoice> invoicePage = invoiceRepository.findAll(specification, pageable);
		
		List<InvoiceResponse> responses = invoicePage.getContent()
				.stream()
				.map(mapper::toResponse)
				.toList();
		
		return PageResponse.<InvoiceResponse>builder()
				.content(responses)
				.pageNumber(invoicePage.getNumber())
				.pageSize(invoicePage.getSize())
				.totalElements(invoicePage.getTotalElements())
				.totalPage(invoicePage.getTotalPages())
				.last(invoicePage.isLast())
				.build();
	}

	@Override
	@Transactional
	public InvoiceResponse updatePaymentStatus(Long id, UpdatePaymentStatusRequest request) {
		
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
		
		PaymentStatus current = invoice.getPaymentStatus();
		
		PaymentStatus next = request.getPaymentStatus();
		
		if (!PaymentStatusValidator.isValidTransition(current, next)) {
			
			throw new IllegalStateException("Invalid payment status transition from " + current + " to " + next);
		}
		
		invoice.setPaymentStatus(next);
		
		Invoice updated = invoiceRepository.save(invoice);
		
		return mapper.toResponse(updated);
	}

	@Override
	public byte[] downloadInvoice(Long id) {
		
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
		return invoicePdfGenerator.generate(invoice);
	}

	@Override
	public InvoiceResponse getInvoiceByBookingId(Long bookingId) {
		
		Invoice invoice = invoiceRepository.findByBookingId(bookingId).orElse(null);
		
		return invoice != null ? mapper.toResponse(invoice) : null;
	}
}
