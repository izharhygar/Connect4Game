package com.example.connect4.exceptions;

public class InvalidGameStatusException extends GameException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5795128199396571497L;

	public InvalidGameStatusException(String message) {
		super(message);
	}

}
