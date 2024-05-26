package com.aninfo.exceptions;

/**
 * Custom exception for invalid values.
 */
public class InvalidValueException extends Exception {

	/**
	 * Constructor.
	 */
    public InvalidValueException()
	{
        super();
    }

	/**
	 * Constructor.
	 * 
	 * @param String message	the message.
	 */
	public InvalidValueException(String message) {
        super(message);
    }
}
