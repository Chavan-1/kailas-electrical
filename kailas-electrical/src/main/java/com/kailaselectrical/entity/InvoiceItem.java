package com.kailaselectrical.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kailaselectrical.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "invoice_id")
	private Invoice invoice;
	
	@ManyToOne
	@JoinColumn(name = "service_id")
	private ElectricalService electricalService;
	
	@Column(nullable = false)
	private Integer quantity;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal unitPrice;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal lineTotal;
}
