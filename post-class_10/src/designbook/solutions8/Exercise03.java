package designbook.solutions8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import designbook.chapter8.Card;
import designbook.chapter8.Rank;
import designbook.chapter8.Suit;

public class Exercise03 {

	public static void main(String[] args) {
		ObservableDeck3 deck = new ObservableDeck3();
		deck.addObserver(new DrawLogger3());
		deck.addObserver(new SizeStatus3());
		deck.shuffle();
		deck.draw();
		deck.draw();
	}
}

@SuppressWarnings("deprecation")
class ObservableDeck3 extends Observable {

	private final List<Card> aCards = new ArrayList<>();

	/**
	 * Creates a new deck of 52 cards, shuffled.
	 */
	public ObservableDeck3() {
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
		setChanged();
		notifyObservers(new DeckChangeEvent(DeckChangeType.SHUFFLED));
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
		setChanged();
		notifyObservers(new DeckChangeEvent(DeckChangeType.CARD_PUSHED, pCard));
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
		setChanged();
		notifyObservers(new DeckChangeEvent(DeckChangeType.CARD_DRAWN, card));
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

@SuppressWarnings("deprecation")
class DrawLogger3 implements Observer {

	@Override
	public void update(Observable pDeck, Object pEvent) {
		if (pEvent instanceof DeckChangeEvent) {
			DeckChangeEvent event = (DeckChangeEvent) pEvent;
			if (event.getChangeType() == DeckChangeType.CARD_DRAWN) {
				IO.println(event.getCard().get() + " drawn");
			}
		}
	}
}

@SuppressWarnings("deprecation")
class SizeStatus3 implements Observer {

	@Override
	public void update(Observable pDeck, Object pEvent) {
		IO.println(((ObservableDeck3) pDeck).getCards().size());
	}
}

enum DeckChangeType {
	SHUFFLED, CARD_DRAWN, CARD_PUSHED
}

class DeckChangeEvent {

	private final DeckChangeType aChangeType;
	private final Optional<Card> aCard;

	public DeckChangeEvent(DeckChangeType pType) {
		aChangeType = pType;
		aCard = Optional.empty();
	}

	public DeckChangeEvent(DeckChangeType pType, Card pCard) {
		aChangeType = pType;
		aCard = Optional.of(pCard);
	}

	public DeckChangeType getChangeType() {
		return aChangeType;
	}

	public Optional<Card> getCard() {
		return aCard;
	}
}
