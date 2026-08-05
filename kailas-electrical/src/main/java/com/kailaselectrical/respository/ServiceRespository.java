package com.kailaselectrical.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kailaselectrical.entity.ElectricalService;


public interface ServiceRespository extends JpaRepository<ElectricalService, Long>, JpaSpecificationExecutor<ElectricalService>{
	
	Page<ElectricalService> findByActiveTrue(Pageable pageable);
	
	@Query("""
			SELECT DISTINCT s 
			FROM ElectricalService s
			JOIN s.translations t
			WHERE s.active = true
			AND (
				LOWER(t.serviceName) LIKE LOWER(CONCAT('%', :keyword, '%'))
				OR
				LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
			)
			""")
	Page<ElectricalService> searchActiveServices(
			@Param("keyword") String keyword,
			Pageable pageable);
	
}
