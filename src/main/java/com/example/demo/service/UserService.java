package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.error.EmailAlreadyRegisteredException;
import com.example.demo.error.LogroAlreadyRegisteredException;
import com.example.demo.error.LogroNoExistenteException;
import com.example.demo.error.LogroNoRegistradoException;
import com.example.demo.error.ObjectiveNotAllowedException;
import com.example.demo.error.UsernameAlreadyRegistered;
import com.example.demo.error.UsuarioNoExistenteException;
import com.example.demo.model.Logro;
import com.example.demo.model.User;
import com.example.demo.repository.LogroRepo;
import com.example.demo.repository.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private LogroRepo logroRepo;

	public User getUserEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public User getUsername(String username) {
		return userRepo.findByUsername(username);
	}

	/**
	 * Se hace una comprobación en el frontend para saber si hacer un put o un post
	 * Cuando se use el post (llamado por este método), no habrá un logro con la
	 * misma fecha y ese id de usuario en la tabla de logros
	 * 
	 * @param logro
	 * @param idUser
	 * @return logro
	 */
	public Logro addLogro(Logro logro, Long id) {
		// Encuentro al usuario por id y se lo añado al logro pues no lo trae incluido
		User user;
		try {
			user = this.userRepo.findById(id).get();
		} catch (Exception e) {
			throw new UsuarioNoExistenteException();
		}
		compruebaLogro(logro, id);
		try {
			logro.setUser(user);
			user.seteaAvancePost(logro);
			this.userRepo.save(user);
			return this.logroRepo.save(logro);
		} catch (Exception e) {
			throw new LogroNoExistenteException();
		}
	}

	private void compruebaLogro(Logro logro, Long id) {
		if (this.logroRepo.getLogro(logro.getTipo(), logro.getFecha(), id) != null) {
			throw new LogroAlreadyRegisteredException();
		}
	}

	public void compruebaRegistro(User user) {
		if (this.userRepo.findByUsername(user.getUsername()) != null) {
			throw new UsernameAlreadyRegistered();
		}
		if (this.userRepo.findByEmail(user.getEmail()) != null) {
			throw new EmailAlreadyRegisteredException();
		}
	}

	public Logro modificaLogro(Logro logro, Long id, Long idLogro) {
		// Encuentro al usuario por id y se lo añado al logro pues no lo trae incluido
		User user;
		try {
			user = this.userRepo.findById(id).get();
		} catch (Exception e) {
			throw new UsuarioNoExistenteException();
		}
		if (this.logroRepo.getIdLogroFromUser(idLogro, id) == null) {
			throw new LogroNoExistenteException();
		}

		try {
			logro.setUser(user);
			logro.setId(idLogro);// le pongo la misma id para que lo sustituya al guardarlo.
			user.seteaAvance(logro);
			return this.logroRepo.save(logro);
		} catch (Exception e) {
			throw new LogroNoExistenteException();
		}

	}

	public User getUser(Long id) {
		try {
			return userRepo.getById(id);
		} catch (Exception e) {
			throw new UsuarioNoExistenteException();
		}
	}

	public List<Logro> getRegistroUser(Long id) {
		User user;
		try {
			user = this.userRepo.findById(id).get();
		} catch (Exception e) {
			throw new UsuarioNoExistenteException();
		}
		try {
			return this.logroRepo.findByUser(user);
		} catch (Exception e) {
			throw new LogroNoExistenteException();
		}
	}

	public List<Logro> getRegistroFiltradoTipo(Long id, String tipo) {
		// Hago esto para comprobar que el usuario introducido existe
		User user;
		try {
			user = this.userRepo.findById(id).get();
		} catch (Exception e) {
			throw new UsuarioNoExistenteException();
		}
		if (tipo.equals("food") || tipo.equals("sport")) {
			return this.logroRepo.getLogrosTipo(user.getId(), tipo);
		} else {
			throw new LogroNoRegistradoException();
		}
	}

	public void reiniciaAvance() {
		// Se reinicia el avance de los usuarios
		this.userRepo.reiniciaAvance();
		// Se guardan en BBDD con los cambios
		for (User user : this.userRepo.findAll()) {
			this.userRepo.save(user);
		}
	}

	public void creaUnNoRegistrado(Long idUser, String tipo, String ayer) {
		User user = userRepo.getById(idUser);
		Logro logroFood = new Logro(ayer, user, tipo, true);
		this.logroRepo.save(logroFood);
	}

	public List<Long> getIdUsersWithoutFoodRegister(String fecha) {
		return this.logroRepo.getIdUsersWithoutFoodRegister(fecha);
	}

	public List<Long> getIdUsersWithoutSportRegister(String fecha) {
		return this.logroRepo.getIdUsersWithoutSportRegister(fecha);
	}

	public void eliminaLogro(Long id, Long idUser) {
		try {
			this.userRepo.findById(idUser).get();
		} catch (Exception e) {
			throw new UsuarioNoExistenteException();
		}
		if (this.logroRepo.getIdLogroFromUser(id, idUser) == null) {
			throw new LogroNoExistenteException();
		}else {
			this.logroRepo.deleteById(id);
		}
		
	}

	/**
	 * Actualiza el objetivo de deporte del usuario.
	 * @param id
	 * @param objetivoSport
	 * @return usuario
	 */
	public User cambiaObjetivoSport(Long id, Integer objetivoSport, String tipo) {
		User user;
		try {
			user = this.userRepo.findById(id).get();
		} catch (Exception e) {
			throw new UsuarioNoExistenteException();
		}
		if(tipo.equals("sport")) {
			if(objetivoSport > 0 && objetivoSport < 8) {
				user.setObjetivoSportSemanal(objetivoSport);
				return this.userRepo.save(user);
			}else {
				throw new ObjectiveNotAllowedException();
			}
		}else {
			if(objetivoSport > 0 && objetivoSport < 8) {
				user.setObjetivoFoodSemanal(objetivoSport);
				return this.userRepo.save(user);
			}else {
				throw new ObjectiveNotAllowedException();
			}
		}
	}
	
	

}
