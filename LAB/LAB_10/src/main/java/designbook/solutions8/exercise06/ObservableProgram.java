package designbook.solutions8.exercise06;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import designbook.solutions8.common.Day;
import designbook.solutions8.common.Show;

/**
 * Exercise 8.6: Observable version of Program (from Exercise 6.13) using
 * the PUSH data-flow strategy and a single observer interface (ProgramObserver).
 */
public class ObservableProgram {

	public static final Show NULL_SHOW = createNullShow();

	private static Show createNullShow() {
		return new Show() {
			@Override
			public String description() { return "[No Show]"; }
			@Override
			public int runningTime() { return 0; }
			@Override
			public Show copy() { return this; }
			@Override
			public int hashCode() { return 0; }
			@Override
			public boolean equals(Object pObject) {
				return pObject != null && pObject.getClass() == this.getClass();
			}
			@Override
			public String toString() { return "[NULL_SHOW]"; }
		};
	}

	private final EnumMap<Day, Show> aShows = new EnumMap<>(Day.class);
	private final List<ProgramObserver> aObservers = new ArrayList<>();

	public ObservableProgram() {
		initNullShows();
	}

	private void initNullShows() {
		for (Day day : Day.values()) {
			aShows.put(day, NULL_SHOW);
		}
	}

	/**
	 * Registers a new observer to receive push notifications.
	 *
	 * @param pObserver The observer to register (must not be null).
	 */
	public void addObserver(ProgramObserver pObserver) {
		assert pObserver != null : "Observer cannot be null";
		aObservers.add(pObserver);
	}

	/**
	 * Unregisters an existing observer.
	 *
	 * @param pObserver The observer to remove.
	 */
	public void removeObserver(ProgramObserver pObserver) {
		aObservers.remove(pObserver);
	}

	/**
	 * Clears the program by resetting all days to the null show and notifies observers.
	 */
	public void clear() {
		initNullShows();
		for (ProgramObserver observer : aObservers) {
			observer.programCleared();
		}
	}

	/**
	 * Adds a new show to the program and pushes (pShow, pDay) to all observers.
	 *
	 * @param pShow The show to add.
	 * @param pDay  The day when the show takes place.
	 */
	public void add(Show pShow, Day pDay) {
		assert pShow != null && pDay != null;
		aShows.put(pDay, pShow.copy());
		for (ProgramObserver observer : aObservers) {
			observer.showAdded(pShow, pDay);
		}
	}

	/**
	 * Removes a show from the program for pDay, replaces it with NULL_SHOW,
	 * and pushes the removed show and day to all observers.
	 *
	 * @param pDay The day when we want to zap the show.
	 */
	public void remove(Day pDay) {
		assert pDay != null;
		Show removed = aShows.put(pDay, NULL_SHOW);
		for (ProgramObserver observer : aObservers) {
			observer.showRemoved(removed, pDay);
		}
	}

	/**
	 * @param pDay The day of the requested show.
	 * @return A copy of the show on a given day.
	 */
	public Show get(Day pDay) {
		assert pDay != null;
		return aShows.get(pDay).copy();
	}

	public boolean isNullShow(Show pShow) {
		return NULL_SHOW.equals(pShow) || pShow == NULL_SHOW;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Day day : Day.values()) {
			result.append(String.format("%9s: %s%n", day.name(), aShows.get(day).description()));
		}
		return result.toString();
	}
}

