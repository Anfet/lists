package net.anfet;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Thread safe map for mapping lists to any object
 */
public final class MultiMap<T, V> implements Serializable {

	private final Map<T, List<V>> map;

	public MultiMap() {
		map = new HashMap<T, List<V>>();
	}

	public synchronized Set<T> keys() {
		return new HashSet<T>(map.keySet());
	}

	public synchronized void remove(T key) {
		map.remove(key);
	}

	public synchronized void clear(T key) {
		map.put(key, new LinkedList<V>());
	}

	/**
	 * removes value element from key set
	 * @param key
	 * @param value
	 */
	public synchronized boolean remove(T key, V value) {
		if (key == null || value == null) {
			throw new NullPointerException("Key or value is null");
		}


		List<V> list = map.get(key);
		if (list != null) {
			return list.remove(value);
		}

		return false;
	}

	/**
	 * adds {@link V} to collection of {@link T}
	 * @param key   key
	 * @param value adding value
	 */
	public synchronized V add(T key, V value) {
		if (key == null || value == null) {
			throw new NullPointerException("Key or value is null");
		}


		List<V> list = map.get(key);
		if (list == null) {
			map.put(key, (list = new LinkedList<V>()));
		}

		list.add(value);

		return value;
	}

	/**
	 * @param key key
	 * @return defensive copy of the internal list
	 */
	public synchronized List<V> get(T key) {
		List<V> list;
		if (key == null || ((list = map.get(key)) == null)) {
			return new LinkedList<V>();
		}

		return new LinkedList<V>(list);
	}

	/**
	 * clears the map
	 */
	public synchronized void clear() {
		map.clear();
	}

	public synchronized boolean contains(T key) {
		return map.containsKey(key);
	}

	public synchronized void set(T type, Collection<V> fields) {

		if (type == null)
			throw new NullPointerException("type is null");

		map.put(type, fields == null ? new LinkedList<V>() : new LinkedList<V>(fields));
	}
}
