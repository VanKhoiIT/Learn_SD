package designbook.solutions8.exercise07;

import java.util.ArrayList;
import java.util.List;
import designbook.solutions8.common.IO;
import designbook.solutions8.common.Show;

/**
 * Exercise 8.7: ConcreteObserver2 implements ONLY ProgramRemovalObserver.
 * It only receives showRemoved(Iterable<Show>) and programCleared() notifications.
 */
public class ConcreteObserver2 implements ProgramRemovalObserver {

	private int aRemovalNotifications = 0;
	private int aClearedNotifications = 0;
	private final List<String> aPulledSnapshots = new ArrayList<>();

	@Override
	public void showRemoved(Iterable<Show> pProgram) {
		aRemovalNotifications++;
		String snapshot = ConcreteObserver1.pullScheduleSummary(pProgram);
		aPulledSnapshots.add(snapshot);
		IO.println("[ConcreteObserver2 - RemovalOnly (PULL)] showRemoved notification #" + aRemovalNotifications
				+ " -> Remaining active shows: " + snapshot);
	}

	@Override
	public void programCleared() {
		aClearedNotifications++;
		IO.println("[ConcreteObserver2 - RemovalOnly (PULL)] programCleared notification #" + aClearedNotifications);
	}

	public int getRemovalNotifications() {
		return aRemovalNotifications;
	}

	public int getClearedNotifications() {
		return aClearedNotifications;
	}

	public List<String> getPulledSnapshots() {
		return aPulledSnapshots;
	}
}

