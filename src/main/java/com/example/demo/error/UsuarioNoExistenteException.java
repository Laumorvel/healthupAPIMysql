package com.example.demo.error;

public class UsuarioNoExistenteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioNoExistenteException() {
		super("User not identified");
	}
}
