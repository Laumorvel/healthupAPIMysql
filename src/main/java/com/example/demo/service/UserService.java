package com.example.demo.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.error.EmailAlreadyRegisteredException;
import com.example.demo.error.IncorrectDateException;
import com.example.demo.error.LogroAlreadyRegisteredException;
import com.example.demo.error.LogroNoExistenteException;
import com.example.demo.error.LogroNoRegistradoException;
import com.example.demo.error.LogroWronglyFormedException;
import com.example.demo.error.ObjectiveNotAllowedException;
import com.example.demo.error.UsernameAlreadyRegistered;
import com.example.demo.error.UsuarioNoExistenteException;
import com.example.demo.model.Logro;
import com.example.demo.model.User;
import com.example.demo.repository.LogroRepo;
import com.example.demo.repository.UserRepo;

/**
 * Clase creada para poder gestionar las peticiones realizadas desde el frontend
 * o postman, con la que poder hacer CRUDs de los diferentes niveles de la
 * aplicación.
 * 
 * @author laura
 *
 */
@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private LogroRepo logroRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;


	/**
	 * Devuelve el email del usuario
	 * 
	 * @param email
	 * @return
	 */
	public User getUserEmail(String email) {
		return userRepo.findByEmail(email);
	}

	/**
	 * Devuelve el nombre de de usuario del usuario
	 * 
	 * @param username
	 * @return
	 */
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

		// Compruebo que la fecha es correcta
		if (!isDateValid(logro.getFecha())) {
			throw new IncorrectDateException();
		}

		try {
			logro.setUser(user);
			user.seteaAvancePost(logro);
			this.userRepo.save(user);
			return this.logroRepo.save(logro);
		} catch (Exception e) {
			throw new LogroNoExistenteException();
		}
	}

	/**
	 * Comprueba que el logro sea correcto, es decir, que posea todos sus
	 * parámetros. En caso contrario, saltará una excepción
	 * 
	 * @param logro
	 * @param id
	 */
	private void compruebaLogro(Logro logro, Long id) {
		if (this.logroRepo.getLogro(logro.getTipo(), logro.getFecha(), id) != null) {
			throw new LogroAlreadyRegisteredException();
		}if(!logro.getTipo().equals("food") && !logro.getTipo().equals("sport")) {
			throw new LogroWronglyFormedException();
		}
	}

	/**
	 * Método para comprobar que el nombre de usuario no esté ya registrado o que el
	 * email ya lo esté
	 * 
	 * @param user
	 */
	public void compruebaRegistro(User user) {
		if (this.userRepo.findByUsername(user.getUsername()) != null) {
			throw new UsernameAlreadyRegistered();
		}
		if (this.userRepo.findByEmail(user.getEmail()) != null) {
			throw new EmailAlreadyRegisteredException();
		}
	}

	/**
	 * Modifica un logro. Comprueba que el logro exista, que el usuario exista y
	 * elimina el logro. Hace las modificaciones necesarias en el avance del usuario
	 * teniendo en cuenta que se ha eliminado uno de sus logros.
	 * 
	 * @param logro
	 * @param id
	 * @param idLogro
	 * @return logro modificado
	 */
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
		if((!logro.getTipo().equals("food") && !logro.getTipo().equals("sport")) || !isDateValid(logro.getFecha())) {
			throw new LogroWronglyFormedException();
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

	/**
	 * Devuelve el usuario. Salta excepción si no existe
	 * 
	 * @param id
	 * @return usuario
	 */
	public User getUser(Long id) {
		try {
			return userRepo.getById(id);
		} catch (Exception e) {
			throw new UsuarioNoExistenteException();
		}
	}

	/**
	 * Devuelve todos los logros de un usario (su registro). Salta excepción si el
	 * usuario no existe
	 * 
	 * @param id
	 * @return lista de logros de un usuario
	 */
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

	/**
	 * Consigue una lista de logros de un usuario. Solo devuelve los logros del tipo
	 * que le indiquemos (food o sport). Salta exepción si el logro o el usuario no
	 * existen
	 * 
	 * @param id
	 * @param tipo
	 * @return logros de un usuario de un solo tipo (o food o sport)
	 */
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

	/**
	 * Pone el avance a 0. Esto se hace los lunes a las 00:00.
	 */
	public void reiniciaAvance() {
		// Se reinicia el avance de los usuarios
		this.userRepo.reiniciaAvance();
		// Se guardan en BBDD con los cambios
		for (User user : this.userRepo.findAll()) {
			this.userRepo.save(user);
		}
	}

	/**
	 * Crea un logro de tipo no registrado, es decir, que posee el true en la opción
	 * de no registrado.
	 * 
	 * @param idUser
	 * @param tipo
	 * @param ayer
	 */
	public void creaUnNoRegistrado(Long idUser, String tipo, String ayer) {
		User user = userRepo.getById(idUser);
		Logro logroFood = new Logro(ayer, user, tipo, true);
		this.logroRepo.save(logroFood);
	}

	/**
	 * Recupera los ids de los usuarios que no posean el registro de comida
	 * 
	 * @param fecha
	 * @return lista de ids
	 */
	public List<Long> getIdUsersWithoutFoodRegister(String fecha) {
		return this.logroRepo.getIdUsersWithoutFoodRegister(fecha);
	}

	/**
	 * Encuentra los ids de aquellos usuarios que no poseen registro de deporte
	 * 
	 * @param fecha
	 * @return lista de ids
	 */
	public List<Long> getIdUsersWithoutSportRegister(String fecha) {
		return this.logroRepo.getIdUsersWithoutSportRegister(fecha);
	}

	/**
	 * Elimina un logro comprobando si el usuario y el logro existen. Saltará
	 * excepción si el logro no existe o el usuario no existe.
	 * 
	 * @param id
	 * @param idUser
	 */
	public void eliminaLogro(Long id, Long idUser) {
		try {
			this.userRepo.findById(idUser).get();
		} catch (Exception e) {
			throw new UsuarioNoExistenteException();
		}
		if (this.logroRepo.getIdLogroFromUser(id, idUser) == null) {
			throw new LogroNoExistenteException();
		} else {
			this.logroRepo.deleteById(id);
		}

	}

	/**
	 * Actualiza el objetivo de deporte del usuario. Comprueba todos los pasos,
	 * saltando excepción en caso de que el usuario no exista, o el objetivo
	 * introducido no sea válido (1-7).
	 * 
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
		if (tipo.equals("sport")) {
			if (objetivoSport > 0 && objetivoSport < 8) {
				user.setObjetivoSportSemanal(objetivoSport);
				return this.userRepo.save(user);
			} else {
				throw new ObjectiveNotAllowedException();
			}
		} else {
			if (objetivoSport > 0 && objetivoSport < 8) {
				user.setObjetivoFoodSemanal(objetivoSport);
				return this.userRepo.save(user);
			} else {
				throw new ObjectiveNotAllowedException();
			}
		}
	}

	/**
	 * Método para comprobar si una fecha es correcta. Es un método que se usuará
	 * cuando las peticiones impliquen un logro.
	 * 
	 * @param date
	 * @return boolean
	 */
	public Boolean isDateValid(String date) {
		String DATE_FORMAT = "dd-mm-yyyy";

		try {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * Cambia la contraseña del usuario por la indicada
	 * @param newPassword
	 * @param id
	 * @return
	 */
	public User cambiaContrasena(String newPassword, Long id) {
		User user = this.userRepo.getById(id);
		user.setPassword(passwordEncoder.encode(newPassword));
		return this.userRepo.save(user);
	}

}
