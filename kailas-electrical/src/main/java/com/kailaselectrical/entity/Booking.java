package com.kailaselectrical.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.kailaselectrical.common.BaseEntity;
import com.kailaselectrical.enums.BookingStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "bookings")
public class Booking extends BaseEntity{
	
	@Column(nullable = false, unique = true)
	private String bookingNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookingServiceItem> serviceItems = new ArrayList<>();
	
	@Column(nullable = false)
	private LocalDate bookingDate;
	
	@Column(nullable = false)
	private LocalTime bookingTime;
	
	@Column(columnDefinition = "TEXT")
	private String remarks;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status;
	
	@Column(nullable = false)
	private BigDecimal estimatedPrice = BigDecimal.ZERO;
}
