package designbook.solutions8.exercise07;

import designbook.solutions8.common.Day;
import designbook.solutions8.common.IO;
import designbook.solutions8.common.Movie;
import designbook.solutions8.common.Show;

/**
 * Exercise 8.7 Demonstration:
 * Observer Pattern using PULL data-flow strategy, two segregated observer interfaces
 * (ProgramAdditionObserver, ProgramRemovalObserver), and the Interface Segregation Principle
 * via Iterable<Show>.
 */
public class Exercise07 {

	public static void main(String[] args) {
		IO.println("================================================================================");
		IO.println("   EXERCISE 8.7: OBSERVER PATTERN (PULL DATA-FLOW & INTERFACE SEGREGATION)      ");
		IO.println("================================================================================");

		ObservableProgram program = new ObservableProgram();

		// 1. ConcreteObserver1: listens only to addition events
		ConcreteObserver1 obs1 = new ConcreteObserver1();
		program.addObserver(obs1);

		// 2. ConcreteObserver2: listens only to removal and clear events
		ConcreteObserver2 obs2 = new ConcreteObserver2();
		program.addObserver(obs2);

		// 3. ConcreteObserver3: listens to both addition and removal/clear events
		ConcreteObserver3 obs3 = new ConcreteObserver3();
		program.addObserver((ProgramAdditionObserver) obs3);
		program.addObserver((ProgramRemovalObserver) obs3);

		Show movie1 = new Movie("The Matrix", 1999, 136);
		Show movie2 = new Movie("The Godfather", 1972, 175);

		IO.println("\n--- Step 1: Add 'The Matrix' on MONDAY ---");
		program.add(movie1, Day.MONDAY);

		IO.println("\n--- Step 2: Add 'The Godfather' on WEDNESDAY ---");
		program.add(movie2, Day.WEDNESDAY);

		IO.println("\n--- Step 3: Remove show on MONDAY ---");
		program.remove(Day.MONDAY);

		IO.println("\n--- Step 4: Clear entire program ---");
		program.clear();

		IO.println("\n--- Verification Summary ---");
		IO.println("ConcreteObserver1 (Addition only) notifications: " + obs1.getAdditionNotifications());
		IO.println("ConcreteObserver2 (Removal only) removals=" + obs2.getRemovalNotifications()
				+ ", clears=" + obs2.getClearedNotifications());
		IO.println("ConcreteObserver3 (Both) additions=" + obs3.getAddedCount()
				+ ", removals=" + obs3.getRemovedCount()
				+ ", clears=" + obs3.getClearedCount());

		assert obs1.getAdditionNotifications() == 2;
		assert obs2.getRemovalNotifications() == 1 && obs2.getClearedNotifications() == 1;
		assert obs3.getAddedCount() == 2 && obs3.getRemovedCount() == 1 && obs3.getClearedCount() == 1;
		IO.println("[SUCCESS] Exercise 8.7 completed and verified!");
	}
}

