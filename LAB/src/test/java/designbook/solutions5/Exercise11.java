package designbook.solutions5;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import e3.chapter4.Card;
import e3.chapter4.CardSource;
import e3.chapter4.Rank;
import e3.chapter4.Suit;
import e3.chapter5.BiCardSource;

/**
 * Exercise 11: Write a test suite for all the public methods of class BiCardSource
 * to achieve 100% branch coverage, using stubs to help isolate the behavior to test.
 * 
 * Adapted strictly to 3rd Edition (code-e3) packages:
 * - e3.chapter4.CardSource
 * - e3.chapter4.Card (uses Card.get(Rank, Suit))
 * - e3.chapter4.Rank
 * - e3.chapter4.Suit
 * - e3.chapter5.BiCardSource
 */
public class Exercise11 {

	/**
	 * Test Stub isolating the CardSource behavior.
	 * Can hold an optional card or be empty.
	 */
	public static class CardSourceStub implements CardSource {

		private Optional<Card> aCard = Optional.empty();

		public CardSourceStub() {}

		public CardSourceStub(Card pCard) {
			aCard = Optional.of(pCard);
		}

		@Override
		public Card draw() {
			assert !isEmpty() : "Cannot draw from an empty CardSourceStub";
			Card card = aCard.get();
			aCard = Optional.empty();
			return card;
		}

		@Override
		public boolean isEmpty() {
			return !aCard.isPresent();
		}
	}

	private static final Card ACE = Card.get(Rank.ACE, Suit.CLUBS);
	private static final Card TWO = Card.get(Rank.TWO, Suit.CLUBS);

	private CardSource aEmpty = new CardSourceStub();
	private CardSource aContainsAce = new CardSourceStub(ACE);
	private CardSource aContainsTwo = new CardSourceStub(TWO);

	@BeforeEach
	public void setUp() {
		aEmpty = new CardSourceStub();
		aContainsAce = new CardSourceStub(ACE);
		aContainsTwo = new CardSourceStub(TWO);
	}

	@Test
	@DisplayName("testEmpty_True: both sources empty -> isEmpty() returns true")
	public void testEmpty_True() {
		assertTrue(new BiCardSource(aEmpty, aEmpty).isEmpty());
	}

	@Test
	@DisplayName("testEmpty_False_FirstEmpty: source 1 empty, source 2 has Ace -> isEmpty() returns false")
	public void testEmpty_False_FirstEmpty() {
		assertFalse(new BiCardSource(aEmpty, aContainsAce).isEmpty());
	}

	@Test
	@DisplayName("testEmpty_False_SecondEmpty: source 1 has Ace, source 2 empty -> isEmpty() returns false")
	public void testEmpty_False_SecondEmpty() {
		assertFalse(new BiCardSource(aContainsAce, aEmpty).isEmpty());
	}

	@Test
	@DisplayName("testEmpty_False_NeitherEmpty: source 1 has Ace, source 2 has Two -> isEmpty() returns false")
	public void testEmpty_False_NeitherEmpty() {
		assertFalse(new BiCardSource(aContainsAce, aContainsTwo).isEmpty());
	}

	@Test
	@DisplayName("testDraw_FirstEmpty: source 1 empty, source 2 has Ace -> draw() returns Ace from source 2")
	public void testDraw_FirstEmpty() {
		assertSame(ACE, new BiCardSource(aEmpty, aContainsAce).draw());
	}

	@Test
	@DisplayName("testDraw_SecondEmpty: source 1 has Ace, source 2 empty -> draw() returns Ace from source 1")
	public void testDraw_SecondEmpty() {
		assertSame(ACE, new BiCardSource(aContainsAce, aEmpty).draw());
	}

	@Test
	@DisplayName("testDraw_NeitherEmpty: source 1 has Ace, source 2 has Two -> draw() returns Ace from source 1")
	public void testDraw_NeitherEmpty() {
		assertSame(ACE, new BiCardSource(aContainsAce, aContainsTwo).draw());
	}

	@Test
	@DisplayName("testIsNull: verifies isNull() returns false for BiCardSource")
	public void testIsNull() {
		assertFalse(new BiCardSource(aEmpty, aEmpty).isNull());
	}

	@Test
	@DisplayName("testDraw_SequentialTransition: draws first card from source 1, then second card from source 2")
	public void testDraw_SequentialTransition() {
		BiCardSource biSource = new BiCardSource(aContainsAce, aContainsTwo);
		assertFalse(biSource.isEmpty());
		assertSame(ACE, biSource.draw());
		assertFalse(biSource.isEmpty());
		assertSame(TWO, biSource.draw());
		assertTrue(biSource.isEmpty());
	}
}
