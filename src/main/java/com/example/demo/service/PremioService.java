package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.error.PremioNotFoundException;
import com.example.demo.error.UserNotFounfException;
import com.example.demo.model.Logro;
import com.example.demo.model.Premio;
import com.example.demo.model.User;
import com.example.demo.repository.PremioRepo;
import com.example.demo.repository.UserRepo;

@Service
public class PremioService {

	@Autowired
	private PremioRepo premioRepo;
	
	@Autowired UserRepo userRepo;

	
	public Premio daPremio(Long idUser, Long idPremio, String username) {
		Premio premio;
		User user;
		User userEnvio;
		try {
			user = userRepo.findById(idUser).get();
			userEnvio = userRepo.findByUsername(username);
		}catch(Exception e) {
			throw new UserNotFounfException();
		}
		try {
			premio = premioRepo.findById(idPremio).get();
			premioRepo.getUserPremio(userEnvio.getId(), idPremio);
		}catch(Exception e) {
			throw new PremioNotFoundException();
		}
		
		//Cambio el usuario del premio
		premio.setUser(user);
		return premioRepo.save(premio);
		
	}


}
