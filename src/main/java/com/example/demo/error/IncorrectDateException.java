package com.example.demo.error;

public class IncorrectDateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IncorrectDateException() {
		super("Incorrect date");
	}

}
