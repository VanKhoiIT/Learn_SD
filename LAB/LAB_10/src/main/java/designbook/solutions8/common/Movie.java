package designbook.solutions8.common;

import java.util.Objects;

/**
 * Concrete Show representing a movie in a theater program.
 */
public class Movie implements Show {

	private final String aTitle;
	private final int aYear;
	private final int aRunningTime;

	public Movie(String pTitle, int pYear, int pTime) {
		assert pTitle != null && pTime >= 0;
		aTitle = pTitle;
		aYear = pYear;
		aRunningTime = pTime;
	}

	public Movie(Movie pMovie) {
		this(pMovie.aTitle, pMovie.aYear, pMovie.aRunningTime);
	}

	public String title() {
		return aTitle;
	}

	public int year() {
		return aYear;
	}

	@Override
	public String description() {
		return String.format("%s (%d) [%d min]", aTitle, aYear, aRunningTime);
	}

	@Override
	public int runningTime() {
		return aRunningTime;
	}

	@Override
	public Show copy() {
		return new Movie(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(aRunningTime, aTitle, aYear);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return aRunningTime == other.aRunningTime
				&& Objects.equals(aTitle, other.aTitle)
				&& aYear == other.aYear;
	}

	@Override
	public String toString() {
		return description();
	}
}

