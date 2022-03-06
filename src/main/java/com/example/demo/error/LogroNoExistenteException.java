package com.example.demo.error;

public class LogroNoExistenteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public LogroNoExistenteException() {
		super("Non-existent record");
	}

}
