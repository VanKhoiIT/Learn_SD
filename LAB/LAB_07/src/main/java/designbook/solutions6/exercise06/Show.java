package designbook.solutions6.exercise06;

/**
 * Exercise 6.6 - Baseline Interface Show:
 * Represents a performance in a theater.
 * Unchanged according to requirement: "in a way that does NOT change the Show interface".
 */
public interface Show {

	/**
	 * @return A description of the show.
	 */
	String description();

	/**
	 * @return The running time of the show, in minutes.
	 */
	int runningTime();

	/**
	 * @return A deep copy of the show.
	 */
	Show copy();
}

