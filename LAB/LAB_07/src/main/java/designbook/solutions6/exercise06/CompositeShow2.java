package designbook.solutions6.exercise06;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Exercise 6.6 Solution (Safety Approach):
 * Implements Iterable<Show> on CompositeShow2 WITHOUT modifying Show interface.
 * Returns an unmodifiable iterator to prevent illegal mutation.
 */
public class CompositeShow2 implements Show, Iterable<Show> {

	private final List<Show> aShows = new ArrayList<>();

	public CompositeShow2(Show... pShows) {
		if (pShows.length < 2 || pShows.length > 5) {
			throw new IllegalArgumentException("Arguments should be between two and five shows inclusively");
		}
		for (Show show : pShows) {
			if (show == null) {
				throw new IllegalArgumentException("Subshow cannot be null");
			}
		}
		aShows.addAll(Arrays.asList(pShows));
	}

	@Override
	public Iterator<Show> iterator() {
		return Collections.unmodifiableList(aShows).iterator();
	}

	@Override
	public String description() {
		StringJoiner description = new StringJoiner("; ", "[", "]");
		for (Show show : aShows) {
			description.add(show.description());
		}
		return description.toString();
	}

	@Override
	public int runningTime() {
		int time = 0;
		for (Show show : aShows) {
			time += show.runningTime();
		}
		return time;
	}

	@Override
	public Show copy() {
		List<Show> copies = new ArrayList<>();
		for (Show show : aShows) {
			copies.add(show.copy());
		}
		return new CompositeShow2(copies.toArray(new Show[0]));
	}

	public int size() {
		return aShows.size();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CompositeShow2 other = (CompositeShow2) obj;
		return Objects.equals(aShows, other.aShows);
	}

	@Override
	public int hashCode() {
		return Objects.hash(aShows);
	}

	@Override
	public String toString() {
		return description();
	}
}

