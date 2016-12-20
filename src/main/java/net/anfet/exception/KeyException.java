package net.anfet.exception;

import java.util.Locale;

/**
 * Generic Key exception
 */
public class KeyException extends Exception {

	public KeyException() {

	}

	public KeyException(String message) {
		super(message);
	}

	public KeyException(String pattern, Object... args) {
		super(String.format(Locale.getDefault(), pattern, args));
	}
}
