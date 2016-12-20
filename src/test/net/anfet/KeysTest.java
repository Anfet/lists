package net.anfet;

import net.anfet.abstraction.IKey;
import net.anfet.exception.ElementNotFoundException;
import net.anfet.exception.RemapperException;
import net.anfet.support.CollectonManipulator;
import net.anfet.support.Nullsafe;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Oleg on 16.12.2016.
 */
public class KeysTest {

	Keys<Element> elements;

	@org.junit.Before
	public void setUp() throws Exception {
		elements = new Keys<>();
	}

	@Test
	public void shouldAddOneElement() {
		elements.add(new Element(0));
		Assert.assertTrue(!elements.isEmpty());
		Assert.assertTrue(elements.size() == 1);
	}

	@Test(expected = ElementNotFoundException.class)
	public void shouldRaiseExceptionIfFirstOnEmptyKeys() throws ElementNotFoundException {
		elements.first();
	}

	@Test()
	public void shouldNotRaiseExceptionIfFirstOnEmptyKeys() throws ElementNotFoundException {
		elements.add(new Element(2));
		elements.first();
	}

	@Test
	public void shouldNotThrowConcurrentModification() {
		Thread a = new Thread(new Runnable() {
			@Override
			public void run() {
				for (long i = 0; i < 1000; i++) elements.add(new Element(i));
			}
		});

		Thread b = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!elements.isEmpty()) {
					try {
						elements.remove(elements.first());
					} catch (ElementNotFoundException e) {
						Assert.fail();
					}
				}
			}
		});

		a.start();
		b.start();

		try {
			a.join();
			b.join();
		} catch (InterruptedException ignored) {

		}
	}


	@Test
	public void shouldFindElement() {
		Long a = 6443346344L;
		Long b = 6443346344L;
		elements.add(new Element(a));
		elements.removeByKey(b);
		Assert.assertTrue(elements.isEmpty());

	}

	@Test
	public void shouldWorkWithManipulator() {

		CollectonManipulator<Element> collectonManipulator = new CollectonManipulator<>();
		for (int i = 0; i < 100; i++) {
			elements.add(new Element(i));
		}


		List<Element> list = collectonManipulator.update(elements).getListForSequentialAccess();
		Assert.assertTrue(!list.isEmpty());
	}

	@Test
	public void remapperShouldWork() {
		elements.add(new Element(123));

		List<Long> longs = Remapper.remap(elements, new Remapper.Function<Element, Long>() {
			@Override
			public Long remap(Element element) throws RemapperException {
				return element.getId();
			}
		});

		Assert.assertTrue(!longs.isEmpty());
	}

	@Test(expected = RemapperException.class)
	public void remapperShouldFail() {
		elements.add(new Element(123));

		List<Long> longs = Remapper.remap(elements, new Remapper.Function<Element, Long>() {
			@Override
			public Long remap(Element element) throws RemapperException {
				throw new RemapperException();
			}
		});

		Assert.fail();
	}


	private static class Element implements IKey<Long> {

		private Long id;

		public Element(long id) {
			this.id = id;
		}

		@Override
		public Long getId() {
			return id;
		}

		@Override
		public int compareTo(Long o) {
			return Nullsafe.get(id, 0L).compareTo(Nullsafe.get(o, 0L));
		}
	}

}