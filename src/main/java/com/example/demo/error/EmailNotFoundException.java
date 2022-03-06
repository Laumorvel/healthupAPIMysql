package com.example.demo.error;

public class EmailNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmailNotFoundException() {
		super("Email not registered");
	}

}
