package exercise09;

import exercise09.Deck.Shuffler;

/**
 * File Client gốc từ Chapter 4 của tác giả Martin P. Robillard,
 * được bổ sung thêm phần kiểm thử Bài tập 9 (Singleton GameModel).
 */
public class Client {

	public static void main(String[] args) {
		// 1. Illustration of the use of the Null object
		CardSource source = new Deck();
		System.out.println("Null Object check (Deck): " + source.isNull());
		source = CardSource.NULL;
		System.out.println("Null Object check (CardSource.NULL): " + source.isNull());

		// 2. Illustration of the use of the Shuffler nested class
		Deck deck = new Deck();
		Shuffler shuffler = deck.newShuffler();
		shuffler.shuffle();
		shuffler.shuffle();
		deck.shuffle();
		System.out.println("Shuffler count: " + shuffler.shuffles());

		// 3. Illustration of the use of the createByRankComparator static factory method
		System.out.println("Rank comparator result: " + Deck.createRankComparator(Rank.ACE).compare(new Deck(), new Deck()));

		// 4. KIỂM THỬ BÀI TẬP 9 (EXERCISE 9 - SINGLETON GAMEMODEL):
		System.out.println("\n--- KIỂM TRA BÀI TẬP 9 (GAMEMODEL SINGLETON) ---");
		GameModel model1 = GameModel.instance();
		GameModel model2 = GameModel.instance();
		System.out.println("Kiem tra Singleton (model1 == model2): " + (model1 == model2));

		model1.newGame();
		System.out.println("Goi newGame() khoi tao van moi thanh cong!");
	}
}
