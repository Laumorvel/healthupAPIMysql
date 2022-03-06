package com.example.demo.error;

public class UserWronglyFormedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserWronglyFormedException() {
		super("Users must contain all their attributes using an appropriate form");
	}

}
