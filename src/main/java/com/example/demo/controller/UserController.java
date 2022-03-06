package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.error.ApiError;
import com.example.demo.error.EmailAlreadyRegisteredException;
import com.example.demo.error.IncorrectDateException;
import com.example.demo.error.InvalidCredentialsException;
import com.example.demo.error.LogroAlreadyRegisteredException;
import com.example.demo.error.LogroNoExistenteException;
import com.example.demo.error.ObjectiveNotAllowedException;
import com.example.demo.error.PremioNotFoundException;
import com.example.demo.error.UserNotFounfException;
import com.example.demo.error.UsuarioNoExistenteException;
import com.example.demo.model.Logro;
import com.example.demo.model.Premio;
import com.example.demo.model.User;
import com.example.demo.repository.PremioRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.PremioService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private UserService userService;
	
	@Autowired
	private PremioRepo premioRepo;
	
	@Autowired
	private PremioService premioService;

	@GetMapping("/user")
	public User getUserDetails() {
		User user;
		try {
			String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			try {
				user = userRepo.findByUsername(username);
			} catch (Exception e) {
				throw new InvalidCredentialsException();
			}
			return user;
		} catch (Exception e) {
			throw new InvalidCredentialsException();
		}
	}

	/**
	 * Actualiza el objetivo SPORT del usuario
	 * 
	 * @param id
	 * @param objetivoSport
	 * @return usuario
	 */
	@PutMapping("/user")
	public User cambiaObjetivoSport(@RequestParam(required = false) Integer objetivoSport,
			@RequestParam(required = false) Integer objetivoFood) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long id = this.userRepo.findByUsername(username).getId();
		if(objetivoSport != null) {
			return this.userService.cambiaObjetivoSport(id, objetivoSport, "sport");
		}else {
			return this.userService.cambiaObjetivoSport(id, objetivoSport, "food");
		}
	}
//
//	@GetMapping("/user")
//	public User getUser() {
//		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		User user;
//		try {
//			user = this.userRepo.findByUsername(username);
//		} catch (Exception e) {
//			throw new UserNotFounfException();
//		}
//		return user;
//	}

	/**
	 * Consigue el registro de un usuario.
	 * 
	 * @param id
	 * @return lista de logros (registro)
	 */
	@GetMapping("/registro")
	public List<Logro> getRegistroUser() {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long id = this.userRepo.findByUsername(username).getId();
		return this.userService.getRegistroUser(id);
	}

	/**
	 * Añade registro en tabla de logros cuando el usuario marque por primera vez el
	 * logro diario. El logro lo añado en el servidio a la bbdd pero lo que devuelvo
	 * es el usuario para tener los datos nuevos.
	 * 
	 * @param logro
	 * @return logro
	 */
	@PostMapping("/newLogro")
	public Logro anadeLogro(@RequestBody Logro logro) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long id = this.userRepo.findByUsername(username).getId();
		return userService.addLogro(logro, id);
	}

	/**
	 * Cambia el logro cuando el usuario vuelve a pulsar el botón el mismo día.
	 * 
	 * @param logro
	 * @return logro
	 */
	@PutMapping("/modificaLogro/{idLogro}")
	public Logro modificaLogroSport(@RequestBody Logro logro, @PathVariable Long idLogro) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long id = this.userRepo.findByUsername(username).getId();
		return userService.modificaLogro(logro, id, idLogro);
	}

	@DeleteMapping("/eliminaLogro/{id}")
	public void eliminaLogro(@PathVariable Long id) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long idUser = this.userRepo.findByUsername(username).getId();
		this.userService.eliminaLogro(id, idUser);
	}
	
	/**
	 * Para que un usuario le de un premio a otro.
	 * Se modifica el premio
	 * @param idUser
	 * @param idPremio
	 * @return Premio
	 */
	@PutMapping("/premio/{idPremio}/user/{idUser}")
	public Premio anadePremioSegunLogro(@PathVariable Long idUser, @PathVariable Long idPremio) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(username == null) {throw new UserNotFounfException();}
		return this.premioService.daPremio(idUser, idPremio, username);
	}

	// Exceptiones------------------------------------------------------------------------------

	/**
	 * Modifica la salida de la excepción del pedido
	 * 
	 * @param ex
	 * @return excepción
	 */
	@ExceptionHandler(EmailAlreadyRegisteredException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(EmailAlreadyRegisteredException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	/**
	 * Modifica la salida de la excepción BAD_REQUEST
	 * 
	 * @param ex
	 * @return excepción
	 */
	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<ApiError> handleJsonMappingException(JsonMappingException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(UserNotFounfException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(UserNotFounfException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	@ExceptionHandler(IncorrectDateException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(IncorrectDateException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(PremioNotFoundException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(PremioNotFoundException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(LogroNoExistenteException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(LogroNoExistenteException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(UsuarioNoExistenteException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(UsuarioNoExistenteException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(LogroAlreadyRegisteredException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(LogroAlreadyRegisteredException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.CONFLICT);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	@ExceptionHandler(ObjectiveNotAllowedException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(ObjectiveNotAllowedException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.CONFLICT);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}
}
