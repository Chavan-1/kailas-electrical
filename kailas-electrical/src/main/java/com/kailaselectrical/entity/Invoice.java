package com.kailaselectrical.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kailaselectrical.enums.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String invoiceNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "booking_id", nullable = false, unique = true)
	private Booking booking;
	
	@Column(nullable = false)
	private LocalDate invoiceDate;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal subtotal;
	
	@Column(nullable = false, precision = 5, scale = 2)
	private BigDecimal gstPercentage;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal gstAmount;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal discount;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	
	private String notes;
	
	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<InvoiceItem> invoiceItems = new ArrayList<>();
}
