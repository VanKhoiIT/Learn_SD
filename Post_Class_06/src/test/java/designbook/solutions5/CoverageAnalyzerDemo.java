package designbook.solutions5;

import e3.chapter5.Card;
import e3.chapter5.PokerHand;
import e3.chapter5.Rank;
import e3.chapter5.Suit;

/**
 * Trình phân tích luồng điều khiển và giải thích độ bao phủ kiểm thử (CLI Demo).
 * 
 * ĐÁP ÁN CHỐT CHO CÁC BÀI TẬP:
 * - Exercise 5.8: Tối thiểu 1 lần gọi UUT cho ca true (hoặc 36 lần gọi cho mọi sảnh UUT nhận diện).
 * - Exercise 5.9 (Statement Coverage): Tối thiểu ĐÚNG 2 LẦN GỌI UUT (1 ca true + 1 ca false).
 * - Exercise 5.10 (Branch Coverage): Tối thiểu ĐÚNG 3 LẦN GỌI UUT (TC01: True, TC02: Sai chất, TC03: Sai bậc).
 *   Lý do: Tác giả coi hai vế của toán tử short-circuit OR (||) là hai nhánh rẽ riêng rẽ trong luồng điều khiển.
 * 
 * LƯU Ý HỌC THUẬT:
 * - Đây là phân tích cấu trúc mã nguồn tĩnh (Static Control-Flow Analysis).
 * - Không phải số đo từ công cụ đo bytecode động (JaCoCo).
 */
public final class CoverageAnalyzerDemo {

	public static void main(String[] args) {
		System.out.println("================================================================================");
		System.out.println("  DESIGNBOOK CHAPTER 5: EXERCISES 5.8, 5.9, 5.10 - COMPREHENSIVE COVERAGE DEMO  ");
		System.out.println("================================================================================");
		System.out.println("Đối tượng kiểm thử (UUT): e3.chapter5.PokerHand.isStraightFlush() (private)");
		System.out.println("Phương pháp gọi: Java Reflection API (setAccessible = true)");
		System.out.println("--------------------------------------------------------------------------------");

		runExercise08Demo();
		runExercise09Demo();
		runExercise10Demo();
		printSummaryMatrix();
	}

	public static void runExercise08Demo() {
		System.out.println("\n>>> [EXERCISE 5.8: JAVA REFLECTION & POSITIVE TEST] <<<");
		PokerHand hand = new PokerHand(
				Card.get(Rank.TWO, Suit.CLUBS),
				Card.get(Rank.THREE, Suit.CLUBS),
				Card.get(Rank.FOUR, Suit.CLUBS),
				Card.get(Rank.FIVE, Suit.CLUBS),
				Card.get(Rank.SIX, Suit.CLUBS));

		boolean res = TestPokerHand.invokeIsStraightFlush(hand);
		System.out.println("Kiểm thử tay bài: [2C, 3C, 4C, 5C, 6C]");
		System.out.println("Kết quả isStraightFlush(): " + res + " (Mong đợi: true)");
		System.out.println("Số sảnh theo ordinal: 9 sảnh/chất x 4 chất = 36 sảnh.");
		System.out.println("Số sảnh theo luật Poker: 10 sảnh/chất x 4 chất = 40 sảnh (4 sảnh 10-J-Q-K-A bị UUT trả về false).");
		System.out.println("Kiểm thử vét cạn? KHÔNG. 36 (hoặc 40) ca / 2,598,960 tay bài = 0.00138% không gian mẫu.");
	}

	public static void runExercise09Demo() {
		System.out.println("\n>>> [EXERCISE 5.9: STATEMENT COVERAGE (100%)] <<<");
		System.out.println("Lần gọi 1 (TC01 - True): Thực thi 9/10 câu lệnh (S1..S7, S9, S10). Thiếu S8 return false;");
		System.out.println("Lần gọi 2 (TC02 - False): Kích hoạt câu lệnh S8 (return false;).");
		System.out.println("KẾT LUẬN: Tối thiểu ĐÚNG 2 LẦN GỌI UUT là đạt 100% Statement Coverage cho phương thức.");
		System.out.println("Ghi chú: Đây là phân tích cấu trúc mã nguồn tĩnh, không phải số đo JaCoCo.");
	}

	public static void runExercise10Demo() {
		System.out.println("\n>>> [EXERCISE 5.10: BRANCH COVERAGE (100%)] <<<");
		System.out.println("Phân tích cơ chế ngắn mạch (short-circuit OR) if (suit != suit || rankGap != 1):");
		System.out.println("Tác giả coi hai vế của || là hai nhánh rẽ riêng rẽ cần được kích hoạt độc lập:");
		System.out.println(" - Ca 1 (TC01): Cả 2 vế False -> Bỏ qua IF, vòng lặp kết thúc -> return true;");
		System.out.println(" - Ca 2 (TC02): Vế 1 True (Sai chất) -> Ngắt ngắn mạch -> return false;");
		System.out.println(" - Ca 3 (TC03): Vế 1 False, Vế 2 True (Sai bậc) -> return false;");
		System.out.println("KẾT LUẬN CHÍNH THỨC: Tối thiểu ĐÚNG 3 LẦN GỌI UUT để đạt 100% Branch Coverage.");
		System.out.println("(Ghi chú phụ: Nếu chỉ xét quyết định IF tổng thể ở mức True/False mà không xét ngắn mạch, cần 2 lần gọi).");
	}

	public static void printSummaryMatrix() {
		System.out.println("\n================================================================================");
		System.out.println("                               TỔNG KẾT SO SÁNH                                 ");
		System.out.println("================================================================================");
		System.out.println(String.format("%-15s | %-20s | %-25s | %-16s", "Bài tập", "Ca kiểm thử", "Tiêu chí bao phủ", "Số lần gọi tối thiểu"));
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println(String.format("%-15s | %-20s | %-25s | %-16s", "Exercise 5.8", "TC01 (True)", "Chức năng (True)", "1 (hoặc 36/40)"));
		System.out.println(String.format("%-15s | %-20s | %-25s | %-16s", "Exercise 5.9", "TC01 + TC02", "100% Statement Coverage", "2 lần gọi"));
		System.out.println(String.format("%-15s | %-20s | %-25s | %-16s", "Exercise 5.10", "TC01 + TC02 + TC03", "100% Branch Coverage", "3 lần gọi"));
		System.out.println("================================================================================\n");
	}
}
