package com.example.demo.error;

public class LogroWronglyFormedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LogroWronglyFormedException() {
		super("Achievement wrongly formed");
	}
}
