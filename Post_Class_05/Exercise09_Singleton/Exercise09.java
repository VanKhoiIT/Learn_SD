package exercise09;

/**
 * Lớp chạy chính theo đúng cấu trúc lời giải chuẩn của tác giả trong giáo trình.
 */
public class Exercise09 {

	public static void main(String[] args) {
		// Lấy thể hiện duy nhất của GameModel và gọi newGame()
		GameModel.instance().newGame();
		System.out.println("Khoi tao tro choi moi thanh cong qua Singleton GameModel!");
		System.out.println("So la bai trong bo bai sau khi newGame(): " + GameModel.instance().getDeck().size());
	}
}
