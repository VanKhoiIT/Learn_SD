package designbook.solutions8.exercise07;

import designbook.solutions8.common.IO;
import designbook.solutions8.common.Show;

/**
 * Exercise 8.7: ConcreteObserver3 implements BOTH ProgramAdditionObserver and ProgramRemovalObserver.
 * Demonstrates how the segregated interfaces still allow an observer to subscribe to all event types
 * if desired, while receiving only the read-only Iterable<Show> view of the subject.
 */
public class ConcreteObserver3 implements ProgramAdditionObserver, ProgramRemovalObserver {

	private int aAddedCount = 0;
	private int aRemovedCount = 0;
	private int aClearedCount = 0;
	private int aLastTotalRunningTime = 0;

	@Override
	public void showAdded(Iterable<Show> pProgram) {
		aAddedCount++;
		aLastTotalRunningTime = calculateTotalRunningTime(pProgram);
		IO.println("[ConcreteObserver3 - Combined (PULL)] showAdded -> Pulled total running time: "
				+ aLastTotalRunningTime + " min");
	}

	@Override
	public void showRemoved(Iterable<Show> pProgram) {
		aRemovedCount++;
		aLastTotalRunningTime = calculateTotalRunningTime(pProgram);
		IO.println("[ConcreteObserver3 - Combined (PULL)] showRemoved -> Pulled total running time: "
				+ aLastTotalRunningTime + " min");
	}

	@Override
	public void programCleared() {
		aClearedCount++;
		aLastTotalRunningTime = 0;
		IO.println("[ConcreteObserver3 - Combined (PULL)] programCleared -> Total running time reset to 0 min");
	}

	private int calculateTotalRunningTime(Iterable<Show> pProgram) {
		int total = 0;
		for (Show show : pProgram) {
			total += show.runningTime();
		}
		return total;
	}

	public int getAddedCount() {
		return aAddedCount;
	}

	public int getRemovedCount() {
		return aRemovedCount;
	}

	public int getClearedCount() {
		return aClearedCount;
	}

	public int getLastTotalRunningTime() {
		return aLastTotalRunningTime;
	}
}

