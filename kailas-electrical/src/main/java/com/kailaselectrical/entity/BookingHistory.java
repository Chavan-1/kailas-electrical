package com.kailaselectrical.entity;

import java.time.LocalDateTime;

import com.kailaselectrical.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "booking_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "booking_id", nullable = false)
	private Booking booking;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BookingStatus oldStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BookingStatus newStatus;
	
	@Column(length = 500)
	private String remarks;
	
	@Column(nullable = false)
	private String changedBy;
	
	@Column(nullable = false)
	private LocalDateTime changedAt;
}
