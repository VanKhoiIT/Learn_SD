package designbook.solutions8.common;

/**
 * Base interface representing a performance (from Chapter 6 / Exercise 6.13).
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

