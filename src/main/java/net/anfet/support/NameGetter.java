package net.anfet.support;

/**
 * Created by Oleg on 16.06.2017.
 * <p>
 * interface for extracting names from keys
 */
public interface NameGetter<T> {

	NameGetter DEFAULT = new NameGetter() {
		@Override
		public String getName(Object element) {
			return element.toString();
		}
	};

	/**
	 * extracts name from specific element
	 * @param element key element
	 * @return name
	 */
	String getName(T element);
}
