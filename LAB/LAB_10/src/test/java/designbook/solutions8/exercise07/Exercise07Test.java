package designbook.solutions8.exercise07;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import designbook.solutions8.common.Day;
import designbook.solutions8.common.Movie;
import designbook.solutions8.common.Show;

/**
 * Unit Tests for Exercise 8.7 (Observer Pattern - Pull Data-Flow & Segregated Interfaces with ISP).
 */
public class Exercise07Test {

	private ObservableProgram aProgram;
	private ConcreteObserver1 aObs1;
	private ConcreteObserver2 aObs2;
	private ConcreteObserver3 aObs3;
	private Show aMovie1;
	private Show aMovie2;

	@BeforeEach
	public void setUp() {
		aProgram = new ObservableProgram();
		aObs1 = new ConcreteObserver1();
		aObs2 = new ConcreteObserver2();
		aObs3 = new ConcreteObserver3();

		aProgram.addObserver(aObs1);
		aProgram.addObserver(aObs2);
		aProgram.addObserver((ProgramAdditionObserver) aObs3);
		aProgram.addObserver((ProgramRemovalObserver) aObs3);

		aMovie1 = new Movie("The Matrix", 1999, 136);
		aMovie2 = new Movie("The Godfather", 1972, 175);
	}

	@Test
	public void testAdditionObserverOnlyNotifiedOnAdd() {
		aProgram.add(aMovie1, Day.MONDAY);
		aProgram.remove(Day.MONDAY);
		aProgram.clear();

		// ConcreteObserver1 only listens to addition events
		assertEquals(1, aObs1.getAdditionNotifications());
	}

	@Test
	public void testRemovalObserverOnlyNotifiedOnRemoveAndClear() {
		aProgram.add(aMovie1, Day.MONDAY);
		aProgram.add(aMovie2, Day.FRIDAY);
		aProgram.remove(Day.MONDAY);
		aProgram.clear();

		// ConcreteObserver2 only listens to removal and clear events
		assertEquals(1, aObs2.getRemovalNotifications());
		assertEquals(1, aObs2.getClearedNotifications());
	}

	@Test
	public void testCombinedObserverPullsTotalRunningTime() {
		aProgram.add(aMovie1, Day.MONDAY); // 136 min
		assertEquals(136, aObs3.getLastTotalRunningTime());

		aProgram.add(aMovie2, Day.WEDNESDAY); // 136 + 175 = 311 min
		assertEquals(311, aObs3.getLastTotalRunningTime());

		aProgram.remove(Day.MONDAY); // 175 min remaining
		assertEquals(175, aObs3.getLastTotalRunningTime());

		aProgram.clear(); // 0 min
		assertEquals(0, aObs3.getLastTotalRunningTime());
	}

	@Test
	public void testIterableCoversSevenDaysInPredictableOrder() {
		aProgram.add(aMovie1, Day.MONDAY);
		aProgram.add(aMovie2, Day.SUNDAY);

		List<Show> iteratedShows = new ArrayList<>();
		for (Show show : aProgram) {
			iteratedShows.add(show);
		}

		assertEquals(7, iteratedShows.size(), "Iterator must cover all 7 days of the week");
		assertEquals(aMovie1, iteratedShows.get(0), "First element must be MONDAY");
		assertEquals(aMovie2, iteratedShows.get(6), "Seventh element must be SUNDAY");
	}

	@Test
	public void testInterfaceSegregationPrincipleEnforcedOnObserverParameter() throws Exception {
		// Verify that ProgramAdditionObserver.showAdded parameter is strictly Iterable,
		// which does NOT expose mutating methods like add, remove, or clear.
		Method showAddedMethod = ProgramAdditionObserver.class.getMethod("showAdded", Iterable.class);
		Class<?> paramType = showAddedMethod.getParameterTypes()[0];
		assertEquals(Iterable.class, paramType);

		for (Method m : paramType.getMethods()) {
			assertFalse(m.getName().equals("add") || m.getName().equals("remove") || m.getName().equals("clear"),
					"Observer parameter type must not expose mutating methods");
		}
	}
}

