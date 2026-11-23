package designbook.solutions8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import designbook.chapter8.Card;
import designbook.chapter8.Rank;
import designbook.chapter8.Suit;

public class Exercise01 {

	public static void main(String[] args) {
		ObservableDeck1 deck = new ObservableDeck1();
		deck.addObserver(new DrawLogger1());
		deck.addObserver(new SizeStatus1());
		deck.shuffle();
		deck.draw();
		deck.draw();
	}
}

interface DeckObserver1 {

	void shuffled();
	void cardDrawn(Card pCard);
	void cardPushed(Card pCard);
}

/**
 * Represents a deck of playing cards. In this version, the cards in the deck
 * are stored in a list and the list of cards in the deck can be obtained by
 * client code using an immutable wrapper object.
 */
class ObservableDeck1 {

	private final List<Card> aCards = new ArrayList<>();
	private final List<DeckObserver1> aObservers = new ArrayList<>();

	/**
	 * Creates a new deck of 52 cards, shuffled.
	 */
	public ObservableDeck1() {
		shuffle();
	}

	public void addObserver(DeckObserver1 pObserver) {
		assert pObserver != null;
		aObservers.add(pObserver);
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
		for (DeckObserver1 observer : aObservers) {
			observer.shuffled();
		}
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
		for (DeckObserver1 observer : aObservers) {
			observer.cardPushed(pCard);
		}
	}

	/**
	 * Draws a card from the deck: removes the card from the top of the deck and
	 * returns it.
	 * 
	 * @return The card drawn.
	 * @pre !isEmpty()
	 */
	public Card draw() {
		assert !isEmpty();
		Card card = aCards.remove(aCards.size() - 1);
		for (DeckObserver1 observer : aObservers) {
			observer.cardDrawn(card);
		}
		return card;
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

/*
 * anchor: DrawLogger1
 * title: Concrete Observer
 * ---
 * This class is an example of _concrete observer_ in the design pattern.
 */
class DrawLogger1 implements DeckObserver1 {

	@Override
	public void shuffled() {}

	@Override
	public void cardDrawn(Card pCard) {
		IO.println(pCard + " drawn");
	}

	@Override
	public void cardPushed(Card pCard) {}
}

class SizeStatus1 implements DeckObserver1 {

	private int aSize = -1;

	@Override
	public void shuffled() {
		aSize = 52;
		IO.println(aSize);
	}

	@Override
	public void cardDrawn(Card pCard) {
		if (aSize >= 0) {
			aSize--;
			IO.println(aSize);
		}
		else {
			IO.println("Size unknown");
		}
	}

	@Override
	public void cardPushed(Card pCard) {
		if (aSize >= 0) {
			aSize++;
			IO.println(aSize);
		}
		else {
			IO.println("Size unknown");
		}
	}
}

