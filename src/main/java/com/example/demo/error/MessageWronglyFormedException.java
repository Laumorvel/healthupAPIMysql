package com.example.demo.error;

public class MessageWronglyFormedException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageWronglyFormedException() {
		super("The message must contain an email, a name and a message.");
	}

}
