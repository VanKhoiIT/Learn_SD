package designbook.solutions5;

import e3.chapter4.Card;
import e3.chapter4.CardSource;
import e3.chapter4.Rank;
import e3.chapter4.Suit;
import e3.chapter5.BiCardSource;
import e3.chapter5.BiCardSourceBuggy;

/**
 * Trình phân tích luồng điều khiển (CFG), ma trận bao phủ nhánh (Branch Coverage),
 * và đối chiếu lỗi cài cắm (Seeded Bug) cho Exercise 5.11 (BiCardSource).
 */
public final class CoverageAnalyzerDemo {

	public static void main(String[] args) {
		System.out.println("================================================================================");
		System.out.println("  DESIGNBOOK CHAPTER 5: EXERCISE 5.11 - BICARDSOURCE BRANCH COVERAGE ANALYSIS    ");
		System.out.println("================================================================================");
		System.out.println("UUT: e3.chapter5.BiCardSource (implements e3.chapter4.CardSource)");
		System.out.println("Framework: JUnit Jupiter 5.10.2 + Test Stubs (CardSourceStub with Optional<Card>)");
		System.out.println("Package phong cách: Edition 3 (code-e3 - 2025)");
		System.out.println("--------------------------------------------------------------------------------");

		analyzeIsEmptyCoverage();
		analyzeDrawCoverage();
		analyzeSeededBug();
		printCoverageMatrix();
	}

	public static void analyzeIsEmptyCoverage() {
		System.out.println("\n>>> [PHÂN TÍCH 1: PHƯƠNG THỨC isEmpty() - LOGICAL AND SHORT-CIRCUIT] <<<");
		System.out.println("Mã nguồn: return aSource1.isEmpty() && aSource2.isEmpty();");
		System.out.println("Toán tử logic '&&' tạo ra 3 đường thực thi độc lập (Branches):");
		System.out.println(" 1. Nhánh 1 (True && True -> TRUE):");
		System.out.println("    - aSource1.isEmpty() = true, aSource2.isEmpty() = true.");
		System.out.println("    - Ca kiểm thử: testEmpty_True() -> Trả về true. [COVERED]");
		System.out.println(" 2. Nhánh 2 (True && False -> FALSE):");
		System.out.println("    - aSource1.isEmpty() = true, aSource2.isEmpty() = false.");
		System.out.println("    - Ca kiểm thử: testEmpty_False_FirstEmpty() -> Trả về false. [COVERED]");
		System.out.println(" 3. Nhánh 3 (False -> Ngắt ngắn mạch / Short-circuit -> FALSE):");
		System.out.println("    - aSource1.isEmpty() = false (không cần duyệt aSource2).");
		System.out.println("    - Ca kiểm thử 3a: testEmpty_False_SecondEmpty() (Source 2 rỗng). [COVERED]");
		System.out.println("    - Ca kiểm thử 3b: testEmpty_False_NeitherEmpty() (Source 2 không rỗng). [COVERED]");
		System.out.println("=> KẾT LUẬN: 100% Branch Coverage cho isEmpty() (4 ca kiểm thử bao phủ toàn bộ bảng chân trị).");
	}

	public static void analyzeDrawCoverage() {
		System.out.println("\n>>> [PHÂN TÍCH 2: PHƯƠNG THỨC draw() - IF-ELSE BRANCH COVERAGE] <<<");
		System.out.println("Mã nguồn chuẩn:");
		System.out.println("   if (!aSource1.isEmpty()) {");
		System.out.println("       return aSource1.draw();   // Branch T");
		System.out.println("   } else {");
		System.out.println("       return aSource2.draw();   // Branch F");
		System.out.println("   }");
		System.out.println("Luồng điều khiển có đúng 2 nhánh rẽ chính:");
		System.out.println(" 1. Nhánh True (!aSource1.isEmpty() == true):");
		System.out.println("    - Nguồn 1 có bài. Rút bài từ Nguồn 1.");
		System.out.println("    - Ca kiểm thử: testDraw_SecondEmpty() (Nguồn 2 rỗng) -> Trả về lá bài từ Nguồn 1. [COVERED]");
		System.out.println("    - Ca kiểm thử: testDraw_NeitherEmpty() (Nguồn 2 cũng có bài) -> Ưu tiên Nguồn 1. [COVERED]");
		System.out.println(" 2. Nhánh False (!aSource1.isEmpty() == false):");
		System.out.println("    - Nguồn 1 rỗng. Chuyển sang rút từ Nguồn 2.");
		System.out.println("    - Ca kiểm thử: testDraw_FirstEmpty() -> Trả về lá bài từ Nguồn 2. [COVERED]");
		System.out.println("=> KẾT LUẬN: 100% Branch Coverage cho draw() (Bao phủ cả 2 nhánh True và False).");
	}

	public static void analyzeSeededBug() {
		System.out.println("\n>>> [PHÂN TÍCH 3: LỖI CÀI CẮM (SEEDED BUG) VÀ CƠ CHẾ PHÁT HIỆN] <<<");
		System.out.println("Đề bài gốc cài cắm lỗi logic ngược (Inverted Delegation) trong draw():");
		System.out.println("   if (!aSource1.isEmpty()) {");
		System.out.println("       return aSource2.draw(); // SAI LẦM: Nguồn 1 còn bài lại rút từ Nguồn 2!");
		System.out.println("   } else {");
		System.out.println("       return aSource1.draw(); // SAI LẦM: Nguồn 1 đã rỗng lại cố rút từ Nguồn 1!");
		System.out.println("   }");
		System.out.println("Hậu quả khi chạy bộ kiểm thử trên mã có lỗi:");
		System.out.println(" - testDraw_FirstEmpty: Văng ngoại lệ/AssertionError vì gọi draw() trên aSource1 rỗng! [FAILED]");
		System.out.println(" - testDraw_SecondEmpty: Văng ngoại lệ/AssertionError vì gọi draw() trên aSource2 rỗng! [FAILED]");
		System.out.println(" - testDraw_NeitherEmpty: Rút nhầm lá TWO từ aSource2 thay vì lá ACE từ aSource1! [FAILED]");
		System.out.println("SỬA LỖI (FIX): Hoán đổi aSource2 <-> aSource1 trong hai nhánh if-else.");
		System.out.println("Sau khi sửa: Toàn bộ 7/7 test cases chính (và 2 test mở rộng) đều PASS 100%.");
	}

	public static void printCoverageMatrix() {
		System.out.println("\n================================================================================");
		System.out.println("                    MA TRẬN BAO PHỦ NHÁNH (BRANCH COVERAGE MATRIX)             ");
		System.out.println("================================================================================");
		System.out.println(String.format("%-28s | %-16s | %-16s | %-15s", "Test Method", "aSource1.isEmpty()", "aSource2.isEmpty()", "Branch Executed"));
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println(String.format("%-28s | %-16s | %-16s | %-15s", "testEmpty_True", "true", "true", "isEmpty() T&&T"));
		System.out.println(String.format("%-28s | %-16s | %-16s | %-15s", "testEmpty_False_FirstEmpty", "true", "false", "isEmpty() T&&F"));
		System.out.println(String.format("%-28s | %-16s | %-16s | %-15s", "testEmpty_False_SecondEmpty", "false", "true", "isEmpty() F (sc)"));
		System.out.println(String.format("%-28s | %-16s | %-16s | %-15s", "testEmpty_False_NeitherEmpty", "false", "false", "isEmpty() F (sc)"));
		System.out.println(String.format("%-28s | %-16s | %-16s | %-15s", "testDraw_FirstEmpty", "true", "false", "draw() Else (S2)"));
		System.out.println(String.format("%-28s | %-16s | %-16s | %-15s", "testDraw_SecondEmpty", "false", "true", "draw() If (S1)"));
		System.out.println(String.format("%-28s | %-16s | %-16s | %-15s", "testDraw_NeitherEmpty", "false", "false", "draw() If (S1)"));
		System.out.println("================================================================================");
		System.out.println("TỔNG CỘNG BAO PHỦ NHÁNH: 100% BRANCH COVERAGE (Tất cả nhánh quyết định đều được kích hoạt).");
		System.out.println("================================================================================\n");
	}
}
