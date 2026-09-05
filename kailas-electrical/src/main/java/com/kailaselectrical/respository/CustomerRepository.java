package com.kailaselectrical.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kailaselectrical.entity.Customer;
import com.kailaselectrical.entity.User;


public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer>{
	
	Optional<Customer> findByPhoneNumber(String phoneNumber);
	
	Optional<Customer> findByEmail(String email);
	
	boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
	
	Optional<Customer> findByUser(User user);
	
	Optional<Customer> findByUserId(Long userId);

}
