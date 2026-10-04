import java.util.Objects;

public class Card implements Comparable<Card> {

    private final Rank aRank;
    private final Suit aSuit;

    public Card(Rank pRank, Suit pSuit) {
        assert pRank != null && pSuit != null;
        this.aRank = pRank;
        this.aSuit = pSuit;
    }

    public Rank getRank() {
        return aRank;
    }

    public Suit getSuit() {
        return aSuit;
    }

    @Override
    public int compareTo(Card pCard) {
        return this.aRank.compareTo(pCard.aRank);
    }

    @Override
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (pObject == null || getClass() != pObject.getClass()) {
            return false;
        }
        Card card = (Card) pObject;
        return aRank == card.aRank && aSuit == card.aSuit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(aRank, aSuit);
    }

    @Override
    public String toString() {
        return aRank + " of " + aSuit;
    }
}