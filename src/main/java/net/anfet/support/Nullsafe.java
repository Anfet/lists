package net.anfet.support;

/**
 * class for safely getting nullable values and defaults
 */
public final class Nullsafe {
	public static <T> T get(T source, T fallback) {
		return source == null ? fallback : source;
	}

	private Nullsafe() {

	}
}
