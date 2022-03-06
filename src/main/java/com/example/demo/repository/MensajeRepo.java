package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Mensaje;

public interface MensajeRepo extends JpaRepository<Mensaje, Long>{
	
	public Mensaje findByEmail(String email);

}
