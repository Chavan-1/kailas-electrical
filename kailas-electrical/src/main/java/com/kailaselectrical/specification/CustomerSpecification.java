package com.kailaselectrical.specification;

import org.springframework.data.jpa.domain.Specification;

import com.kailaselectrical.entity.Customer;

public class CustomerSpecification {
	
	private CustomerSpecification() {
		
	}
	
	public static Specification<Customer> hasKeyword(String keyword) {
		
		return (root, query, cb) -> {
			
			if (keyword == null || keyword.isBlank()) {
				return cb.conjunction();
			}
			
			String search = "%" + keyword.toLowerCase() + "%";
			
			return cb.or(
					cb.like(cb.lower(root.get("fullName")), search),
					cb.like(cb.lower(root.get("phoneNumber")), search),
					cb.like(cb.lower(root.get("email")), search)
			);
		};
	}
	
	public static Specification<Customer> hasActive(Boolean active) {
		
		return (root, query, cb) -> {
			
			if (active == null) {
				return cb.conjunction();
			}
			
			return cb.equal(root.get("active"), active);
		};
	}
}
