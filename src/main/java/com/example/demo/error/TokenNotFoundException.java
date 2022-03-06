package com.example.demo.error;

public class TokenNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TokenNotFoundException() {
		super("Token not found");
	}

}
