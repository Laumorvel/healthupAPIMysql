package com.example.demo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.Logro;
import com.example.demo.model.Premio;
import com.example.demo.model.User;
import com.example.demo.repository.LogroRepo;
import com.example.demo.repository.PremioRepo;
import com.example.demo.repository.UserRepo;

@SpringBootApplication
@EnableScheduling
public class HealthUpMysql2 extends SpringBootServletInitializer{
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HealthUpMysql2.class);
	}
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HealthUpMysql2.class, args);
	}
	
	/**
	 * Se han creado una serie de usuarios, premios y logros para realizar las pruebas de la aplicación.
	 * @param repositorioUsers
	 * @return
	 */
	@Bean
	CommandLineRunner initData (UserRepo repositorioUsers, LogroRepo logroRepo, PremioRepo premioRepo) {
		User user = new User("Loli", "Montes García", passwordEncoder.encode("loli123"), "loli", "loli@gmail.com", 2, 2);
		User user2 = new User("Pepi", "Moreno García", passwordEncoder.encode("pepi123"), "pepi", "pepi@gmail.com", 4, 3, 1,2);
		User user3 = new User("Pili", "Aguilar García", passwordEncoder.encode("pili123"), "pili", "pili@gmail.com", 5, 5,2,2);
		
		//PREMIOS
		Premio premio1 = new Premio("Andalusian award");
		Premio premio2 = new Premio("New Years award");
		Premio premio3 = new Premio("Feminist award");
		Premio premio4 = new Premio("Worker's day award");
		Premio premio6 = new Premio("You got through Christmas Award");
		Premio premio7 = new Premio("Gold Metal");
		Premio premio8 = new Premio("Christmas Award");

		return (args) -> {
			repositorioUsers.saveAll(
					Arrays.asList(user2, user, user3));
			
			premioRepo.saveAll(
					Arrays.asList(
							 premio1, premio2, premio3, premio4, premio6, premio7, premio8));
			
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
							(new Logro("18-02-2022", user3, "food", true)),//no registrado 
							
					//USERs con medallas
							(new Logro("28-02-2022", true, user3, "food", premio1)),
							(new Logro("28-02-2022", true, user2, "sport", premio1)),
							(new Logro("28-02-2022", true, user, "sport", premio1)),
							(new Logro("01-01-2022", true, user2, "sport", premio2)),
							(new Logro("01-01-2022", true, user, "sport", premio2)),
							(new Logro("01-01-2022", true, user3, "sport", premio2)),
							(new Logro("08-03-2022", true, user, "sport", premio3)),
							(new Logro("08-03-2022", true, user2, "food", premio3)),
							(new Logro("08-03-2022", true, user3, "sport", premio3))
							));
		};
	}
}
