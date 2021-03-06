package net.anfet;

import net.anfet.abstraction.IFilter;
import net.anfet.abstraction.IKey;
import net.anfet.exception.ElementNotFoundException;
import net.anfet.support.NameGetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Отсортированный список объектов объектов забэкованный с помощью карты, позволяющий быстро искать элементы как по интексу, так и по ключу
 */
@SuppressWarnings("WeakerAccess")
public class Keys<T extends IKey> implements Collection<T>, Serializable {

	private Map<Object, T> map = new TreeMap<>();

	public Keys(Collection<T> list) {
		addAll(list);
	}

	public Keys() {

	}

	@Override
	public synchronized boolean add(T object) {
		addElement(object);
		return true;
	}

	private void addElement(T object) {
		map.put(object.getId(), object);
	}


	@Override
	public boolean addAll(Collection<? extends T> collection) {
		if (collection == null)
			return false;

		for (T element : collection) {
			addElement(element);
		}

		return true;
	}

	@Override
	public synchronized void clear() {
		map.clear();
	}

	@Override
	public synchronized boolean contains(Object object) {
		return map.containsValue(object);
	}

	@Override
	public synchronized boolean containsAll(Collection<?> collection) {
		return collection != null && map.values().containsAll(collection);
	}

	@Override
	public synchronized boolean isEmpty() {
		return map.isEmpty();
	}


	@Override
	public Iterator<T> iterator() {
		return map.values().iterator();
	}

	@Override
	public synchronized boolean remove(Object object) {
		return object != null && object instanceof IKey && map.remove(((IKey) object).getId()) != null;
	}

	@Override
	public synchronized boolean removeAll(Collection<?> collection) {
		if (collection == null)
			return false;


		boolean removed = false;
		for (Object element : collection)
			removed |= map.remove(((IKey) element).getId()) != null;

		return removed;

	}

	@Override
	public synchronized boolean retainAll(Collection<?> collection) {

		Collection<T> values = map.values();

		return values.retainAll(collection);
	}

	@Override
	public synchronized int size() {
		return map.size();
	}


	@Override
	public synchronized Object[] toArray() {
		return map.values().toArray();
	}


	@Override
	public synchronized <T1> T1[] toArray(T1[] array) {
		return map.values().toArray(array);
	}

	public synchronized T get(int index) throws ElementNotFoundException {
		List<T> list = new ArrayList<>(map.values());
		T element;
		try {
			element = list.get(index);
		} catch (IndexOutOfBoundsException ex) {
			throw new ElementNotFoundException(ex.getMessage());
		}

		if (element == null) {
			throw new ElementNotFoundException();
		}

		return element;
	}

	public synchronized T findByKey(Object key) throws ElementNotFoundException {
		if (key == null)
			return null;

		if (!map.containsKey(key))
			throw new ElementNotFoundException();

		return map.get(key);
	}


	public synchronized Keys<T> filter(IFilter<T> filter) {
		Keys<T> list = new Keys<>();

		for (T element : this) {
			if (filter.filter(element))
				list.add(element);
		}

		return list;
	}

	public synchronized List<T> list() {
		return new LinkedList<>(this.map.values());
	}


	public synchronized <X> List<X> ids() {
		List<X> ids = new LinkedList<>();

		for (T element : this) {
			ids.add((X) element.getId());
		}

		return ids;
	}


	public synchronized String[] names() {
		return names(NameGetter.DEFAULT);
	}

	public synchronized String[] names(NameGetter<T> getter) {
		String[] names = new String[size()];
		int i = 0;
		for (T obj : this) {
			names[i++] = getter.getName(obj);
		}
		return names;
	}


	public synchronized T first() throws ElementNotFoundException {
		if (isEmpty())
			throw new ElementNotFoundException();

		return get(0);
	}


	public synchronized T last() throws ElementNotFoundException {
		if (isEmpty())
			throw new ElementNotFoundException();

		return get(size() - 1);
	}

	public synchronized int indexOf(T element) {
		if (element == null) {
			return -1;
		}

		int i = 0;
		for (T item : this) {
			if (item.getId().equals(element.getId())) return i;
			i++;
		}

		return -1;
	}

	public synchronized boolean removeByKey(Object key) {
		return key != null && map.remove(key) != null;
	}


	public synchronized T head() throws ElementNotFoundException {
		return first();
	}

	public synchronized T tail() throws ElementNotFoundException {
		return last();
	}

	public synchronized Keys<T> extract(IFilter<T> filter) {
		if (filter == null) return new Keys<>(this);

		Keys<T> newKeys = new Keys<>();
		for (T item : this) {
			if (filter.filter(item)) newKeys.add(item);
		}
		return newKeys;
	}
}
