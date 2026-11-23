package designbook.chapter8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Models a deck of 52 playing cards.
 * Provides basic operations such as shuffle, push, draw, and state inspection.
 */
public class Deck {

	private final List<Card> aCards = new ArrayList<>();

	/**
	 * Creates a new deck of 52 cards, shuffled.
	 */
	public Deck() {
		shuffle();
	}

	/**
	 * Reinitializes the deck with all 52 cards, and shuffles them.
	 */
	public void shuffle() {
		aCards.clear();
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				aCards.add(Card.get(rank, suit));
			}
		}
		Collections.shuffle(aCards);
	}

	/**
	 * Places pCard on top of the deck.
	 * 
	 * @param pCard The card to place on top of the deck.
	 * @pre pCard != null
	 */
	public void push(Card pCard) {
		assert pCard != null;
		aCards.add(pCard);
	}

	/**
	 * Draws a card from the deck: removes the card from the top of the deck and returns it.
	 * 
	 * @return The card drawn.
	 * @pre !isEmpty()
	 */
	public Card draw() {
		assert !isEmpty();
		return aCards.remove(aCards.size() - 1);
	}

	/**
	 * @return True if and only if there are no cards in the deck.
	 */
	public boolean isEmpty() {
		return aCards.isEmpty();
	}

	/**
	 * @return An unmodifiable list of all the cards in the deck.
	 */
	public List<Card> getCards() {
		return Collections.unmodifiableList(aCards);
	}
}

