package designbook.solutions6.exercise07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test Suite for Exercise 6.7 (Transparency Approach):
 * Verifies modified Show interface, polymorphic iteration on composite and leaves,
 * immutability, and recursive flattening without 'instanceof'.
 */
public class Exercise07Test {

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
		CompositeShow comp = new CompositeShow(concert, movie1);
		assertEquals(2, comp.size());
		assertEquals(45 + 175, comp.runningTime());
	}

	@Test
	public void testConstructorUnderMinimumShowsThrows() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeShow(concert);
		}, "Should throw IllegalArgumentException when less than 2 shows provided");
	}

	@Test
	public void testConstructorOverMaximumShowsThrows() {
		Movie m3 = new Movie("Movie 3", 2020, 100);
		Movie m4 = new Movie("Movie 4", 2021, 100);
		Movie m5 = new Movie("Movie 5", 2022, 100);
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeShow(concert, movie1, movie2, m3, m4, m5);
		}, "Should throw IllegalArgumentException when more than 5 shows provided");
	}

	@Test
	public void testConstructorNullShowThrows() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeShow(concert, null);
		}, "Should throw IllegalArgumentException when a subshow is null");
	}

	@Test
	public void testPolymorphicIterationOnCompositeWithoutInstanceof() {
		Show show = new CompositeShow(concert, movie1, movie2);
		List<Show> subshows = new ArrayList<>();

		// Pure polymorphic for-each loop directly on Show reference!
		for (Show s : show) {
			subshows.add(s);
		}

		assertEquals(3, subshows.size());
		assertSame(concert, subshows.get(0));
		assertSame(movie1, subshows.get(1));
		assertSame(movie2, subshows.get(2));
	}

	@Test
	public void testPolymorphicIterationOnLeafNodeYieldsEmptyIterator() {
		Show leaf = movie1;
		int loopCount = 0;

		// Iterating over a leaf node is valid syntax and executes 0 times safely
		for (Show s : leaf) {
			loopCount++;
		}

		assertEquals(0, loopCount, "Leaf node should return empty iterator with 0 elements");
		assertFalse(leaf.iterator().hasNext(), "Leaf iterator.hasNext() must be false");
	}

	@Test
	public void testIteratorIsUnmodifiable() {
		CompositeShow comp = new CompositeShow(concert, movie1);
		Iterator<Show> it = comp.iterator();
		assertTrue(it.hasNext());
		it.next();
		assertThrows(UnsupportedOperationException.class, it::remove,
				"Calling remove() on CompositeShow iterator must throw UnsupportedOperationException");
	}

	@Test
	public void testNestedCompositePolymorphicUnpacking() {
		CompositeShow inner = new CompositeShow(movie1, movie2);
		Show outer = new CompositeShow(concert, inner);

		assertEquals(45 + 175 + 152, outer.runningTime());

		List<Show> firstLevel = new ArrayList<>();
		for (Show s : outer) {
			firstLevel.add(s);
		}
		assertSame(concert, firstLevel.get(0));
		assertSame(inner, firstLevel.get(1));

		// Iterate over inner show directly via Show interface
		List<Show> secondLevel = new ArrayList<>();
		for (Show s : firstLevel.get(1)) {
			secondLevel.add(s);
		}
		assertEquals(2, secondLevel.size());
		assertSame(movie1, secondLevel.get(0));
		assertSame(movie2, secondLevel.get(1));
	}

	@Test
	public void testPureRecursiveFlatteningWithoutAnyInstanceof() {
		CompositeShow inner = new CompositeShow(movie1, movie2);
		Show root = new CompositeShow(concert, inner);

		List<Show> allLeaves = new ArrayList<>();
		collectLeavesPolymorphically(root, allLeaves);

		assertEquals(3, allLeaves.size());
		assertSame(concert, allLeaves.get(0));
		assertSame(movie1, allLeaves.get(1));
		assertSame(movie2, allLeaves.get(2));
	}

	private void collectLeavesPolymorphically(Show pShow, List<Show> leaves) {
		boolean isParent = false;
		for (Show child : pShow) {
			isParent = true;
			collectLeavesPolymorphically(child, leaves);
		}
		if (!isParent) {
			leaves.add(pShow);
		}
	}

	@Test
	public void testDeepCopy() {
		CompositeShow comp = new CompositeShow(concert, movie1);
		Show copied = comp.copy();

		assertNotNull(copied);
		assertNotSame(comp, copied);
		assertEquals(comp.description(), copied.description());
		assertEquals(comp.runningTime(), copied.runningTime());
		assertEquals(comp, copied);
	}
}

