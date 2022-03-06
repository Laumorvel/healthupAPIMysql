package com.example.demo.error;

public class LogroNoRegistradoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LogroNoRegistradoException() {
		super("That kind of record is nor registered (try with food or sport)");
	}

}
