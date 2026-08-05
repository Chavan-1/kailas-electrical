package com.kailaselectrical.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.kailaselectrical.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "services")
public class ElectricalService extends BaseEntity {
	
	@Column(nullable = false)
	private BigDecimal basePrice;
	
	@Column(nullable = false)
	private Integer estimatedDuration;
	
	@Column(nullable = false)
	private Boolean active = true;
	
	@OneToMany(mappedBy = "electricalService", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ElectricalServiceTranslations> translations = new ArrayList<>();
}
