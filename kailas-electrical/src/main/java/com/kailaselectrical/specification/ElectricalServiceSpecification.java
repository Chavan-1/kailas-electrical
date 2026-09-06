package com.kailaselectrical.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.kailaselectrical.entity.ElectricalService;
import com.kailaselectrical.entity.ElectricalServiceTranslations;

import jakarta.persistence.criteria.Join;

public class ElectricalServiceSpecification {
	
	private ElectricalServiceSpecification() {
		
	}
	
	public static Specification<ElectricalService> hasActive(Boolean active) {
		
		return (root, query, cb) -> {
			
			if (active == null) {
				return cb.conjunction();
			}
			
			return cb.equal(root.get("active"), active);
		};
	}

	public static Specification<ElectricalService> hasKeyword(String keyword) {
		
		return (root, query, cb) -> {
			
			if (keyword == null || keyword.isBlank()) {
				return cb.conjunction();
			}
			
			String search = "%" + keyword.trim().toLowerCase() + "%";
			
			Join<ElectricalService, ElectricalServiceTranslations> translation = 
					root.join("translations");
			
			return cb.or(
					cb.like(cb.lower(translation.get("serviceName")), search),
					cb.like(cb.lower(translation.get("description")), search)
			);
		};
	}
	
	public static Specification<ElectricalService> hasPriceBetween(
			BigDecimal minPrice,
			BigDecimal maxPrice) {
		
		return (root, query, criteriaBuilder) -> {
			
			if (minPrice == null && maxPrice == null) {
				return criteriaBuilder.conjunction();
			}
			
			if (minPrice != null && maxPrice != null) {
				return criteriaBuilder.between(root.get("basePrice"), minPrice, maxPrice);
			}
			
			if (minPrice != null) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("basePrice"), minPrice);
			}
			
			return criteriaBuilder.lessThanOrEqualTo(root.get("basePrice"), maxPrice);
		};
	}
}
