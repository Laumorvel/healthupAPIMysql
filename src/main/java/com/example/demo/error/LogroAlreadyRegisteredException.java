package com.example.demo.error;

public class LogroAlreadyRegisteredException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LogroAlreadyRegisteredException() {
		super("The record is already registered for this user");
	}

}
