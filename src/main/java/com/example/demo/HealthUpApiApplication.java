package com.example.demo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.example.demo.model.Logro;
import com.example.demo.model.Premio;
import com.example.demo.model.User;
import com.example.demo.repository.LogroRepo;
import com.example.demo.repository.PremioRepo;
import com.example.demo.repository.UserRepo;

@SpringBootApplication
@EnableScheduling
public class HealthUpApiApplication extends SpringBootServletInitializer{
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HealthUpApiApplication.class, args);
	}
	
	/**
	 * Se ha creado un usuario para hacer pruebas en la aplicación.
	 * @param repositorioUsers
	 * @return
	 */
	@Bean
	CommandLineRunner initData (UserRepo repositorioUsers, LogroRepo logroRepo, PremioRepo premioRepo) {
		User user = new User("Loli", "Montes García", passwordEncoder.encode("loli123"), "loli", "loli@gmail.com", 2, 2);
		User user2 = new User("Pepi", "Moreno García", passwordEncoder.encode("pepi123"), "pepi", "pepi@gmail.com", 4, 3, 1,2);
		User user3 = new User("Pili", "Aguilar García", passwordEncoder.encode("pili123"), "pili", "pili@gmail.com", 5, 5,2,2);
		return (args) -> {
			repositorioUsers.saveAll(
					Arrays.asList(user2, user, user3));
			logroRepo.saveAll(
					//LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
					
					//USER 1 -- no registrado sport
					Arrays.asList(new Logro("16-02-2022", false, user2, "food"),
							(new Logro("16-02-2022", false, user2, "sport")),
							(new Logro("12-02-2022", user2, "sport", true)),//no registrado
							(new Logro("13-02-2022", true, user2, "food")),
							(new Logro("20-02-2022", true, user2, "food")),
					
					//USER 2 -- ninguno no registrado	
							(new Logro("20-02-2022", true, user, "food")),
							(new Logro("20-02-2022", true, user, "sport")),
							
					//USER 3 -- los dos no registrados
							(new Logro("15-02-2022", true, user3, "food")),
							(new Logro("15-02-2022", true, user3, "sport")),
							(new Logro("14-02-2022", false, user3, "food")),
							(new Logro("14-02-2022", false, user3, "sport")),
							(new Logro("16-02-2022", true, user3, "food")),
							(new Logro("16-02-2022", true, user3, "sport")),
							(new Logro("17-02-2022", user3, "sport", true)),//no registrado
							(new Logro("17-02-2022", user3, "food", true)),//no registrado
							(new Logro("18-02-2022", user3, "food", true))//no registrado 
							));
			
			//PREMIOS
			Premio premio6 = new Premio("Premio amigo", user);
			Premio premio7 = new Premio("Premio a la compasión", user2);
			Premio premio8 = new Premio("Premio al esfuerzo", user3);
			
			premioRepo.saveAll(
					Arrays.asList(
							 premio6, premio7, premio8));
		};
	}
	
	

}
