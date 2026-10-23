package exercise08;

/**
 * Lớp chạy kiểm thử cơ bản theo đúng mẫu Exercise08 của tác giả giáo trình.
 */
public class Exercise08 {

	public static void main(String[] args) {
		// Thử nghiệm lấy 2 lá bài cùng Rank và Suit từ CardLazy
		CardLazy card1 = CardLazy.get(Rank.ACE, Suit.CLUBS);
		CardLazy card2 = CardLazy.get(Rank.ACE, Suit.CLUBS);

		System.out.println("Lá bài 1: " + card1);
		System.out.println("Lá bài 2: " + card2);
		System.out.println("Are they the same object? " + (card1 == card2));
		System.out.println("Tổng số lá bài đã tạo trong cache: " + CardLazy.getCreatedCount());
	}
}
