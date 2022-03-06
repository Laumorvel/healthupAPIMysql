package com.example.demo.error;

public class EmailAlreadyRegisteredException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmailAlreadyRegisteredException() {
		super("Email already registered");
	}

}
