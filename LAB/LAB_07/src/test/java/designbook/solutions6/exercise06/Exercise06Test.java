package designbook.solutions6.exercise06;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test Suite for Exercise 6.6 (Safety Approach):
 * Verifies CompositeShow2 functionality, iterator behavior, immutability,
 * and client unpacking requirements.
 */
public class Exercise06Test {

	private Concert concert;
	private Movie movie1;
	private Movie movie2;

	@BeforeEach
	public void setUp() {
		concert = new Concert("Symphony No. 5", "Beethoven", 45);
		movie1 = new Movie("The Godfather", 1972, 175);
		movie2 = new Movie("The Dark Knight", 2008, 152);
	}

	@Test
	public void testValidCompositeCreation() {
		CompositeShow2 comp = new CompositeShow2(concert, movie1);
		assertEquals(2, comp.size());
		assertEquals(45 + 175, comp.runningTime());
	}

	@Test
	public void testConstructorUnderMinimumShowsThrows() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeShow2(concert);
		}, "Should throw IllegalArgumentException when less than 2 shows provided");
	}

	@Test
	public void testConstructorOverMaximumShowsThrows() {
		Movie m3 = new Movie("Movie 3", 2020, 100);
		Movie m4 = new Movie("Movie 4", 2021, 100);
		Movie m5 = new Movie("Movie 5", 2022, 100);
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeShow2(concert, movie1, movie2, m3, m4, m5);
		}, "Should throw IllegalArgumentException when more than 5 shows provided");
	}

	@Test
	public void testConstructorNullShowThrows() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeShow2(concert, null);
		}, "Should throw IllegalArgumentException when a subshow is null");
	}

	@Test
	public void testIteratorUnpacksAllShowsInOrder() {
		CompositeShow2 comp = new CompositeShow2(concert, movie1, movie2);
		List<Show> unpacked = new ArrayList<>();
		for (Show s : comp) {
			unpacked.add(s);
		}

		assertEquals(3, unpacked.size());
		assertSame(concert, unpacked.get(0));
		assertSame(movie1, unpacked.get(1));
		assertSame(movie2, unpacked.get(2));
	}

	@Test
	public void testIteratorIsUnmodifiable() {
		CompositeShow2 comp = new CompositeShow2(concert, movie1);
		Iterator<Show> it = comp.iterator();
		assertTrue(it.hasNext());
		it.next();
		assertThrows(UnsupportedOperationException.class, it::remove,
				"Calling remove() on CompositeShow2 iterator must throw UnsupportedOperationException");
	}

	@Test
	public void testNestedCompositeUnpacking() {
		CompositeShow2 inner = new CompositeShow2(movie1, movie2);
		CompositeShow2 outer = new CompositeShow2(concert, inner);

		assertEquals(2, outer.size());
		assertEquals(45 + 175 + 152, outer.runningTime());

		List<Show> outerShows = new ArrayList<>();
		for (Show s : outer) {
			outerShows.add(s);
		}
		assertSame(concert, outerShows.get(0));
		assertSame(inner, outerShows.get(1));

		// Unpack inner composite
		assertTrue(outerShows.get(1) instanceof CompositeShow2);
		CompositeShow2 unpackedInner = (CompositeShow2) outerShows.get(1);
		List<Show> innerShows = new ArrayList<>();
		for (Show s : unpackedInner) {
			innerShows.add(s);
		}
		assertEquals(2, innerShows.size());
		assertSame(movie1, innerShows.get(0));
		assertSame(movie2, innerShows.get(1));
	}

	@Test
	public void testDescriptionFormatting() {
		CompositeShow2 comp = new CompositeShow2(concert, movie1);
		String expected = "[" + concert.description() + "; " + movie1.description() + "]";
		assertEquals(expected, comp.description());
	}

	@Test
	public void testDeepCopy() {
		CompositeShow2 comp = new CompositeShow2(concert, movie1);
		Show copied = comp.copy();

		assertNotNull(copied);
		assertNotSame(comp, copied);
		assertEquals(comp.description(), copied.description());
		assertEquals(comp.runningTime(), copied.runningTime());
		assertEquals(comp, copied);
	}

	@Test
	public void testEqualsAndHashCode() {
		CompositeShow2 comp1 = new CompositeShow2(concert, movie1);
		CompositeShow2 comp2 = new CompositeShow2(concert, movie1);
		CompositeShow2 comp3 = new CompositeShow2(movie1, concert);

		assertEquals(comp1, comp2);
		assertEquals(comp1.hashCode(), comp2.hashCode());
		assertNotEquals(comp1, comp3);
	}
}

