package com.example.demo.error;

public class LogroAlreadyContainingPremioException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LogroAlreadyContainingPremioException () {
		super("This achievement already has an award");
	}

}
