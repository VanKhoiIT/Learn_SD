package exercise08;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Cài đặt mẫu Flyweight cho quân bài theo chiến lược EAGER INITIALIZATION (Khởi tạo sớm / Pre-initialization).
 * Toàn bộ 52 đối tượng CardEager được tạo sẵn ngay khi nạp lớp (Class Loading) thông qua khối static.
 */
public class CardEager {

	// Flyweight Store lưu trữ các đối tượng CardEager dùng Map lồng nhau
	private static final Map<Rank, Map<Suit, CardEager>> CARDS = new IdentityHashMap<>();

	private final Rank aRank;
	private final Suit aSuit;

	// Khối khởi tạo tĩnh (Static Initialization Block) - Pre-initialization
	static {
		for (Rank rank : Rank.values()) {
			Map<Suit, CardEager> innerMap = new IdentityHashMap<>();
			for (Suit suit : Suit.values()) {
				innerMap.put(suit, new CardEager(rank, suit));
			}
			CARDS.put(rank, innerMap);
		}
	}

	// Constructor private ngăn chặn việc khởi tạo tùy tiện từ bên ngoài
	private CardEager(Rank pRank, Suit pSuit) {
		assert pRank != null && pSuit != null;
		aRank = pRank;
		aSuit = pSuit;
	}

	/**
	 * Lấy đối tượng CardEager duy nhất từ kho lưu trữ Flyweight.
	 * 
	 * @param pRank Thứ hạng quân bài
	 * @param pSuit Chất quân bài
	 * @return Đối tượng CardEager duy nhất tương ứng
	 */
	public static CardEager get(Rank pRank, Suit pSuit) {
		assert pRank != null && pSuit != null;
		return CARDS.get(pRank).get(pSuit);
	}

	public Rank rank() {
		return aRank;
	}

	public Suit suit() {
		return aSuit;
	}

	@Override
	public String toString() {
		return String.format("%s of %s", aRank, aSuit);
	}
}
