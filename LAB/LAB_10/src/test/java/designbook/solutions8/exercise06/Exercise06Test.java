package designbook.solutions8.exercise06;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import designbook.solutions8.common.Day;
import designbook.solutions8.common.Movie;
import designbook.solutions8.common.Show;

/**
 * Unit Tests for Exercise 8.6 (Observer Pattern - Push Data-Flow & Single Observer Interface).
 */
public class Exercise06Test {

	private ObservableProgram aProgram;
	private ConcreteProgramObserver aObserver;
	private Show aMovie1;
	private Show aMovie2;

	@BeforeEach
	public void setUp() {
		aProgram = new ObservableProgram();
		aObserver = new ConcreteProgramObserver("TestObserver");
		aProgram.addObserver(aObserver);
		aMovie1 = new Movie("Inception", 2010, 148);
		aMovie2 = new Movie("Interstellar", 2014, 169);
	}

	@Test
	public void testPushNotificationOnShowAdded() {
		aProgram.add(aMovie1, Day.MONDAY);

		assertEquals(1, aObserver.getEventLog().size());
		assertEquals(aMovie1, aObserver.getLastAddedShow());
		assertEquals(Day.MONDAY, aObserver.getLastAddedDay());
		assertEquals(aMovie1, aProgram.get(Day.MONDAY));
	}

	@Test
	public void testPushNotificationOnShowRemoved() {
		aProgram.add(aMovie2, Day.FRIDAY);
		aProgram.remove(Day.FRIDAY);

		assertEquals(2, aObserver.getEventLog().size());
		assertEquals(aMovie2, aObserver.getLastRemovedShow());
		assertEquals(Day.FRIDAY, aObserver.getLastRemovedDay());
		assertTrue(aProgram.isNullShow(aProgram.get(Day.FRIDAY)));
	}

	@Test
	public void testNotificationOnProgramCleared() {
		aProgram.add(aMovie1, Day.MONDAY);
		aProgram.add(aMovie2, Day.WEDNESDAY);
		aProgram.clear();

		assertEquals(3, aObserver.getEventLog().size());
		assertEquals(1, aObserver.getClearedCount());
		assertTrue(aProgram.isNullShow(aProgram.get(Day.MONDAY)));
		assertTrue(aProgram.isNullShow(aProgram.get(Day.WEDNESDAY)));
	}

	@Test
	public void testMultipleObserversReceivePushEvents() {
		ConcreteProgramObserver secondObserver = new ConcreteProgramObserver("SecondObserver");
		aProgram.addObserver(secondObserver);

		aProgram.add(aMovie1, Day.TUESDAY);
		assertEquals(1, aObserver.getEventLog().size());
		assertEquals(1, secondObserver.getEventLog().size());
		assertEquals(Day.TUESDAY, secondObserver.getLastAddedDay());
	}
}

