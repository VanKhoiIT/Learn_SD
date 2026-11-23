package designbook.solutions8;

import java.util.ArrayList;
import java.util.List;

import designbook.chapter8.Card;
import designbook.chapter8.Deck;

public class Exercise04 {

	public static void main(String[] args) {
		ObservableDeck4 deck = new ObservableDeck4();
		deck.addObserver(new DrawLogger1());
		deck.addObserver(new SizeStatus1());
		deck.shuffle();
		deck.draw();
		deck.draw();
	}
}

class ObservableDeck4 extends Deck {

	private final List<DeckObserver1> aObservers = new ArrayList<>();

	public void addObserver(DeckObserver1 pObserver) {
		assert pObserver != null;
		aObservers.add(pObserver);
	}

	@Override
	public void shuffle() {
		super.shuffle();
		if (aObservers != null) {
			for (DeckObserver1 observer : aObservers) {
				observer.shuffled();
			}
		}
	}

	// Push is skipped in this example for compatibility with the plain Deck object

	@Override
	public Card draw() {
		Card card = super.draw();
		for (DeckObserver1 observer : aObservers) {
			observer.cardDrawn(card);
		}
		return card;
	}
}
