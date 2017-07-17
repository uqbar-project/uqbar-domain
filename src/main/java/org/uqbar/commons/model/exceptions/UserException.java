package org.uqbar.commons.model.exceptions;

/**
 * Modela una excepción de usuario: esto ocurre cuando el usuario carga información
 * que no cumple con las reglas impuestas por el dominio.
 * 
 * <hr>
 * 
 * User exception represents and invalid user input that does not fulfill business
 * rules 
 * 
 * @author npasserini
 */
public class UserException extends RuntimeException {

	private static final long serialVersionUID = -3529440449758596711L;

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
