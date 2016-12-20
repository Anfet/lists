package net.anfet.abstraction;

/**
 * Simple filter interface for any object
 */
public interface IFilter<T> {
	boolean filter(T t);
}
