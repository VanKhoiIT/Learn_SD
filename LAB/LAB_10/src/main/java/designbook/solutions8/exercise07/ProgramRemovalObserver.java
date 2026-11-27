package designbook.solutions8.exercise07;

import designbook.solutions8.common.Show;

/**
 * Exercise 8.7: Observer interface strictly for clearing and removing shows from the program,
 * using the PULL data-flow strategy and the Interface Segregation Principle (ISP).
 *
 * Matches the official "Solution to Exercise 7" class diagram:
 * - showRemoved(Iterable<Show>): void
 * - programCleared(): void
 */
public interface ProgramRemovalObserver {

	/**
	 * Callback invoked when a show is removed from the program.
	 *
	 * @param pProgram Read-only Iterable<Show> view of the subject from which the observer pulls state.
	 */
	void showRemoved(Iterable<Show> pProgram);

	/**
	 * Callback invoked when the program is cleared.
	 */
	void programCleared();
}

