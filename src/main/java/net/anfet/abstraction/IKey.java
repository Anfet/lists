package net.anfet.abstraction;

import java.io.Serializable;

/**
 * Key element which has some kind of id
 */
public interface IKey<T> extends Serializable, Comparable<T> {
	T getId();
}
