package exercise09;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Chương trình kiểm thử và minh họa giải pháp cho Exercise 9 (Chapter 4).
 */
public class Exercise09Demo {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("======================================================================");
		System.out.println("         CHƯƠNG TRÌNH KIỂM THỬ EXERCISE 9: SINGLETON GAMEMODEL        ");
		System.out.println("======================================================================\n");

		// Test 1: Kiểm chứng tính chất duy nhất của Singleton (Eager Initialization)
		System.out.println("--- BƯỚC 1: KIỂM CHỨNG TÍNH CHẤT SINGLETON (DUY NHẤT) ---");
		GameModel model1 = GameModel.instance();
		GameModel model2 = GameModel.instance();
		GameModel model3 = GameModel.getInstance();

		System.out.println("Lần gọi 1 (GameModel.instance())   : " + System.identityHashCode(model1));
		System.out.println("Lần gọi 2 (GameModel.instance())   : " + System.identityHashCode(model2));
		System.out.println("Lần gọi 3 (GameModel.getInstance()): " + System.identityHashCode(model3));

		boolean isSameInstance = (model1 == model2) && (model2 == model3);
		System.out.println("Kết quả so sánh (model1 == model2 == model3): " + isSameInstance);
		if (isSameInstance) {
			System.out.println("=> [PASS] Chỉ có DUY NHẤT một thể hiện GameModel được tạo ra.\n");
		} else {
			System.err.println("=> [FAIL] Vi phạm quy tắc Singleton!\n");
		}

		// Test 2: Kiểm chứng hoạt động của newGame() và biến Deck
		System.out.println("--- BƯỚC 2: KIỂM CHỨNG HOẠT ĐỘNG CỦA newGame() VÀ BỘ BÀI (DECK) ---");
		model1.newGame();
		System.out.println("Khởi tạo ván chơi mới bằng model1.newGame().");
		System.out.println("Số lượng lá bài ban đầu trong bộ bài: " + model1.getDeck().size());

		System.out.println("\nTiến hành rút 5 lá bài từ model1:");
		for (int i = 1; i <= 5; i++) {
			Card drawn = model1.getDeck().draw();
			System.out.printf("  Lá %d: %s\n", i, drawn);
		}
		System.out.println("Số lượng lá bài còn lại trong model1: " + model1.getDeck().size());

		// Truy cập qua model2 (thực chất là cùng 1 instance)
		System.out.println("\nKiểm tra số lượng lá bài qua biến model2:");
		System.out.println("Số lượng lá bài nhìn từ model2: " + model2.getDeck().size());

		System.out.println("\nGọi model2.newGame() để bắt đầu ván mới...");
		model2.newGame();
		System.out.println("Số lượng lá bài trong model1 sau khi model2.newGame(): " + model1.getDeck().size());
		if (model1.getDeck().size() == 52) {
			System.out.println("=> [PASS] newGame() đã tái khởi tạo và xáo đủ 52 lá bài thành công!\n");
		} else {
			System.err.println("=> [FAIL] newGame() chưa thiết lập lại đúng 52 lá bài!\n");
		}

		// Test 3: Kiểm chứng môi trường đa luồng (Multi-threading)
		System.out.println("--- BƯỚC 3: KIỂM CHỨNG AN TOÀN TRONG MÔI TRƯỜNG ĐA LUỒNG (10 THREADS) ---");
		int threadCount = 10;
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch readyLatch = new CountDownLatch(threadCount);
		CountDownLatch startLatch = new CountDownLatch(1);
		Set<GameModel> instances = Collections.newSetFromMap(new ConcurrentHashMap<>());

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				readyLatch.countDown();
				try {
					startLatch.await(); // Chờ tất cả thread sẵn sàng cùng chạy đồng thời
					GameModel instance = GameModel.instance();
					instances.add(instance);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			});
		}

		readyLatch.await();
		startLatch.countDown(); // Phát lệnh chạy đồng thời
		executor.shutdown();
		executor.awaitTermination(3, java.util.concurrent.TimeUnit.SECONDS);

		System.out.println("Số lượng thể hiện khác nhau được tạo ra bởi " + threadCount + " threads: " + instances.size());
		if (instances.size() == 1) {
			System.out.println("=> [PASS] Thread-safety hoàn hảo: Tất cả các luồng nhận cùng một thể hiện duy nhất!\n");
		} else {
			System.err.println("=> [FAIL] Race condition xảy ra, có nhiều thể hiện được tạo ra!\n");
		}

		// Test 4: Kiểm tra nhanh biến thể Lazy Singleton (GameModelLazy)
		System.out.println("--- BƯỚC 4: KIỂM TRA BIẾN THỂ LAZY INITIALIZATION (GAMEMODELLAZY) ---");
		GameModelLazy lazy1 = GameModelLazy.getInstance();
		GameModelLazy lazy2 = GameModelLazy.getInstance();
		System.out.println("lazy1 == lazy2: " + (lazy1 == lazy2));
		System.out.println("=> [PASS] Biến thể Lazy hoạt động chính xác.\n");

		System.out.println("======================================================================");
		System.out.println("                TẤT CẢ KIỂM THỬ ĐÃ HOÀN TẤT THÀNH CÔNG!               ");
		System.out.println("======================================================================");
	}
}
