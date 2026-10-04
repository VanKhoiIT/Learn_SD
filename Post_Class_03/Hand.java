import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Hand implements Iterable<Card>, Comparable<Hand> {

    private final List<Card> aCards = new ArrayList<>();
    private final int aMaxCards;

    public Hand(int pMaxCards) {
        assert pMaxCards > 0;
        this.aMaxCards = pMaxCards;
    }

    public void add(Card pCard) {
        assert pCard != null;
        assert !isFull();
        aCards.add(pCard);
    }

    public boolean isFull() {
        return aCards.size() == aMaxCards;
    }

    public boolean isEmpty() {
        return aCards.isEmpty();
    }

    public void remove(Card pCard) {
        assert pCard != null;
        aCards.remove(pCard);
    }

    public boolean contains(Card pCard) {
        assert pCard != null;
        return aCards.contains(pCard);
    }

    public int size() {
        return aCards.size();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(aCards);
    }

    @Override
    public Iterator<Card> iterator() {
        return aCards.iterator();
    }

    public HandType getHandType() {
        assert aCards.size() == 5 : "Chi xep hang khi bo bai co du 5 la";

        boolean flush = isFlush();
        boolean straight = isStraight();

        if (straight && flush) {
            return HandType.STRAIGHT_FLUSH;
        }

        Map<Rank, Integer> rankCounts = getRankCounts();
        List<Integer> counts = new ArrayList<>(rankCounts.values());
        counts.sort(Collections.reverseOrder());

        if (counts.get(0) == 4) {
            return HandType.FOUR_OF_A_KIND;
        }
        if (counts.get(0) == 3 && counts.get(1) == 2) {
            return HandType.FULL_HOUSE;
        }
        if (flush) {
            return HandType.FLUSH;
        }
        if (straight) {
            return HandType.STRAIGHT;
        }
        if (counts.get(0) == 3) {
            return HandType.THREE_OF_A_KIND;
        }
        if (counts.get(0) == 2 && counts.get(1) == 2) {
            return HandType.TWO_PAIR;
        }
        if (counts.get(0) == 2) {
            return HandType.ONE_PAIR;
        }

        return HandType.HIGH_CARD;
    }

    private boolean isFlush() {
        for (int i = 1; i < aCards.size(); i++) {
            if (aCards.get(i).getSuit() != aCards.get(0).getSuit()) {
                return false;
            }
        }
        return true;
    }

    private boolean isStraight() {
        List<Card> sortedCards = new ArrayList<>(aCards);
        sortedCards.sort(Comparator.comparing(Card::getRank));

        // Sảnh Át thấp: 2, 3, 4, 5, ACE
        boolean isAceLow = sortedCards.get(0).getRank().ordinal() == 0
                && sortedCards.get(1).getRank().ordinal() == 1
                && sortedCards.get(2).getRank().ordinal() == 2
                && sortedCards.get(3).getRank().ordinal() == 3
                && sortedCards.get(4).getRank().ordinal() == 12;

        if (isAceLow) {
            return true;
        }

        for (int i = 0; i < sortedCards.size() - 1; i++) {
            int currentOrdinal = sortedCards.get(i).getRank().ordinal();
            int nextOrdinal = sortedCards.get(i + 1).getRank().ordinal();
            if (nextOrdinal - currentOrdinal != 1) {
                return false;
            }
        }
        return true;
    }

    private Map<Rank, Integer> getRankCounts() {
        Map<Rank, Integer> counts = new HashMap<>();
        for (Card card : aCards) {
            counts.put(card.getRank(), counts.getOrDefault(card.getRank(), 0) + 1);
        }
        return counts;
    }

    private List<Rank> getTieBreakerRanks() {
        Map<Rank, Integer> counts = getRankCounts();
        List<Rank> ranks = new ArrayList<>(counts.keySet());

        ranks.sort((r1, r2) -> {
            int countCompare = counts.get(r2).compareTo(counts.get(r1));
            if (countCompare != 0) {
                return countCompare;
            }
            return r2.compareTo(r1);
        });

        return ranks;
    }

    @Override
    public int compareTo(Hand pHand) {
        if (this.size() < 5 || pHand.size() < 5) {
            return Integer.compare(this.size(), pHand.size());
        }

        int typeComparison = this.getHandType().compareTo(pHand.getHandType());
        if (typeComparison != 0) {
            return typeComparison;
        }

        List<Rank> myRanks = this.getTieBreakerRanks();
        List<Rank> otherRanks = pHand.getTieBreakerRanks();

        for (int i = 0; i < myRanks.size(); i++) {
            int rankComparison = myRanks.get(i).compareTo(otherRanks.get(i));
            if (rankComparison != 0) {
                return rankComparison;
            }
        }

        return 0;
    }

    public static Comparator<Hand> createAscendingComparator() {
        return Hand::compareTo;
    }

    public static Comparator<Hand> createDescendingComparator() {
        return Collections.reverseOrder(Hand::compareTo);
    }
}