package exercise08;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Cài đặt mẫu Flyweight cho quân bài theo chiến lược LAZY INITIALIZATION (Khởi tạo lười / On-demand).
 * Các đối tượng CardLazy chỉ được khởi tạo khi có yêu cầu lần đầu tiên thông qua hàm get().
 * (Theo đúng cấu trúc lời giải chuẩn Card8 của tác giả trong giáo trình)
 */
public class CardLazy {

	// Flyweight Store lưu trữ các đối tượng CardLazy đã được tạo
	private static final Map<Rank, Map<Suit, CardLazy>> CARDS = new IdentityHashMap<>();

	private final Rank aRank;
	private final Suit aSuit;

	// Constructor private
	private CardLazy(Rank pRank, Suit pSuit) {
		assert pRank != null && pSuit != null;
		aRank = pRank;
		aSuit = pSuit;
	}

	/**
	 * Lấy đối tượng CardLazy duy nhất từ kho lưu trữ.
	 * Nếu chưa có trong cache, đối tượng sẽ được khởi tạo mới một cách lười (lazily) và lưu vào Map.
	 * 
	 * @param pRank Thứ hạng quân bài
	 * @param pSuit Chất quân bài
	 * @return Đối tượng CardLazy duy nhất
	 */
	public static CardLazy get(Rank pRank, Suit pSuit) {
		assert pRank != null && pSuit != null;
		Map<Suit, CardLazy> innerMap = CARDS.getOrDefault(pRank, new IdentityHashMap<>());
		CARDS.putIfAbsent(pRank, innerMap);
		if (!innerMap.containsKey(pSuit)) {
			innerMap.put(pSuit, new CardLazy(pRank, pSuit));
		}
		return innerMap.get(pSuit);
	}

	/**
	 * Đếm số lượng đối tượng CardLazy thực tế đã được khởi tạo trong bộ nhớ cache.
	 */
	public static int getCreatedCount() {
		int count = 0;
		for (Map<Suit, CardLazy> innerMap : CARDS.values()) {
			count += innerMap.size();
		}
		return count;
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
