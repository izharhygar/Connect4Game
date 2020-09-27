package com.example.connect4.exceptions;

public class GameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6545295308530249255L;

	public GameException(Throwable e) {
		super(e);
	}

	public GameException() {

	}

	public GameException(String message) {
		super(message);
	}

}
