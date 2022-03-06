package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String surname;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String email;

	private Integer objetivoFoodSemanal;

	private Integer objetivoSportSemanal;

	private Boolean logradoSemanaFood = false;

	private Boolean logradoSemanaSport = false;

	private Integer avanceSemanaFood = 0;

	private Integer avanceSemanaSport = 0;
	

	
	public User(String name, String surname, String password, String username, String email,
			Integer objetivoFoodSemanal, Integer objetivoSportSemanal) {
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.username = username;
		this.email = email;
		this.objetivoFoodSemanal = objetivoFoodSemanal;
		this.objetivoSportSemanal = objetivoSportSemanal;
	}

	public User(String name, String surname, String password, String username, String email) {
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.username = username;
		this.email = email;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(String name, String surname, String password, String username, String email,
			Integer objetivoFoodSemanal, Integer objetivoSportSemanal, Integer avanceSemanaFood,
			Integer avanceSemanaSport) {
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.username = username;
		this.email = email;
		this.objetivoFoodSemanal = objetivoFoodSemanal;
		this.objetivoSportSemanal = objetivoSportSemanal;
		this.avanceSemanaFood = avanceSemanaFood;
		this.avanceSemanaSport = avanceSemanaSport;
	}
	
	

	public void seteaAvance(Logro logro) {
		if (logro.getLogradoDia() != null) {
			if (logro.getTipo().equals("food")) {
				if (logro.getLogradoDia()) {
					if (this.avanceSemanaFood < 7) {
						this.avanceSemanaFood++;
					}
				} else {
					if (this.avanceSemanaFood > 0) {
						this.avanceSemanaFood--;
					}
				}

			} else if (logro.getTipo().equals("sport")) {
				if (logro.getLogradoDia()) {
					if (this.avanceSemanaSport < 7) {
						this.avanceSemanaSport++;
					}
				} else {
					if (this.avanceSemanaSport > 0) {
						this.avanceSemanaSport--;
					}
				}
			}
		}
		this.logradoSemanaFood = (this.objetivoFoodSemanal <= this.avanceSemanaFood);
		this.logradoSemanaSport = (this.objetivoSportSemanal <= this.avanceSemanaSport);
	}

	/**
	 * Cuando sea un post, no se le restará al avance 1 si es false.
	 * 
	 * @param logro
	 */
	public void seteaAvancePost(Logro logro) {
		if (logro.getLogradoDia() != null) {
			if (logro.getTipo().equals("food")) {
				if (logro.getLogradoDia()) {
					if (this.avanceSemanaFood < 7) {
						this.avanceSemanaFood++;
					}
				}
			} else if (logro.getTipo().equals("sport")) {
				if (logro.getLogradoDia()) {
					if (this.avanceSemanaSport < 7) {
						this.avanceSemanaSport++;
					}
				}
			}
		}
		this.logradoSemanaFood = (this.objetivoFoodSemanal <= this.avanceSemanaFood);
		this.logradoSemanaSport = (this.objetivoSportSemanal <= this.avanceSemanaSport);
	}

}
