package designbook.solutions5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import e3.chapter4.Card;
import e3.chapter4.CardSource;
import e3.chapter4.Rank;
import e3.chapter4.Suit;
import e3.chapter5.BiCardSourceBuggy;

/**
 * Demonstrates the seeded bug in the original textbook code of BiCardSource.
 * Exercise 5.11 explicitly notes:
 * "This solution will reveal a seeded bug. Once you fix the bug, the suite should achieve complete branch coverage."
 */
public class Exercise11BugDemonstrator {

	private static final Card ACE = Card.get(Rank.ACE, Suit.CLUBS);
	private static final Card TWO = Card.get(Rank.TWO, Suit.CLUBS);

	private CardSource aEmpty;
	private CardSource aContainsAce;
	private CardSource aContainsTwo;

	@BeforeEach
	public void setUp() {
		aEmpty = new Exercise11.CardSourceStub();
		aContainsAce = new Exercise11.CardSourceStub(ACE);
		aContainsTwo = new Exercise11.CardSourceStub(TWO);
	}

	@Test
	@DisplayName("BugDemo: isEmpty() works correctly even in buggy version")
	public void demonstrateEmptyWorks() {
		assertTrue(new BiCardSourceBuggy(aEmpty, aEmpty).isEmpty());
		assertFalse(new BiCardSourceBuggy(aEmpty, aContainsAce).isEmpty());
		assertFalse(new BiCardSourceBuggy(aContainsAce, aEmpty).isEmpty());
		assertFalse(new BiCardSourceBuggy(aContainsAce, aContainsTwo).isEmpty());
	}

	@Test
	@DisplayName("BugDemo 1: testDraw_FirstEmpty FAILS because it tries to draw from empty source 1")
	public void demonstrateBugInDraw_FirstEmpty() {
		BiCardSourceBuggy buggy = new BiCardSourceBuggy(aEmpty, aContainsAce);
		try {
			buggy.draw();
			fail("Expected AssertionError or Exception because draw() erroneously called empty aSource1.draw()");
		}
		catch (Throwable t) {
			// Bug confirmed!
			System.out.println("  [CONFIRMED SEEDED BUG 1] testDraw_FirstEmpty threw: " + t.getClass().getSimpleName());
		}
	}

	@Test
	@DisplayName("BugDemo 2: testDraw_SecondEmpty FAILS because it tries to draw from empty source 2")
	public void demonstrateBugInDraw_SecondEmpty() {
		BiCardSourceBuggy buggy = new BiCardSourceBuggy(aContainsAce, aEmpty);
		try {
			buggy.draw();
			fail("Expected AssertionError or Exception because draw() erroneously called empty aSource2.draw()");
		}
		catch (Throwable t) {
			// Bug confirmed!
			System.out.println("  [CONFIRMED SEEDED BUG 2] testDraw_SecondEmpty threw: " + t.getClass().getSimpleName());
		}
	}

	@Test
	@DisplayName("BugDemo 3: testDraw_NeitherEmpty FAILS because it returned TWO from source 2 instead of ACE from source 1")
	public void demonstrateBugInDraw_NeitherEmpty() {
		BiCardSourceBuggy buggy = new BiCardSourceBuggy(aContainsAce, aContainsTwo);
		Card drawn = buggy.draw();
		// In buggy version, drawn is TWO instead of ACE!
		assertNotSame(ACE, drawn, "Seeded bug caused draw() to pull from source 2 instead of source 1!");
		assertEquals(TWO, drawn);
		System.out.println("  [CONFIRMED SEEDED BUG 3] testDraw_NeitherEmpty returned: " + drawn + " instead of ACE of CLUBS!");
	}
}
