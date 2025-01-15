package com.autjuan.exceptions;

/**
 * Custom exception class for the global framework. This exception is thrown to
 * indicate framework-specific errors.
 * @author:[@JuanchoDelValley]
 */
public class GlobalException extends RuntimeException {

	private static final long serialVersionUID = 1L; // For serialization

	// Optional error code for more specific error identification
	private final int errorCode;

	/**
	 * Constructs a new GlobalException with the specified detail message.
	 *
	 * @param message the detail message
	 */
	public GlobalException(String message) {
		super(message);
		this.errorCode = 0; // Default error code
	}

	/**
	 * Constructs a new GlobalException with the specified detail message and cause.
	 *
	 * @param message the detail message
	 * @param cause   the cause of the exception
	 */
	public GlobalException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = 0; // Default error code
	}

	/**
	 * Constructs a new GlobalException with the specified detail message, cause,
	 * and error code.
	 *
	 * @param message   the detail message
	 * @param cause     the cause of the exception
	 * @param errorCode the error code associated with the exception
	 */
	public GlobalException(String message, Throwable cause, int errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * Returns the error code associated with this exception.
	 *
	 * @return the error code
	 */
	public int getErrorCode() {
		return errorCode;
	}

	@Override
	public String toString() {
		return String.format("GlobalException{message='%s', errorCode=%d}", getMessage(), errorCode);
	}
}