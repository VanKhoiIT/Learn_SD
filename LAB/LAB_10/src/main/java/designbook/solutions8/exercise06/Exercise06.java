package designbook.solutions8.exercise06;

import designbook.solutions8.common.Day;
import designbook.solutions8.common.IO;
import designbook.solutions8.common.Movie;
import designbook.solutions8.common.Show;

/**
 * Exercise 8.6 Demonstration:
 * Observer Pattern applied to ObservableProgram (Exercise 6.13)
 * using the PUSH data-flow strategy and a single observer interface (ProgramObserver).
 */
public class Exercise06 {

	public static void main(String[] args) {
		IO.println("================================================================================");
		IO.println("        EXERCISE 8.6: OBSERVER PATTERN (PUSH DATA-FLOW & SINGLE INTERFACE)      ");
		IO.println("================================================================================");

		ObservableProgram program = new ObservableProgram();
		ConcreteProgramObserver observer = new ConcreteProgramObserver("AuditLogger");
		program.addObserver(observer);

		Show movie1 = new Movie("Inception", 2010, 148);
		Show movie2 = new Movie("Interstellar", 2014, 169);

		IO.println("\n--- Step 1: Add 'Inception' on MONDAY ---");
		program.add(movie1, Day.MONDAY);

		IO.println("\n--- Step 2: Add 'Interstellar' on FRIDAY ---");
		program.add(movie2, Day.FRIDAY);

		IO.println("\n--- Current Program Schedule ---");
		IO.print(program);

		IO.println("--- Step 3: Remove show on MONDAY ---");
		program.remove(Day.MONDAY);

		IO.println("\n--- Step 4: Clear entire program ---");
		program.clear();

		IO.println("\n--- Verification Summary ---");
		IO.println("Total events recorded by ConcreteProgramObserver: " + observer.getEventLog().size());
		assert observer.getEventLog().size() == 4 : "Expected 4 push notifications";
		assert observer.getClearedCount() == 1 : "Expected 1 programCleared event";
		IO.println("[SUCCESS] Exercise 8.6 completed and verified!");
	}
}

