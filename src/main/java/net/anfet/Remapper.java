package net.anfet;

import net.anfet.exception.RemapperException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Remaps one collection into another
 */
public final class Remapper {

	private Remapper() {

	}


	/**
	 * maps collection of elements of class {@link X} to list of elements of class {@link Y} using a {@link Function}
	 * @param elements source elements
	 * @param function remapping function
	 * @param <X> source class
	 * @param <Y> target class
	 * @return list of target element classses
	 * @throws RemapperException {@link RuntimeException} if something goes wrong during remap
	 */
	public static <X, Y> List<Y> remap(Collection<X> elements, Function<X, Y> function) throws RemapperException {
		List<Y> remappedList = new LinkedList<Y>();
		for (X x : elements) {
			Y remappedElement = function.remap(x);
			remappedList.add(remappedElement);
		}

		return remappedList;
	}

	/**
	 * remapper function interface
	 * @param <X> source class
	 * @param <Y> target class
	 */
	public interface Function<X, Y> {
		/**
		 * remaps element x to y
		 * @param x source element
		 * @return target element
		 * @throws RemapperException if something goes wrong
		 */
		Y remap(X x) throws RemapperException;
	}
}
