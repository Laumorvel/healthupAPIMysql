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
import com.example.demo.error.AwardNotFoundInLogroException;
import com.example.demo.error.EmailAlreadyRegisteredException;
import com.example.demo.error.IncorrectDateException;
import com.example.demo.error.InvalidCredentialsException;
import com.example.demo.error.LogroAlreadyContainingPremioException;
import com.example.demo.error.LogroAlreadyRegisteredException;
import com.example.demo.error.LogroNoExistenteException;
import com.example.demo.error.LogroWronglyFormedException;
import com.example.demo.error.ObjectiveNotAllowedException;
import com.example.demo.error.PremioNotFoundException;
import com.example.demo.error.PremioNotRegisteredInLogroException;
import com.example.demo.error.TokenNotFoundException;
import com.example.demo.error.UserNotFounfException;
import com.example.demo.error.UsuarioNoExistenteException;
import com.example.demo.model.Logro;
import com.example.demo.model.Premio;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.PremioService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Clase controladores, donde encontramos los endpoints para la gestión del
 * usuario. Está enlazada con la clase de servicio del usuario, la cual contiene
 * la lógica tras las peticiones.
 * 
 * @author laura
 *
 */
@RestController
public class UserController {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private PremioService premioService;

	/**
	 * Comprueba que el usuario se encuentra registrado, en caso contrario, nos
	 * saltará una excepción
	 * 
	 * @return
	 */
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
			@RequestParam(required = false) Integer objetivoFood, @RequestParam (required=false)String newPassword) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long id = this.userRepo.findByUsername(username).getId();
		if (objetivoSport != null) {
			return this.userService.cambiaObjetivoSport(id, objetivoSport, "sport");
		} else if(objetivoFood != null) {
			return this.userService.cambiaObjetivoSport(id, objetivoSport, "food");
		}else {
			return this.userService.cambiaContrasena(newPassword, id);
		}
	}

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
	 * @throws Exception 
	 */
	@PostMapping("/newLogro")
	public Logro anadeLogro(@RequestBody Logro logro) throws Exception {
		String username = "";
		Long id = null;
		try {
			username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			id = this.userRepo.findByUsername(username).getId();
		}catch(NullPointerException e) {
			throw new TokenNotFoundException();
		}
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

	/**
	 * Método para eliminar un logro concreto, al que le pasamos la id del logro que
	 * buscamos
	 * 
	 * @param id
	 */
	@DeleteMapping("/eliminaLogro/{id}")
	public void eliminaLogro(@PathVariable Long id) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long idUser = this.userRepo.findByUsername(username).getId();
		this.userService.eliminaLogro(id, idUser);
	}

	/**
	 * 2º nivel Consigue un premio, el cual se encuentra dentro de un logro.
	 * 
	 * @param idLogro
	 * @param idPremio
	 * @return
	 */
	@GetMapping("/logro/{idLogro}/premio/{idPremio}")
	public Premio getPremio(@PathVariable Long idLogro, @PathVariable Long idPremio) {
		return premioService.getPremio(idPremio, idLogro);
	}

	/**
	 * 2º nivel Crea un premio nuevo, el cual se encuentra dentro de un logro
	 * 
	 * @param idLogro
	 * @param premio
	 * @return
	 */
	@PostMapping("/logro/{idLogro}/premio")
	public Premio publicaPremio(@PathVariable Long idLogro, @RequestBody Premio premio) {
		return premioService.publicaPremio(premio, idLogro);
	}

	/**
	 * 2º nivel Modifica el premio de un logro.
	 * 
	 * @param idLogro
	 * @param idPremio
	 * @param premio
	 * @return
	 */
	@PutMapping("/logro/{idLogro}/premio/{idPremio}")
	public Premio modificaPremio(@PathVariable Long idLogro, @PathVariable Long idPremio, @RequestBody Premio premio) {
		return premioService.modificaPremio(idPremio, premio, idLogro);
	}

	/**
	 * 2º nivel Elimina el premio de un logro. Tendremos que eliminar el id de ese
	 * premio que esté referenciado en cualquier logro.
	 * 
	 * @param idLogro
	 * @param idPremio
	 */
	@DeleteMapping("/logro/{idLogro}/premio/{idPremio}")
	public void eliminaPremio(@PathVariable Long idLogro, @PathVariable Long idPremio) {
		premioService.borraPremio(idPremio, idLogro);
	}
	
	/**
	 * Consigue la contraseña del usuario.
	 * @return string
	 */
	@GetMapping("/getPassword")
	public String getPassword() {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = this.userRepo.findByUsername(username);
		return user.getPassword();
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
	 * Indica que el premio no se ha registrado en un logro concreto.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(PremioNotRegisteredInLogroException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(PremioNotRegisteredInLogroException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	/**
	 * Indica que el token no se ha insertado.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(TokenNotFoundException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(TokenNotFoundException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.UNAUTHORIZED);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
	}

	/**
	 * Indica que el logro ya contiene un premio.
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(LogroAlreadyContainingPremioException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(LogroAlreadyContainingPremioException ex) {
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

	/**
	 * Indica que el usuario no se encuentra en al bbdd
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(UserNotFounfException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(UserNotFounfException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	/**
	 * Indica que los datos introducidos son incorrectos
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(IncorrectDateException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(IncorrectDateException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	/**
	 * Indica que no se ha encontrado el premio indicado
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(PremioNotFoundException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(PremioNotFoundException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}
	
	/**
	 * Indica que no se ha introducido el tipo adecuado de logro (food o sport)
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(LogroWronglyFormedException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(LogroWronglyFormedException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	/**
	 * Indica que el logro no se encuentra en la bbdd
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(LogroNoExistenteException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(LogroNoExistenteException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	/**
	 * Indica que el usuario no se encuentra en la bbdd
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(UsuarioNoExistenteException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(UsuarioNoExistenteException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.NOT_FOUND);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	/**
	 * Indica que el logro ya está registrado (se comprueba por fecha)
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(LogroAlreadyRegisteredException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(LogroAlreadyRegisteredException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.CONFLICT);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	/**
	 * Indica que el objetivo introducido no está permitido (entre 1 y 7)
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ObjectiveNotAllowedException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(ObjectiveNotAllowedException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.CONFLICT);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	/**
	 * Indica que el premio no se encuentra dentro del logro concreto
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(AwardNotFoundInLogroException.class)
	public ResponseEntity<ApiError> handleJsonMappingException(AwardNotFoundInLogroException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
}
