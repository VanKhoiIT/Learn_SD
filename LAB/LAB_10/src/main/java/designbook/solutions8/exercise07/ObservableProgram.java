package designbook.solutions8.exercise07;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import designbook.solutions8.common.Day;
import designbook.solutions8.common.Show;

/**
 * Exercise 8.7: ObservableProgram refactored to use:
 * 1. The PULL data-flow strategy (passing 'this' typed as Iterable<Show>).
 * 2. Two segregated observer interfaces:
 *    - ProgramAdditionObserver (for show additions)
 *    - ProgramRemovalObserver (for show removals and program clearing)
 * 3. Interface Segregation Principle (ISP): ObservableProgram implements Iterable<Show>,
 *    and callbacks receive Iterable<Show> so observers cannot call mutating methods
 *    (add, remove, clear) on the subject.
 */
public class ObservableProgram implements Iterable<Show> {

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
	private final List<ProgramAdditionObserver> aAdditionObservers = new ArrayList<>();
	private final List<ProgramRemovalObserver> aRemovalObservers = new ArrayList<>();

	public ObservableProgram() {
		initNullShows();
	}

	private void initNullShows() {
		for (Day day : Day.values()) {
			aShows.put(day, NULL_SHOW);
		}
	}

	/**
	 * Registers an observer interested only in show addition events.
	 *
	 * @param pObserver The ProgramAdditionObserver to add.
	 */
	public void addObserver(ProgramAdditionObserver pObserver) {
		assert pObserver != null : "Addition observer cannot be null";
		aAdditionObservers.add(pObserver);
	}

	/**
	 * Registers an observer interested only in show removal and program clear events.
	 *
	 * @param pObserver The ProgramRemovalObserver to add.
	 */
	public void addObserver(ProgramRemovalObserver pObserver) {
		assert pObserver != null : "Removal observer cannot be null";
		aRemovalObservers.add(pObserver);
	}

	/**
	 * Unregisters an addition observer.
	 */
	public void removeObserver(ProgramAdditionObserver pObserver) {
		aAdditionObservers.remove(pObserver);
	}

	/**
	 * Unregisters a removal observer.
	 */
	public void removeObserver(ProgramRemovalObserver pObserver) {
		aRemovalObservers.remove(pObserver);
	}

	/**
	 * Clears all shows by resetting every day to NULL_SHOW and notifies removal observers.
	 */
	public void clear() {
		initNullShows();
		for (ProgramRemovalObserver observer : aRemovalObservers) {
			observer.programCleared();
		}
	}

	/**
	 * Adds a show on a specific day and notifies addition observers using PULL (passing 'this' as Iterable<Show>).
	 *
	 * @param pShow The show to add.
	 * @param pDay  The day on which to schedule the show.
	 */
	public void add(Show pShow, Day pDay) {
		assert pShow != null && pDay != null;
		aShows.put(pDay, pShow.copy());
		for (ProgramAdditionObserver observer : aAdditionObservers) {
			observer.showAdded(this);
		}
	}

	/**
	 * Convenience overload matching the diagram signature add(Show) which schedules on MONDAY by default
	 * or first available day.
	 *
	 * @param pShow The show to add.
	 */
	public void add(Show pShow) {
		assert pShow != null;
		Day targetDay = Day.MONDAY;
		for (Day day : Day.values()) {
			if (isNullShow(aShows.get(day))) {
				targetDay = day;
				break;
			}
		}
		add(pShow, targetDay);
	}

	/**
	 * Removes the show on pDay (replaces with NULL_SHOW) and notifies removal observers
	 * using PULL (passing 'this' as Iterable<Show>).
	 *
	 * @param pDay The day to remove the show from.
	 */
	public void remove(Day pDay) {
		assert pDay != null;
		aShows.put(pDay, NULL_SHOW);
		for (ProgramRemovalObserver observer : aRemovalObservers) {
			observer.showRemoved(this);
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

	/**
	 * Iterates over all shows, covering the seven days of the week in a predictable order (MONDAY -> SUNDAY).
	 * Returns deep copies of each Show so observers cannot mutate internal state.
	 */
	@Override
	public Iterator<Show> iterator() {
		List<Show> snapshot = new ArrayList<>(Day.values().length);
		for (Day day : Day.values()) {
			snapshot.add(aShows.get(day).copy());
		}
		return snapshot.iterator();
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

