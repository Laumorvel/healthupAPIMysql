package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Mensaje;

public interface MensajeRepo extends JpaRepository<Mensaje, Long>{
	
	/**
	 * Encuentra un mensaje por email
	 * @param email
	 * @return
	 */
	public Mensaje findByEmail(String email);

}
