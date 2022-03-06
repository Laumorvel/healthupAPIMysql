package com.example.demo.error;

public class UsernameAlreadyRegistered extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsernameAlreadyRegistered() {
		super("Username already registered");
	}
}
