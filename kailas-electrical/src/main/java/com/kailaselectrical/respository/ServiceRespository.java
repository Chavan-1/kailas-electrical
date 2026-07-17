package com.kailaselectrical.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kailaselectrical.entity.Service;


public interface ServiceRespository extends JpaRepository<Service, Long>{
	Optional<Service> findByServiceName(String serviceName);
}
