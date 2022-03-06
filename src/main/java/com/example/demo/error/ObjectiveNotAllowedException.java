package com.example.demo.error;

public class ObjectiveNotAllowedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ObjectiveNotAllowedException() {
		super("The objective selected is not correct. It shall be between 1 and 7.");
	}

}
