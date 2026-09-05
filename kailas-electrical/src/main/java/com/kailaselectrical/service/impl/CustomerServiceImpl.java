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
import com.kailaselectrical.dto.request.CreateCustomerRequest;
import com.kailaselectrical.dto.request.SearchCriteria;
import com.kailaselectrical.dto.request.UpdateCustomerRequest;
import com.kailaselectrical.dto.request.UpdateCustomerStatusRequest;
import com.kailaselectrical.dto.response.BookingResponse;
import com.kailaselectrical.dto.response.CustomerDetailsResponse;
import com.kailaselectrical.dto.response.CustomerResponse;
import com.kailaselectrical.dto.response.InvoiceResponse;
import com.kailaselectrical.entity.Booking;
import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.Invoice;
import com.kailaselectrical.entity.User;
import com.kailaselectrical.enums.BookingStatus;
import com.kailaselectrical.enums.PaymentStatus;
import com.kailaselectrical.enums.Role;
import com.kailaselectrical.exception.ResourceAlreadyExistsException;
import com.kailaselectrical.exception.ResourceNotFoundException;
import com.kailaselectrical.mapper.BookingMapper;
import com.kailaselectrical.mapper.CustomerMapper;
import com.kailaselectrical.mapper.InvoiceMapper;
import com.kailaselectrical.respository.BookingRepository;
import com.kailaselectrical.respository.CustomerRepository;
import com.kailaselectrical.respository.InvoiceRepository;
import com.kailaselectrical.respository.UserRepository;
import com.kailaselectrical.service.CustomerService;
import com.kailaselectrical.service.EmailService;
import com.kailaselectrical.specification.CustomerSpecification;
import com.kailaselectrical.util.CustomerConstants;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

	private final CustomerRepository customerRepository;
	
	private final UserRepository userRepository;
	
	private final CustomerMapper mapper;
	
	private final BookingRepository bookingRepository;

	private final InvoiceRepository invoiceRepository;

	private final BookingMapper bookingMapper;

	private final InvoiceMapper invoiceMapper;
	
	private final EmailService emailService;
	
	@Override
	public CustomerResponse createCustomer(CreateCustomerRequest request) {
		
		customerRepository.findByPhoneNumber(request.getPhoneNumber())
						  .ifPresent(customer -> {
							  throw new ResourceAlreadyExistsException("Phone number already registered.");
						  });
		
		if (request.getEmail() != null && !request.getEmail().isBlank()) {
			
			customerRepository.findByEmail(request.getEmail())
							  .ifPresent(customer -> {
								  throw new ResourceAlreadyExistsException("Email already registered.");
							  });
		}
		
		Customer customer = mapper.toEntity(request);
		
		customer.setActive(true);
		
		Customer saved = customerRepository.save(customer);
		
		emailService.sendWelcomeEmail(saved.getId());
		
		return mapper.toResponse(saved);
	}

	@Override
	public PageResponse<CustomerResponse> getAllCustomers(SearchCriteria criteria) {
		
		String sortBy = criteria.getSortBy();
		
		if (sortBy == null || sortBy.isBlank()) {
			sortBy = CustomerConstants.DEFAULT_SORT_FIELD;
		}
		
		if (!CustomerConstants.ALLOWED_SORT_FIELDS.contains(sortBy)) {
			throw new IllegalArgumentException("Invalid sort field: " + sortBy);
		}
		
		Sort sort = criteria.getDirection().equalsIgnoreCase("desc")
		        ? Sort.by(sortBy).descending()
		        : Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
		
		Specification<Customer> specification = Specification.where(CustomerSpecification.hasActive(criteria.getActive()))
															 .and(CustomerSpecification.hasKeyword(criteria.getKeyword()));
		
		Page<Customer> customerPage = customerRepository.findAll(specification, pageable);
		
		List<CustomerResponse> responses = customerPage.getContent()
										               .stream()
										               .map(mapper::toResponse)
										               .toList();
		return PageResponse.<CustomerResponse>builder()
				.content(responses)
				.pageNumber(customerPage.getNumber())
				.pageNumber(customerPage.getNumber())
				.pageSize(customerPage.getSize())
				.totalElements(customerPage.getTotalElements())
				.totalPage(customerPage.getTotalPages())
				.last(customerPage.isLast())
				.build();
	}

	@Override
	public CustomerResponse getCustomerById(Long id) {
		
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id : " + id));
		
		return mapper.toResponse(customer);
	}

	@Override
	public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
		
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id : "+ id));
		
		customerRepository.findByPhoneNumber(request.getPhoneNumber())
						  .ifPresent(existingCustomer -> {
							 if(!existingCustomer.getId().equals(id)) {
							 
								 throw new ResourceAlreadyExistsException("Phone number already registered.");
							 }
						   });
		
		if (request.getEmail() != null && !request.getEmail().isBlank()) {
			
			customerRepository.findByEmail(request.getEmail())
			  .ifPresent(existingCustomer -> {
				 if(!existingCustomer.getId().equals(id)) {
				 
					 throw new ResourceAlreadyExistsException("Email already registered.");
				 }
			   });
		}
		
		mapper.updateEntity(customer, request);
		
		Customer updatedCustomer = customerRepository.save(customer);
		
		return mapper.toResponse(updatedCustomer);
	}

	@Override
	public void deleteCustomer(Long id) {

		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with id : " + id));
		
		customer.setActive(false);
		
		customerRepository.save(customer);
	}

	@Override
	@Transactional
	public CustomerResponse updateStatus(Long id, UpdateCustomerStatusRequest request) {
		
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		
		if (customer.getUser().getRole() != Role.CUSTOMER) {
		    throw new IllegalStateException(
		            "Only customer accounts can be activated or deactivated");
		}
		
		User user = customer.getUser();
		
		boolean active = request.getActive();
		
		customer.setActive(active);
		
		user.setEnabled(active);
		
		userRepository.save(user);
		Customer updated = customerRepository.save(customer);
		
		return mapper.toResponse(updated);
	}

	@Override
	public CustomerDetailsResponse getCustomerDetails(Long id) {
		
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
		
		List<Booking> bookings = bookingRepository.findByCustomer(customer);
		
		List<Invoice> invoices = invoiceRepository.findByBookingIn(bookings);
		
		List<BookingResponse> bookingResponses = bookings.stream()
				.map(bookingMapper::toResponse)
				.toList();
		
		List<InvoiceResponse> invoiceResponses = invoices.stream()
				.map(invoiceMapper::toResponse)
				.toList();
		
		BigDecimal totalSpent = invoiceRepository.getTotalSpent(customer.getId(), PaymentStatus.PAID);
		
		BigDecimal pendingAmount = invoiceRepository.getTotalSpent(customer.getId(), PaymentStatus.PENDING);
		
		long totalBookings = bookingRepository.countByCustomer(customer);
		
		long completedBookings = bookingRepository.countByCustomerAndStatus(customer, BookingStatus.COMPLETED);
		
		long cancelledBookings = bookingRepository.countByCustomerAndStatus(customer, BookingStatus.CANCELLED);
		
		LocalDate lastBookingDate = bookingRepository
										.findTopByCustomerOrderByBookingDateDesc(customer)
										.map(Booking::getBookingDate)
										.orElse(null);
		
		return CustomerDetailsResponse.builder()
				.customer(mapper.toResponse(customer))
				.bookings(bookingResponses)
				.invoices(invoiceResponses)
				.totalBookings((int) totalBookings)
				.completedBookings((int) completedBookings)
				.cancelledBookings((int) cancelledBookings)
				.totalSpent(totalSpent)
				.pendingAmount(pendingAmount)
				.lastBookingDate(lastBookingDate)
				.build();
	}

}
