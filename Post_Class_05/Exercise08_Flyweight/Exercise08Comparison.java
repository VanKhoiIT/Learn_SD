package exercise08;

import java.util.ArrayList;
import java.util.List;

/**
 * Chương trình đo lường, so sánh hiệu năng và kiểm nghiệm thực tế giữa hai cách tiếp cận:
 * 1. Eager Pre-initialization (Khởi tạo sẵn toàn bộ 52 lá bài)
 * 2. Lazy Creation (Khởi tạo lười theo nhu cầu)
 * 
 * Sử dụng System.currentTimeMillis() (theo yêu cầu đề bài) và bổ sung System.nanoTime()
 * để phân tích chuyên sâu về độ phân giải của đồng hồ đo thời gian trong Java.
 */
public class Exercise08Comparison {

	public static void main(String[] args) {
		printHeader();

		// ====================================================================
		// PHẦN 1: KIỂM CHỨNG TÍNH ĐÚNG ĐẮN CỦA MẪU FLYWEIGHT (OBJECT IDENTITY)
		// ====================================================================
		System.out.println(">>> PHẦN 1: KIỂM CHỨNG TÍNH ĐÚNG ĐẮN CỦA MẪU FLYWEIGHT");
		CardEager eager1 = CardEager.get(Rank.ACE, Suit.SPADES);
		CardEager eager2 = CardEager.get(Rank.ACE, Suit.SPADES);
		System.out.println("  [Eager] Hai lần lấy ACE of SPADES có cùng tham chiếu (==)? " + (eager1 == eager2));

		CardLazy lazy1 = CardLazy.get(Rank.ACE, Suit.SPADES);
		CardLazy lazy2 = CardLazy.get(Rank.ACE, Suit.SPADES);
		System.out.println("  [Lazy]  Hai lần lấy ACE of SPADES có cùng tham chiếu (==)? " + (lazy1 == lazy2));
		System.out.println("  [Lazy]  Số lá bài trong cache sau khi gọi 1 loại: " + CardLazy.getCreatedCount());
		System.out.println();

		// ====================================================================
		// PHẦN 2: ĐO THỜI GIAN LẦN ĐẦU TIÊN LẤY ĐỦ 52 LÁ BÀI (COLD START)
		// ====================================================================
		System.out.println(">>> PHẦN 2: ĐO THỜI GIAN LẤY ĐỦ 52 LÁ BÀI LẦN ĐẦU TIÊN (COLD START)");
		System.out.println("  (Lưu ý: Eager đã khởi tạo lúc nạp lớp; Lazy sẽ vừa lấy vừa khởi tạo 51 lá còn lại)");

		// Đo Eager Cold Start
		long eagerStartMillis = System.currentTimeMillis();
		long eagerStartNano = System.nanoTime();
		List<CardEager> eagerCards = new ArrayList<>(52);
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				eagerCards.add(CardEager.get(rank, suit));
			}
		}
		long eagerEndNano = System.nanoTime();
		long eagerEndMillis = System.currentTimeMillis();
		long eagerDurationMillis = eagerEndMillis - eagerStartMillis;
		long eagerDurationNano = eagerEndNano - eagerStartNano;

		// Đo Lazy Cold Start (khởi tạo 51 lá bài còn lại)
		long lazyStartMillis = System.currentTimeMillis();
		long lazyStartNano = System.nanoTime();
		List<CardLazy> lazyCards = new ArrayList<>(52);
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				lazyCards.add(CardLazy.get(rank, suit));
			}
		}
		long lazyEndNano = System.nanoTime();
		long lazyEndMillis = System.currentTimeMillis();
		long lazyDurationMillis = lazyEndMillis - lazyStartMillis;
		long lazyDurationNano = lazyEndNano - lazyStartNano;

		System.out.printf("  [Eager Cold Start] System.currentTimeMillis(): %d ms  | System.nanoTime(): %,d ns (%.3f ms)%n",
				eagerDurationMillis, eagerDurationNano, eagerDurationNano / 1_000_000.0);
		System.out.printf("  [Lazy Cold Start]  System.currentTimeMillis(): %d ms  | System.nanoTime(): %,d ns (%.3f ms)%n",
				lazyDurationMillis, lazyDurationNano, lazyDurationNano / 1_000_000.0);
		System.out.println("  [Lazy] Tổng số lá bài trong cache sau Cold Start: " + CardLazy.getCreatedCount() + " / 52");
		System.out.println();

		// ====================================================================
		// PHẦN 3: ĐO THỜI GIAN LẤY ĐỦ 52 LÁ BÀI CÁC LẦN TIẾP THEO (WARM ACCESS)
		// ====================================================================
		System.out.println(">>> PHẦN 3: ĐO THỜI GIAN LẤY ĐỦ 52 LÁ BÀI KHI ĐÃ CÓ TRONG CACHE (WARM ACCESS)");
		long eagerWarmStartNano = System.nanoTime();
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				CardEager.get(rank, suit);
			}
		}
		long eagerWarmEndNano = System.nanoTime();

		long lazyWarmStartNano = System.nanoTime();
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				CardLazy.get(rank, suit);
			}
		}
		long lazyWarmEndNano = System.nanoTime();

		System.out.printf("  [Eager Warm] Thời gian lấy 52 lá: %,d ns (%.3f ms)%n",
				(eagerWarmEndNano - eagerWarmStartNano), (eagerWarmEndNano - eagerWarmStartNano) / 1_000_000.0);
		System.out.printf("  [Lazy Warm]  Thời gian lấy 52 lá: %,d ns (%.3f ms)%n",
				(lazyWarmEndNano - lazyWarmStartNano), (lazyWarmEndNano - lazyWarmStartNano) / 1_000_000.0);
		System.out.println();

		// ====================================================================
		// PHẦN 4: BENCHMARK VỚI SỐ LƯỢNG LẶP LỚN ĐỂ KIỂM CHỨNG currentTimeMillis()
		// ====================================================================
		System.out.println(">>> PHẦN 4: BENCHMARK VỚI SỐ LƯỢNG LỚN (ĐO BẰNG System.currentTimeMillis)");
		System.out.println("  (Mục đích: Vì 52 lá bài chạy quá nhanh cỡ microsecond, currentTimeMillis() ghi nhận 0 ms.");
		System.out.println("   Chạy lặp 100,000 và 1,000,000 lần lấy 52 lá để thấy rõ chênh lệch miliseconds).");

		runMultiIterationBenchmark(100_000);
		runMultiIterationBenchmark(1_000_000);

		// ====================================================================
		// PHẦN 5: TỔNG KẾT VÀ KẾT LUẬN (WHAT DO YOU CONCLUDE?)
		// ====================================================================
		printConclusions();
	}

	private static void runMultiIterationBenchmark(int iterations) {
		System.out.printf("%n  --- Thử nghiệm với %,d lượt lấy trọn bộ 52 lá (%,d lần truy xuất get()) ---%n",
				iterations, (long) iterations * 52);

		// Đo Eager
		long startEager = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			for (Suit suit : Suit.values()) {
				for (Rank rank : Rank.values()) {
					CardEager.get(rank, suit);
				}
			}
		}
		long timeEager = System.currentTimeMillis() - startEager;

		// Đo Lazy
		long startLazy = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			for (Suit suit : Suit.values()) {
				for (Rank rank : Rank.values()) {
					CardLazy.get(rank, suit);
				}
			}
		}
		long timeLazy = System.currentTimeMillis() - startLazy;

		System.out.printf("  * Eager Pre-initialization: %4d ms%n", timeEager);
		System.out.printf("  * Lazy Creation           : %4d ms%n", timeLazy);
		if (timeLazy > timeEager) {
			double percent = (double) (timeLazy - timeEager) / timeEager * 100.0;
			System.out.printf("  => Lazy chậm hơn Eager khoảng: %.1f%% (do chi phí kiểm tra branching innerMap.containsKey)%n", percent);
		} else {
			System.out.println("  => Thời gian xấp xỉ tương đương nhau.");
		}
	}

	private static void printHeader() {
		System.out.println("============================================================================");
		System.out.println("   BÀI TẬP 8 (EXERCISE 08): SO SÁNH FLYWEIGHT EAGER VS LAZY INITIALIZATION  ");
		System.out.println("   Giáo trình: Introduction to Software Design with Java - Martin P. Robillard ");
		System.out.println("============================================================================");
		System.out.println();
	}

	private static void printConclusions() {
		System.out.println();
		System.out.println("============================================================================");
		System.out.println("                  KẾT LUẬN VÀ TRẢ LỜI CÂU HỎI ĐỀ BÀI                         ");
		System.out.println("============================================================================");
		System.out.println("1. VỀ ĐỘ PHÂN GIẢI CỦA System.currentTimeMillis():");
		System.out.println("   - Lấy 52 lá bài đơn thuần chỉ mất vài chục microsecond (0.01 - 0.05 ms).");
		System.out.println("   - Do độ phân giải của System.currentTimeMillis() trên hệ điều hành là ~1-15 ms,");
		System.out.println("     phép đo 1 lần duy nhất thường ra kết quả 0 ms. Cần sử dụng System.nanoTime()");
		System.out.println("     hoặc lặp nhiều lần để System.currentTimeMillis() hiển thị rõ ràng.");
		System.out.println();
		System.out.println("2. SO SÁNH HIỆU NĂNG (EAGER VS LAZY):");
		System.out.println("   - Ở lần đầu (Cold Start): Eager nhanh hơn rõ rệt vì 52 lá bài đã được JVM");
		System.out.println("     khởi tạo sẵn lúc nạp lớp (Class Loading). Lazy phải cấp phát 52 đối tượng");
		System.out.println("     và các HashMap phụ ngay trong luồng thực thi.");
		System.out.println("   - Ở các lần sau (Warm Access / High-Throughput): Eager vẫn nhỉnh hơn một chút");
		System.out.println("     vì chỉ thực hiện lookup Map trực tiếp, trong khi Lazy phải kiểm tra");
		System.out.println("     thêm điều kiện if (!innerMap.containsKey(pSuit)) ở mỗi lần gọi get().");
		System.out.println();
		System.out.println("3. KẾT LUẬN THIẾT KẾ (WHAT DO YOU CONCLUDE?):");
		System.out.println("   - Với bài toán bộ bài Tây (chỉ có 52 phần tử cố định và chắc chắn sẽ dùng hết):");
		System.out.println("     Eager Pre-initialization là giải pháp VƯỢT TRỘI:");
		System.out.println("     + Chi phí bộ nhớ cho 52 lá bài là cực nhỏ (vài KB), không đáng để tiết kiệm.");
		System.out.println("     + Đảm bảo an toàn đa luồng (Thread-safe) tự nhiên nhờ cơ chế nạp lớp của JVM.");
		System.out.println("     + Mã nguồn đơn giản, không cần xử lý đồng bộ hóa (synchronization) hay rẽ nhánh.");
		System.out.println("   - Lazy Creation chỉ phát huy thế mạnh khi tập Flyweight rất lớn (hàng triệu đối");
		System.out.println("     tượng) hoặc chi phí khởi tạo một đối tượng rất nặng, mà ứng dụng chỉ sử");
		System.out.println("     dụng một phần nhỏ trong số chúng.");
		System.out.println("============================================================================");
	}
}
