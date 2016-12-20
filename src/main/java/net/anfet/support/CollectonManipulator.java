package net.anfet.support;

import net.anfet.abstraction.IFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Фильтр для любых коллекций
 */

public class CollectonManipulator<T> {

	private final List<IFilter<T>> filters;
	private Comparator<T> comparator;
	private final List<T> resultSet;

	public CollectonManipulator() {
		filters = new LinkedList<>();
		resultSet = new LinkedList<T>();
	}

	public CollectonManipulator<T> setComparator(Comparator<T> comparator) {
		this.comparator = comparator;
		return this;
	}

	public CollectonManipulator<T> addFilter(IFilter<T> filter) {
		synchronized (filters) {
			filters.add(filter);
		}
		return this;
	}

	public CollectonManipulator<T> removeFilter(IFilter<T> filter) {
		synchronized (filters) {
			filters.remove(filter);
		}

		return this;
	}

	/**
	 * @return list suited for iterations {@link LinkedList}
	 */
	public synchronized List<T> getListForSequentialAccess() {
		return new LinkedList<T>(resultSet);
	}

	/**
	 * @return list best suited for random access {@link ArrayList}
	 */
	public synchronized List<T> getListForRandomAccess() {
		return new ArrayList<T>(resultSet);
	}

	public synchronized CollectonManipulator<T> update(Collection<T> items) {
		resultSet.clear();

		if (items != null && !items.isEmpty()) {
			if (filters.isEmpty())
				resultSet.addAll(items);
			else {
				for (T item : items) {
					boolean allow = true;
					synchronized (filters) {
						for (IFilter<T> filter : filters) {
							if (!filter.filter(item)) {
								allow = false;
								break;
							}
						}
					}

					if (allow) {
						resultSet.add(item);
					}
				}
			}

			if (comparator != null) {
				resultSet.sort(comparator);
			}
		}

		return this;
	}

}
