package e3.chapter5;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * PokerHand based on 3rd Edition (code-e3).
 * Uses e3.chapter5.Card, e3.chapter5.Rank, and e3.chapter5.Suit.
 * Accessors in code-e3 are rank() and suit().
 */
public class PokerHand {

	private final List<Card> aCards;

	public PokerHand(Card... pCards) {
		assert pCards != null && pCards.length == 5;
		aCards = Arrays.asList(pCards);
		aCards.sort(new Comparator<Card>() {
			public int compare(Card pCard1, Card pCard2) {
				return pCard1.rank().compareTo(pCard2.rank());
			}
		});
	}

	public List<Card> cards() {
		return Collections.unmodifiableList(aCards);
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(aCards);
	}

	/**
	 * Private method under test (UUT).
	 * Checks if the hand is a straight flush using code-e3 methods card.suit() and card.rank().
	 */
	private boolean isStraightFlush() {
		Suit suit = aCards.get(0).suit();
		Rank rank = aCards.get(0).rank();
		Iterator<Card> iterator = aCards.iterator();
		iterator.next();
		while (iterator.hasNext()) {
			Card card = iterator.next();
			if (card.suit() != suit || card.rank().ordinal() - rank.ordinal() != 1) {
				return false;
			}
			rank = card.rank();
		}
		return true;
	}
}
