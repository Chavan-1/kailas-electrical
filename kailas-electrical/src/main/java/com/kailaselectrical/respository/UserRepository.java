package com.kailaselectrical.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kailaselectrical.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
	
	boolean existsByMobileNumber(String mobileNumber);
	
	boolean existsByEmail(String email);
}
