package com.example.demo.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
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
	
	public Mensaje() {}
	
	@Override
	public String toString() {
		return "Mensaje [id=" + id + ", fecha=" + fecha + ", email=" + email + ", phone=" + phone + ", mssg=" + mssg
				+ ", companyName=" + companyName + ", name=" + name + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMssg() {
		return mssg;
	}

	public void setMssg(String mssg) {
		this.mssg = mssg;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
