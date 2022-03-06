package com.example.demo.error;

public class AwardNotFoundInLogroException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AwardNotFoundInLogroException() {
		super("This award do not correspond to this achievement.");
	}

}
