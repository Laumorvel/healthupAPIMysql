package com.example.demo.error;

public class PremioNotRegisteredInLogroException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PremioNotRegisteredInLogroException() {
		super("This achievement does not have any award");
	}

}
