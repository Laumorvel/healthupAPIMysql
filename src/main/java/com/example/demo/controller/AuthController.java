package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.error.ApiError;
import com.example.demo.error.EmailAlreadyRegisteredException;
import com.example.demo.error.InvalidCredentialsException;
import com.example.demo.error.MessageWronglyFormedException;
import com.example.demo.error.UserWronglyFormedException;
import com.example.demo.error.UsernameAlreadyRegistered;
import com.example.demo.model.LoginCredentials;
import com.example.demo.model.Mensaje;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.security.JWTUtil;
import com.example.demo.service.MensajeService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthController {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private MensajeService mensajeService;

	@PostMapping("/auth/register")
	public Map<String, Object> registerHandler(@RequestBody User user) throws Exception {
		userService.compruebaRegistro(user);
		try {
			String encodedPass = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPass);
			user = userRepo.save(user);
			String token = jwtUtil.generateToken(user.getUsername());
			return Collections.singletonMap("access_token", token);
		}catch(HttpMessageNotReadableException e) {
			throw new Exception("This request requires a body");
		}catch(Exception e ) {
			throw new UserWronglyFormedException();
		}
	}

	@PostMapping("/auth/login")
	public Map<String, Object> loginHandler(@RequestBody LoginCredentials body) {
		try {
			UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
					body.getUsername(), body.getPassword());
			authManager.authenticate(authInputToken);
			String token = jwtUtil.generateToken(body.getUsername());
			return Collections.singletonMap("access_token", token);

		} catch (AuthenticationException authExc) {
			if (userRepo.getUserName(body.getUsername()) != null) {
				throw new InvalidCredentialsException();
			} else {
				throw new InvalidCredentialsException();
			}
		}
	}

	/**
	 * Comprueba que el email o el username indicado. Si existe en la bbdd se lo
	 * comunicará al usuario.
	 * 
	 * @param email / username
	 * @return usuario o null
	 */
	@GetMapping("auth/user")
	public User checkEmailUsers(@RequestParam(required = false) String email,
			@RequestParam(required = false) String username) {
		if (username == null) {
			return userService.getUserEmail(email);
		} else {
			return userService.getUsername(username);
		}
	}

	/**
	 * Endpoint para comprobar que el usuario está logueado. Lo único que queremos
	 * es que nos devuelva el token, en caso contrario, indicará que el usuario no
	 * está logueado y solo quiere acceder directamente desde la url sin loguearse.
	 * 
	 * @return token o error
	 * @throws Exception
	 */
	@GetMapping("/login")
	public ResponseEntity<String> comprobarLogueo() throws Exception {

		try {
			return ResponseEntity.ok("");
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	/**
	 * Endpoint para conseguir token. Cuando hace validaciones al hacer el registro,
	 * se debe validar que el email y el username no estén registrados en la bbdd.
	 * Por tanto, se necesitará el token para hacer el getMapping. Aquí hago
	 * postMapping para no tener que introducir un token, sino obtenerlo y guardarlo
	 * en el localStorage.
	 * 
	 * @return (token)
	 */
	@PostMapping("/auth/token")
	public Map<String, Object> consigueToken(@RequestBody LoginCredentials body) {
		try {
			if(body != null) {
				String token = jwtUtil.generateToken(body.getEmail());
				return Collections.singletonMap("access_token", token);
			}else {
				throw new InvalidCredentialsException();

			}
		}catch(Exception ex) {
			throw new InvalidCredentialsException();
		}
	}
	
	@PostMapping("auth/newMessage")
	public Mensaje newMessage(@RequestBody Mensaje msg) throws MessagingException {
		return mensajeService.newMensaje(msg);
	}

	// EXCEPCIONES------------------------------------------------------------
	/**
	 * Modifica la salida de la excepción del pedido
	 * 
	 * @param ex
	 * @return excepción
	 */
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(InvalidCredentialsException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.CONFLICT);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(HttpMessageNotReadableException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(UserWronglyFormedException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(UserWronglyFormedException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(EmailAlreadyRegisteredException.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(EmailAlreadyRegisteredException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.CONFLICT);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	@ExceptionHandler(UsernameAlreadyRegistered.class)
	public ResponseEntity<ApiError> handleProductoNoEncontrado(UsernameAlreadyRegistered ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.CONFLICT);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	
	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<ApiError> handleJsonMappingException(JsonMappingException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(MessageWronglyFormedException.class)
	public ResponseEntity<ApiError> handleJsonMappingException(MessageWronglyFormedException ex) {
		ApiError apiError = new ApiError();
		apiError.setEstado(HttpStatus.BAD_REQUEST);
		apiError.setFecha(LocalDateTime.now());
		apiError.setMensaje(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

}
