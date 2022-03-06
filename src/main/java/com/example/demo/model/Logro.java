package com.example.demo.model;


import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
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
	
	/**
	 * NO REGISTRADO
	 * @param fecha
	 * @param user
	 * @param tipo
	 * @param noRegistrado
	 */
	public Logro(String fecha, User user, String tipo, Boolean noRegistrado) {
		this.fecha = fecha;
		this.user = user;
		this.tipo = tipo;
		this.noRegistrado = noRegistrado;
	}

	/**
	 * normales
	 * @param fecha
	 * @param logradoDia
	 * @param user
	 * @param tipo
	 */
	public Logro(String fecha, Boolean logradoDia, User user, String tipo) {
		this.fecha = fecha;
		this.logradoDia = logradoDia;
		this.user = user;
		this.tipo = tipo;
	}
	
	
	

}
