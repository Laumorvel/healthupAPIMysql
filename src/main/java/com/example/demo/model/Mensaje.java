package com.example.demo.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Mensaje {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	
	@Column(nullable = false)
	private String email;
	
	private String phone;
	
	@Column(nullable = false)
	private String mssg;
	
	private String companyName;
	
	@Column(nullable = false)
	private String name;

	public Mensaje( String companyName, String email, String mssg, String name, String phone) {
		this.email = email;
		this.phone = phone;
		this.mssg = mssg;
		this.name = name;
		this.companyName = companyName;
	}
	
	public Mensaje(String name, String email, String phone, String mssg) {
		this.email = email;
		this.phone = phone;
		this.mssg = mssg;
		this.name = name;
	}
	
	public Mensaje(String name, String email, String mssg) {
		this.email = email;
		this.name = name;
		this.mssg = mssg;
	}

}
