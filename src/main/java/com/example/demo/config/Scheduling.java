package com.example.demo.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.service.UserService;

@Configuration
public class Scheduling {
	
	@Autowired
	public UserService userService;
	
	
	/**
	 * Todos los lunes a medianoche se vuelven a poner a 0 los avances de cada usuario.
	 */
	@Scheduled(cron = "0 0 0 * * MON")
	public void checkObjectivesEveryDay() {
		this.userService.reiniciaAvance();
		System.out.println("Reiniciado avance de usuarios");
	}
	
	
	/**
	 * Compruebo cada noche a las 00:00 que se han creado los objetivos.
	 * Lo comprueba con el día anterior al que estamos puesto que ya a las 00:00 pasa al día siguiente.
	 * Se crearán los objetivos que sean necesarios. 
	 */
	//@Scheduled(fixedRate = 60000)
	@Scheduled(cron = "0 0 0 * * *")
	public void creaNoRegistrado() {
		String ayer = LocalDate.now().minus(1, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		List<Long>idUsersWithoutFoodRegister = this.userService.getIdUsersWithoutFoodRegister(ayer);
		System.out.println(idUsersWithoutFoodRegister);
		List<Long>idUsersWithoutSportRegister = this.userService.getIdUsersWithoutSportRegister(ayer);
		System.out.println(idUsersWithoutSportRegister);
		
		for (Long long1 : idUsersWithoutSportRegister) {
			this.userService.creaUnNoRegistrado(long1, "sport", ayer);
		}
		
		for (Long long1 : idUsersWithoutFoodRegister) {
			this.userService.creaUnNoRegistrado(long1, "food", ayer);
		}
		System.out.println("Creado no registrados en los usuarios que no han marcado su avance.");
		
	}

}
