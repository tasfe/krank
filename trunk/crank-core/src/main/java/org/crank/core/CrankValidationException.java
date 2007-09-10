package org.crank.core;

public class CrankValidationException extends CrankException {

	public CrankValidationException() {

	}

	public CrankValidationException(String message, Throwable wrappedException) {
		super(message, wrappedException);
	}

	public CrankValidationException(String message) {
		super(message);
	}

	public CrankValidationException(String message, Object... args) {
		super(message, args);
	}

	public CrankValidationException(Throwable wrappedException, String message,
			Object... args) {
		super(wrappedException, message, args);
	}

	public CrankValidationException(Throwable wrappedExeption) {
		super(wrappedExeption);
	}

}
