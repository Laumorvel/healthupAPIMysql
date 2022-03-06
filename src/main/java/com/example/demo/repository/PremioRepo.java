package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Premio;

public interface PremioRepo extends JpaRepository<Premio, Long>{
	
	@Query(value = "SELECT p FROM Premio p WHERE user_id = ?1 AND id = ?2")
	List<Long>getUserPremio(Long user_id, Long premio_id);
	

}
