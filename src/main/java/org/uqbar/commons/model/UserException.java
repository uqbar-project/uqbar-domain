package org.uqbar.commons.model;

/**
 * DOCME
 * 
 * @author npasserini
 */
public class UserException extends RuntimeException {

	public UserException(String message) {
		super(message);
	}

	public UserException(String message, Exception cause) {
		super(message, cause);
	}

	/**
	 * TODO Revisar este algoritmo
	 */
	public static UserException find(RuntimeException exception) {
		if (exception instanceof UserException) {
			return (UserException) exception;
		} else if (exception.getCause() != exception
				&& (exception.getCause() instanceof RuntimeException)) {
			return find((RuntimeException) exception.getCause());
		}

		throw exception;
	}

}
