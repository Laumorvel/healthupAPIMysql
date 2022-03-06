package com.example.demo.error;

public class PremioNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PremioNotFoundException() {
		super("Award not found");
	}
}
