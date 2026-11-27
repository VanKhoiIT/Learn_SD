package designbook.solutions8.exercise07;

import java.util.ArrayList;
import java.util.List;
import designbook.solutions8.common.Day;
import designbook.solutions8.common.IO;
import designbook.solutions8.common.Show;

/**
 * Exercise 8.7: ConcreteObserver1 implements ONLY ProgramAdditionObserver.
 * When notified via showAdded(Iterable<Show>), it PULLS the current schedule by iterating over the 7 days.
 */
public class ConcreteObserver1 implements ProgramAdditionObserver {

	private int aAdditionNotifications = 0;
	private final List<String> aPulledSnapshots = new ArrayList<>();

	@Override
	public void showAdded(Iterable<Show> pProgram) {
		aAdditionNotifications++;
		String snapshot = pullScheduleSummary(pProgram);
		aPulledSnapshots.add(snapshot);
		IO.println("[ConcreteObserver1 - AdditionOnly (PULL)] showAdded notification #" + aAdditionNotifications
				+ " -> Pulled active shows: " + snapshot);
	}

	public static String pullScheduleSummary(Iterable<Show> pProgram) {
		List<String> active = new ArrayList<>();
		Day[] days = Day.values();
		int idx = 0;
		for (Show show : pProgram) {
			Day day = idx < days.length ? days[idx] : Day.MONDAY;
			if (show.runningTime() > 0) {
				active.add(day.name() + "=" + show.description());
			}
			idx++;
		}
		return active.isEmpty() ? "(none)" : String.join(", ", active);
	}

	public int getAdditionNotifications() {
		return aAdditionNotifications;
	}

	public List<String> getPulledSnapshots() {
		return aPulledSnapshots;
	}
}

