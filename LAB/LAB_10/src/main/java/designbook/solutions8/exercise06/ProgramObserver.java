package designbook.solutions8.exercise06;

import designbook.solutions8.common.Day;
import designbook.solutions8.common.Show;

/**
 * Exercise 8.6: Single observer interface using the PUSH data-flow strategy.
 * Matches the official "Solution to Exercise 6" class diagram:
 * - showAdded(Show, Day): void
 * - showRemoved(Show, Day): void
 * - programCleared(): void
 */
public interface ProgramObserver {

	/**
	 * Callback invoked when a show is added to the program on a specific day.
	 *
	 * @param pShow The show that was added (pushed directly to the observer).
	 * @param pDay  The day on which the show was scheduled.
	 */
	void showAdded(Show pShow, Day pDay);

	/**
	 * Callback invoked when a show is removed from a specific day in the program.
	 *
	 * @param pShow The show that was removed (pushed directly to the observer).
	 * @param pDay  The day from which the show was removed.
	 */
	void showRemoved(Show pShow, Day pDay);

	/**
	 * Callback invoked when all shows in the program are cleared.
	 */
	void programCleared();
}

