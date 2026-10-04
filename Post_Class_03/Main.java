public class Main {

    public static void main(String[] args) {
        Hand hand1 = new Hand(5);
        Hand hand2 = new Hand(5);

        // Hand 1: Doi 10
        hand1.add(new Card(Rank.TEN, Suit.CLUBS));
        hand1.add(new Card(Rank.TEN, Suit.DIAMONDS));
        hand1.add(new Card(Rank.ACE, Suit.HEARTS));
        hand1.add(new Card(Rank.EIGHT, Suit.SPADES));
        hand1.add(new Card(Rank.FOUR, Suit.CLUBS));

        // Hand 2: Doi 3
        hand2.add(new Card(Rank.THREE, Suit.HEARTS));
        hand2.add(new Card(Rank.THREE, Suit.SPADES));
        hand2.add(new Card(Rank.KING, Suit.CLUBS));
        hand2.add(new Card(Rank.QUEEN, Suit.DIAMONDS));
        hand2.add(new Card(Rank.NINE, Suit.HEARTS));

        System.out.println("Hand 1 Type: " + hand1.getHandType());
        System.out.println("Hand 2 Type: " + hand2.getHandType());
        System.out.println("-------------------------------------------------");

        // 1. Chạy theo bản cơ bản (Bài 12 gốc - compareTo)
        int basicResult = hand1.compareTo(hand2);
        System.out.println("[Ban Co Ban - De Bai 12]");
        System.out.println("Ket qua compareTo: " + basicResult + " (0 nghia la cung loai bai duoc coi la ngang nhau)");

        System.out.println("-------------------------------------------------");

        // 2. Chạy theo bản nâng cao (Bỏ đơn giản hóa - Tie Breaker)
        int advancedResult = Hand.createTieBreakerComparator().compare(hand1, hand2);
        System.out.println("[Ban Nang Cao - Loai bo don gian hoa]");
        System.out.println("Ket qua Tie-Breaker: " + advancedResult);
        if (advancedResult > 0) {
            System.out.println("-> Hand 1 thang (Doi 10 lon hon Doi 3)");
        } else if (advancedResult < 0) {
            System.out.println("-> Hand 2 thang");
        } else {
            System.out.println("-> Hoa hoan toan");
        }
    }
}