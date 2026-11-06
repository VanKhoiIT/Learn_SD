package designbook.solutions6.exercise07;

import designbook.solutions6.IO;

/**
 * Exercise 6.7 Client Demo:
 * Shows pure polymorphic unpacking WITHOUT instanceof.
 */
public class Exercise07 {

	private static final Concert CONCERT = new Concert("Concert 1", "Artist X", 100);
	private static final Movie MOVIE1 = new Movie("Movie 1", 2017, 120);
	private static final Movie MOVIE2 = new Movie("Movie 2", 2018, 130);

	public static void main(String[] args) {
		IO.println("================================================================================");
		IO.println("        EXERCISE 6.7: GIẢI PHÁP TRONG SUỐT (TRANSPARENCY APPROACH - BÀI 7)      ");
		IO.println("        Yêu cầu: Sửa đổi interface Show để kế thừa Iterable<Show>               ");
		IO.println("================================================================================");

		// Tạo cấu trúc composite lồng nhau với kiểu Show đã sửa đổi
		Show show = new CompositeShow(CONCERT, new CompositeShow(MOVIE1, MOVIE2));

		IO.println("[1] Thông tin Show tổng hợp (qua interface Show đã sửa đổi):");
		IO.println("    Mô tả      : " + show.description());
		IO.println("    Thời lượng : " + show.runningTime() + " phút");

		IO.println("\n[2] Client Unpack show con (DUYỆT ĐA HÌNH TRỰC TIẾP, KHÔNG CẦN instanceof!):");
		int count = 1;
		for (Show subshow : show) {
			IO.println("    Show con " + (count++) + ": " + subshow.description() + 
			           " [" + subshow.runningTime() + " phút]");
		}

		IO.println("\n[3] Duyệt trên nút lá Movie (không gây ngoại lệ nhờ Default Empty Iterator):");
		Show leaf = MOVIE1;
		IO.println("    Đang kiểm tra nút lá: " + leaf.description());
		int leafChildren = 0;
		for (Show ignored : leaf) {
			leafChildren++;
		}
		IO.println("    Số phần tử con: " + leafChildren + " (kết thúc an toàn)");

		IO.println("\n================================================================================");
		IO.println("Ghi chú kiến trúc: Mã Client cực kỳ tinh gọn, không cần ép kiểu hay phân nhánh.  ");
		IO.println("Đổi lại, giao diện Show bị ô nhiễm vì nút lá cũng phải mang phương thức iterator.");
		IO.println("================================================================================");
	}
}

