package com.kailaselectrical.entity;

import com.kailaselectrical.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer extends BaseEntity{
	
	@Column(nullable = false)
	private String fullName;
	
	@Column(nullable = false, unique = true, length = 10)
	private String phoneNumber;
	
	@Column(unique = true)
	private String email;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String address;
	
	private Boolean active = true;
}
