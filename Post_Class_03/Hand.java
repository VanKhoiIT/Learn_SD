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
        assert aCards.size() == 5 : "Hand must have exactly 5 cards to determine rank";

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

    /**
     * Ho tro ca Sanh At thap (A, 2, 3, 4, 5) va Sanh At cao (10, J, Q, K, A)
     */
    private boolean isStraight() {
        List<Card> sorted = new ArrayList<>(aCards);
        sorted.sort(Comparator.comparing(Card::getRank));

        // Sanh At cao (10, J, Q, K, ACE dung dau voi ordinal 0)
        boolean isAceHigh = sorted.get(0).getRank() == Rank.ACE
                && sorted.get(1).getRank() == Rank.TEN
                && sorted.get(2).getRank() == Rank.JACK
                && sorted.get(3).getRank() == Rank.QUEEN
                && sorted.get(4).getRank() == Rank.KING;

        if (isAceHigh) {
            return true;
        }

        // Sanh lien ke thong thuong (bao gom ca A, 2, 3, 4, 5)
        for (int i = 0; i < sorted.size() - 1; i++) {
            int current = sorted.get(i).getRank().ordinal();
            int next = sorted.get(i + 1).getRank().ordinal();
            if (next - current != 1) {
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

    // ==============================================================
    // PHẦN 1: BẢN CƠ BẢN (Theo đúng 2 gợi ý của đề bài 12)
    // Cùng loại bài coi như bằng nhau (trả về 0)
    // ==============================================================
    @Override
    public int compareTo(Hand pHand) {
        if (this.size() != 5 || pHand.size() != 5) {
            return Integer.compare(this.size(), pHand.size());
        }
        return this.getHandType().compareTo(pHand.getHandType());
    }

    // ==============================================================
    // PHẦN 2: BẢN NÂNG CAO (Loại bỏ đơn giản hóa - Tie Breaker)
    // Phân định thắng thua chi tiết khi cùng HandType
    // ==============================================================
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

    public static Comparator<Hand> createTieBreakerComparator() {
        return (h1, h2) -> {
            int baseCompare = h1.compareTo(h2);
            if (baseCompare != 0) {
                return baseCompare;
            }

            // Neu cung loai bai, xet chi tiet cac quan bai
            List<Rank> r1 = h1.getTieBreakerRanks();
            List<Rank> r2 = h2.getTieBreakerRanks();

            for (int i = 0; i < r1.size(); i++) {
                int cmp = r1.get(i).compareTo(r2.get(i));
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0;
        };
    }
}