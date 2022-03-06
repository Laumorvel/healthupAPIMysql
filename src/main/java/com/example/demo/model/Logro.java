package com.example.demo.model;


import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Logro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String fecha;

	private Boolean logradoDia;
	
	private Boolean noRegistrado;
	
	@JsonIgnore
	@ManyToOne
	private User user;
	
	@Column(nullable = false)
	private String tipo;
	
	/**
	 * Solo algunos logros tienen premios
	 */
	@ManyToOne
	private Premio premio = null;

	
	/**
	 * HashCode e equal por fecha y tipo
	 */
	@Override
	public int hashCode() {
		return Objects.hash(fecha, tipo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Logro other = (Logro) obj;
		return Objects.equals(fecha, other.fecha) && tipo == other.tipo;
	}

	
	public Logro(String fecha, Boolean logradoDia, String tipo) {
		this.fecha = fecha;
		this.logradoDia = logradoDia;
		this.tipo = tipo;
	}
	
	
	//NO REGISTRADO
	public Logro(String fecha, User user, String tipo, Boolean noRegistrado) {
		this.fecha = fecha;
		this.user = user;
		this.tipo = tipo;
		this.noRegistrado = noRegistrado;
	}

	//LOGRO BÁSICO
	public Logro(String fecha, Boolean logradoDia, User user, String tipo) {
		this.fecha = fecha;
		this.logradoDia = logradoDia;
		this.user = user;
		this.tipo = tipo;
	}
	
	//LOGRO CON PREMIO
	public Logro(String fecha, Boolean logradoDia, User user, String tipo, Premio premio) {
		this.fecha = fecha;
		this.logradoDia = logradoDia;
		this.user = user;
		this.tipo = tipo;
		this.premio = premio;
	}
	

	public Logro() {}

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

	public Boolean getLogradoDia() {
		return logradoDia;
	}

	public void setLogradoDia(Boolean logradoDia) {
		this.logradoDia = logradoDia;
	}

	public Boolean getNoRegistrado() {
		return noRegistrado;
	}

	public void setNoRegistrado(Boolean noRegistrado) {
		this.noRegistrado = noRegistrado;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Premio getPremio() {
		return premio;
	}

	public void setPremio(Premio premio) {
		this.premio = premio;
	}
	
	
}
