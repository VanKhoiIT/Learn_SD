package designbook.solutions6.exercise06;

import designbook.solutions6.IO;

/**
 * Exercise 6.6 Client Demo:
 * Shows unpacking with 'instanceof CompositeShow2' without changing Show interface.
 */
public class Exercise06 {

	private static final Concert CONCERT = new Concert("Concert 1", "Artist X", 100);
	private static final Movie MOVIE1 = new Movie("Movie 1", 2017, 120);
	private static final Movie MOVIE2 = new Movie("Movie 2", 2018, 130);

	public static void main(String[] args) {
		IO.println("================================================================================");
		IO.println("         EXERCISE 6.6: GIẢI PHÁP AN TOÀN (SAFETY APPROACH - BÀI 6)               ");
		IO.println("         Yêu cầu: Unpack composite show mà KHÔNG SỬA ĐỔI interface Show         ");
		IO.println("================================================================================");

		// Tạo cấu trúc composite lồng nhau (Tree structure)
		Show show = new CompositeShow2(CONCERT, new CompositeShow2(MOVIE1, MOVIE2));

		IO.println("[1] Thông tin Show tổng hợp (qua interface Show):");
		IO.println("    Mô tả      : " + show.description());
		IO.println("    Thời lượng : " + show.runningTime() + " phút");

		IO.println("\n[2] Client Unpack show con (BẮT BUỘC dùng 'instanceof CompositeShow2'):");
		if (show instanceof CompositeShow2 compositeShow) {
			int count = 1;
			for (Show subshow : compositeShow) {
				IO.println("    Show con " + (count++) + ": " + subshow.description() + 
				           " [" + subshow.runningTime() + " phút]");
			}
		} else {
			IO.println("    Đối tượng không phải là CompositeShow2, không thể unpack!");
		}

		IO.println("\n================================================================================");
		IO.println("Ghi chú kiến trúc: Nút lá (Movie, Concert) và interface Show hoàn toàn sạch sẽ, ");
		IO.println("nhưng phía Client buộc phải biết đến CompositeShow2 và dùng kiểm tra kiểu.       ");
		IO.println("================================================================================");
	}
}

