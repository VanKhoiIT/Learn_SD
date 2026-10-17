package exercise09;

/**
 * Biến thể Lazy Initialization (Khởi tạo lười - Thread-Safe với Double-Checked Locking).
 * 
 * Thể hiện này chỉ được tạo khi có lời gọi đầu tiên tới getInstance(),
 * giúp tiết kiệm tài nguyên nếu đối tượng nặng và chưa cần dùng ngay.
 */
public class GameModelLazy {

	// volatile để đảm bảo tính visibility và thứ tự ghi nhớ giữa các thread
	private static volatile GameModelLazy instance;
	
	private Deck aDeck;

	private GameModelLazy() {
		this.aDeck = new Deck();
	}

	public static GameModelLazy getInstance() {
		if (instance == null) {
			synchronized (GameModelLazy.class) {
				if (instance == null) {
					instance = new GameModelLazy();
				}
			}
		}
		return instance;
	}

	public void newGame() {
		aDeck.shuffle();
	}

	public Deck getDeck() {
		return aDeck;
	}
}
