package designbook.solutions5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import e3.chapter5.Card;
import e3.chapter5.PokerHand;
import e3.chapter5.Rank;
import e3.chapter5.Suit;

/**
 * Lớp kiểm thử đơn vị cho lớp PokerHand (Exercises 5.8, 5.9, 5.10).
 * Áp dụng Java Reflection API để kiểm thử phương thức private isStraightFlush().
 * 
 * Bộ kiểm thử kết hợp toàn diện:
 * - Exercise 5.8: Kiểm thử hàm private, ca true, 36 dãy theo ordinal, phân tích 40 sảnh và hạn chế Royal Flush.
 * - Exercise 5.9: Statement Coverage đạt 100% với tối thiểu 2 lần thực thi UUT (TC01 + TC02).
 * - Exercise 5.10: Branch & Short-circuit Condition Coverage đạt 100% với 3 ca test (TC01 + TC02 + TC03).
 */
public class TestPokerHand {

	/**
	 * Phương thức trợ giúp (Helper method) sử dụng Java Reflection API
	 * để truy xuất và thực thi phương thức private isStraightFlush() của PokerHand.
	 * 
	 * @param pHand Đối tượng PokerHand (UUT - Unit Under Test)
	 * @return kết quả boolean của isStraightFlush()
	 */
	public static boolean invokeIsStraightFlush(PokerHand pHand) {
		try {
			Method method = PokerHand.class.getDeclaredMethod("isStraightFlush");
			method.setAccessible(true);
			return (boolean) method.invoke(pHand);
		}
		catch (ReflectiveOperationException e) {
			fail("Reflection failed to invoke private isStraightFlush: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Ca kiểm thử 1 (TC01): 5 lá bài cùng chất, liên tiếp theo thứ bậc.
	 * Mong đợi: true.
	 * Độ bao phủ: Kích hoạt các câu lệnh S1-S7, S9, S10; nhánh IF = false.
	 */
	@Test
	@DisplayName("TC01: Kiểm thử Straight Flush hợp lệ trả về true (Ex 8, 9, 10)")
	public void testIsStraightFlush_True() {
		PokerHand hand = new PokerHand(
				Card.get(Rank.TWO, Suit.CLUBS),
				Card.get(Rank.THREE, Suit.CLUBS),
				Card.get(Rank.FOUR, Suit.CLUBS),
				Card.get(Rank.FIVE, Suit.CLUBS),
				Card.get(Rank.SIX, Suit.CLUBS));

		assertTrue(invokeIsStraightFlush(hand), "Tay bài [2C, 3C, 4C, 5C, 6C] phải là Straight Flush");
	}

	/**
	 * Ca kiểm thử 2 (TC02): 5 lá bài liên tiếp thứ bậc nhưng khác chất (lá cuối khác chất).
	 * Mong đợi: false.
	 * Độ bao phủ: Kích hoạt câu lệnh S8 (return false); nhánh IF = true qua điều kiện suit khác.
	 */
	@Test
	@DisplayName("TC02: Kiểm thử khác chất trả về false - Đạt 100% Statement Coverage (Ex 9, 10)")
	public void testIsStraightFlush_WrongSuit() {
		PokerHand hand = new PokerHand(
				Card.get(Rank.TWO, Suit.CLUBS),
				Card.get(Rank.THREE, Suit.CLUBS),
				Card.get(Rank.FOUR, Suit.CLUBS),
				Card.get(Rank.FIVE, Suit.CLUBS),
				Card.get(Rank.SIX, Suit.DIAMONDS));

		assertFalse(invokeIsStraightFlush(hand), "Tay bài chứa lá bài khác chất không được là Straight Flush");
	}

	/**
	 * Ca kiểm thử 3 (TC03): 5 lá bài cùng chất nhưng không liên tiếp về thứ bậc (đứt quãng rank).
	 * Mong đợi: false.
	 * Độ bao phủ: Nhánh IF = true qua điều kiện rankGap != 1, bao phủ vế sau của toán tử ngắn mạch ||.
	 */
	@Test
	@DisplayName("TC03: Kiểm thử cùng chất nhưng sai thứ bậc rank - Hoàn thành Branch Coverage (Ex 10)")
	public void testIsStraightFlush_WrongRank() {
		PokerHand hand = new PokerHand(
				Card.get(Rank.TWO, Suit.CLUBS),
				Card.get(Rank.THREE, Suit.CLUBS),
				Card.get(Rank.FOUR, Suit.CLUBS),
				Card.get(Rank.FIVE, Suit.CLUBS),
				Card.get(Rank.SEVEN, Suit.CLUBS));

		assertFalse(invokeIsStraightFlush(hand), "Tay bài đứt quãng thứ bậc không được là Straight Flush");
	}

	/**
	 * Kiểm chứng thực nghiệm cho Bài 5.8:
	 * Tự động sinh và kiểm thử toàn bộ 36 dãy liên tiếp theo ordinal khả dĩ trên 4 chất (từ ACE đến NINE).
	 */
	@Test
	@DisplayName("Ex 8: Kiểm chứng thực nghiệm 36 dãy liên tiếp theo ordinal đều trả về true")
	public void testAll36OrdinalConsecutiveHands() {
		List<PokerHand> straightFlushes = new ArrayList<>();
		Rank[] ranks = Rank.values(); // ACE(0) đến KING(12)

		for (Suit suit : Suit.values()) {
			// Thuật toán của UUT kiểm tra ordinal kề nhau bằng 1.
			// Các sảnh khả dĩ bắt đầu từ ACE (index 0) đến NINE (index 8): 9 dãy/chất x 4 chất = 36 dãy.
			for (int startRank = 0; startRank <= ranks.length - 5; startRank++) {
				PokerHand hand = new PokerHand(
						Card.get(ranks[startRank], suit),
						Card.get(ranks[startRank + 1], suit),
						Card.get(ranks[startRank + 2], suit),
						Card.get(ranks[startRank + 3], suit),
						Card.get(ranks[startRank + 4], suit));
				straightFlushes.add(hand);
				assertTrue(invokeIsStraightFlush(hand), "Dãy sảnh từ " + ranks[startRank] + " chất " + suit + " phải trả về true");
			}
		}

		assertEquals(36, straightFlushes.size(), "Tổng số dãy liên tiếp theo ordinal phải là 36");
	}

	/**
	 * Kiểm chứng giới hạn đã biết (Known Limitation) của thuật toán UUT đề bài:
	 * Theo luật Poker tiêu chuẩn, bộ 10-J-Q-K-A (Royal Flush) là Straight Flush cao nhất.
	 * Tuy nhiên, thuật toán UUT gốc sử dụng ordinal() trực tiếp (ACE=0, KING=12),
	 * nên khi sắp xếp tăng dần, ACE đứng đầu và TEN đứng sau, khoảng cách TEN(9) - ACE(0) = 9 != 1.
	 * UUT trả về false.
	 * 
	 * Đây là Characterization Test ghi nhận hạn chế của UUT mà không sửa code đề bài.
	 */
	@Test
	@DisplayName("Ex 8: Ghi nhận hạn chế của UUT gốc đối với sảnh Royal Flush 10-J-Q-K-A (Known Limitation)")
	public void testRoyalFlushKnownLimitation() {
		PokerHand hand = new PokerHand(
				Card.get(Rank.TEN, Suit.HEARTS),
				Card.get(Rank.JACK, Suit.HEARTS),
				Card.get(Rank.QUEEN, Suit.HEARTS),
				Card.get(Rank.KING, Suit.HEARTS),
				Card.get(Rank.ACE, Suit.HEARTS));

		// UUT gốc trả về false do ACE có ordinal() = 0
		assertFalse(invokeIsStraightFlush(hand), "UUT gốc không xử lý ACE cao (Royal Flush trả về false)");
	}

	/**
	 * Kiểm chứng cho Câu hỏi 1 & Câu hỏi 2 (Bài 5.8):
	 * - Số Straight Flush theo luật Poker: 40 (4 chất x 10 dãy).
	 * - Số Straight Flush UUT nhận diện: 36 (4 chất x 9 dãy ordinal liên tiếp).
	 * - Tổng số tay bài 5 lá khả dĩ: C(52, 5) = 2,598,960.
	 * - Chứng minh 36 (hoặc 40) ca test chỉ chiếm ~0.00138% không gian mẫu, không thể đạt kiểm thử vét cạn (Exhaustive Testing).
	 */
	@Test
	@DisplayName("Ex 8: Phân tích số lượng sảnh và chứng minh không thể đạt kiểm thử vét cạn")
	public void testStraightFlushCountsAndExhaustiveAnalysis() {
		int pokerStandardCount = 4 * 10; // 40
		int uutRecognizedCount = Suit.values().length * (Rank.values().length - 4); // 36
		long totalPossibleHands = 2598960L; // C(52, 5)

		assertEquals(40, pokerStandardCount, "Luật Poker tiêu chuẩn có 40 sảnh đồng chất");
		assertEquals(36, uutRecognizedCount, "UUT gốc chỉ nhận diện được 36 dãy theo ordinal");
		
		double coverageRatio = (double) uutRecognizedCount / totalPossibleHands * 100.0;
		assertTrue(coverageRatio < 0.002, "36 ca test chỉ chiếm ~0.00138% không gian mẫu");
		assertTrue(pokerStandardCount < totalPossibleHands, "40 ca test không thể tạo thành Exhaustive Testing");
	}
}
