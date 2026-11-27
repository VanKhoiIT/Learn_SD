package designbook.solutions8.exercise07;

import designbook.solutions8.common.Show;

/**
 * Exercise 8.7: Observer interface strictly for addition events, using the PULL data-flow strategy
 * and the Interface Segregation Principle (ISP).
 *
 * Notice that the parameter type is Iterable<Show> rather than ObservableProgram:
 * - Observers can pull/iterate through all shows across the 7 days of the week in a predictable order.
 * - Observers CANNOT invoke mutating methods (add, remove, clear) on the Subject!
 */
public interface ProgramAdditionObserver {

	/**
	 * Callback invoked when a show is added to the program.
	 *
	 * @param pProgram Read-only Iterable<Show> view of the subject from which the observer pulls state.
	 */
	void showAdded(Iterable<Show> pProgram);
}

