package com.kailaselectrical.entity;

import java.math.BigDecimal;

import com.kailaselectrical.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "services")
public class Service extends BaseEntity {
	
	@Column(nullable = false, unique = true)
	private String serviceName;
	
	@Column(length = 500)
	private String description;
	
	@Column(nullable = false)
	private BigDecimal basePrice;
	
	@Column(nullable = false)
	private Integer estimatedDuration;
	
	@Column(nullable = false)
	private Boolean active = true;
}
