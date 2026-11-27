package designbook.solutions8.exercise06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import designbook.solutions8.common.Day;
import designbook.solutions8.common.IO;
import designbook.solutions8.common.Show;

/**
 * Exercise 8.6: Dummy concrete observer class to test the application of the Observer pattern
 * with the push data-flow strategy.
 */
public class ConcreteProgramObserver implements ProgramObserver {

	private final String aName;
	private final List<String> aEventLog = new ArrayList<>();
	private Show aLastAddedShow;
	private Day aLastAddedDay;
	private Show aLastRemovedShow;
	private Day aLastRemovedDay;
	private int aClearedCount = 0;

	public ConcreteProgramObserver(String pName) {
		assert pName != null;
		this.aName = pName;
	}

	public ConcreteProgramObserver() {
		this("ConcreteProgramObserver");
	}

	@Override
	public void showAdded(Show pShow, Day pDay) {
		aLastAddedShow = pShow;
		aLastAddedDay = pDay;
		String msg = String.format("[%s - PUSH] showAdded -> Day: %s, Show: %s",
				aName, pDay, pShow.description());
		aEventLog.add(msg);
		IO.println(msg);
	}

	@Override
	public void showRemoved(Show pShow, Day pDay) {
		aLastRemovedShow = pShow;
		aLastRemovedDay = pDay;
		String msg = String.format("[%s - PUSH] showRemoved -> Day: %s, Removed Show: %s",
				aName, pDay, pShow.description());
		aEventLog.add(msg);
		IO.println(msg);
	}

	@Override
	public void programCleared() {
		aClearedCount++;
		String msg = String.format("[%s - PUSH] programCleared -> All shows cleared (count=%d)",
				aName, aClearedCount);
		aEventLog.add(msg);
		IO.println(msg);
	}

	public List<String> getEventLog() {
		return Collections.unmodifiableList(aEventLog);
	}

	public Show getLastAddedShow() {
		return aLastAddedShow;
	}

	public Day getLastAddedDay() {
		return aLastAddedDay;
	}

	public Show getLastRemovedShow() {
		return aLastRemovedShow;
	}

	public Day getLastRemovedDay() {
		return aLastRemovedDay;
	}

	public int getClearedCount() {
		return aClearedCount;
	}
}

