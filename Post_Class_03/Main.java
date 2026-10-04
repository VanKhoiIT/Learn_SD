public class Main {

    public static void main(String[] args) {
        Hand hand1 = new Hand(5);
        Hand hand2 = new Hand(5);

        // Hand 1: Doi 10 (Kickers: A, 8, 4)
        hand1.add(new Card(Rank.TEN, Suit.CLUBS));
        hand1.add(new Card(Rank.TEN, Suit.DIAMONDS));
        hand1.add(new Card(Rank.ACE, Suit.HEARTS));
        hand1.add(new Card(Rank.EIGHT, Suit.SPADES));
        hand1.add(new Card(Rank.FOUR, Suit.CLUBS));

        // Hand 2: Doi 3 (Kickers: K, Q, 9)
        hand2.add(new Card(Rank.THREE, Suit.HEARTS));
        hand2.add(new Card(Rank.THREE, Suit.SPADES));
        hand2.add(new Card(Rank.KING, Suit.CLUBS));
        hand2.add(new Card(Rank.QUEEN, Suit.DIAMONDS));
        hand2.add(new Card(Rank.NINE, Suit.HEARTS));

        System.out.println("Hand 1: " + hand1.getCards());
        System.out.println("Hand 1 Type: " + hand1.getHandType());
        System.out.println("-------------------------");
        System.out.println("Hand 2: " + hand2.getCards());
        System.out.println("Hand 2 Type: " + hand2.getHandType());
        System.out.println("-------------------------");

        int result = hand1.compareTo(hand2);
        System.out.println("Ket qua so sanh: " + result);

        if (result > 0) {
            System.out.println("Hand 1 thang Hand 2!");
        } else if (result < 0) {
            System.out.println("Hand 2 thang Hand 1!");
        } else {
            System.out.println("Hoa nhau!");
        }
    }
}