package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				
				//login
				registry.addMapping("/auth/login").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
						.allowedMethods("GET", "POST", "OPTIONS", "PUT")
						.allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
								"Access-Control-Request-Method", "Access-Control-Request-Headers")
						.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//register
				registry.addMapping("/auth/register").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
						.allowedMethods("GET", "POST", "OPTIONS", "PUT")
						.allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
								"Access-Control-Request-Method", "Access-Control-Request-Headers")
						.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//comprobación logueo con guardián
				registry.addMapping("/login").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
						.allowedMethods("GET", "POST", "OPTIONS", "PUT")
						.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
								"Access-Control-Request-Method", "Access-Control-Request-Headers")
						.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//conseguir token
				registry.addMapping("/auth/token").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
						.allowedMethods("GET", "POST", "OPTIONS", "PUT")
						.allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "accept", "Origin",
								"Access-Control-Request-Method", "Access-Control-Request-Headers")
						.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//user
				registry.addMapping("/user").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
						.allowedMethods("GET", "POST", "OPTIONS", "PUT")
						.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
								"Access-Control-Request-Method", "Access-Control-Request-Headers")
						.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
			
				//check email and username
				registry.addMapping("auth/user").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
						.allowedMethods("GET", "POST", "OPTIONS", "PUT")
						.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
								"Access-Control-Request-Method", "Access-Control-Request-Headers")
						.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				
				//registro(historial de logros)
				registry.addMapping("/registro").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
				.allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//Añade logro
				registry.addMapping("/newLogro").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
				.allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//Modifica logro 
				//Put para modificar logros de usuario
				registry.addMapping("/modificaLogro/{idLogro}").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
				.allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//mensaje - contact us
				registry.addMapping("/auth/newMessage").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
				.allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//Elimina logro
				registry.addMapping("/eliminaLogro/{id}").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
				.allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//Crea premio
				registry.addMapping("/logro/{idLogro}/premio").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
				.allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				//Borra, edita y consigue premio
				registry.addMapping("/logro/{idLogro}/premio/{idPremio}").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
				.allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
				
				//Consigue contraseña
				registry.addMapping("/getPassword").allowedOrigins("https://laumorvel.github.io/healthupDespliegueFrontend")
				.allowedMethods("GET", "POST", "OPTIONS", "PUT")
				.allowedHeaders("Content-Type","Authorization", "X-Requested-With", "accept", "Origin",
						"Access-Control-Request-Method", "Access-Control-Request-Headers")
				.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
				
			}

		};
	}


}
