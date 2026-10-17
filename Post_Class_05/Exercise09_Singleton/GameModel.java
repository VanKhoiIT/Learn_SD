package exercise09;

/**
 * GameModel class designed using the Singleton Design Pattern.
 * 
 * Chapter 4 - Exercise 9:
 * "Using the Singleton Design Pattern, design a GameModel class that is a Singleton.
 * The class should have a single instance variable of type Deck and initialize the deck
 * whenever a newGame() method is called. The class can remain a stub, meaning that it
 * does not need to do anything else."
 * 
 * Key Singleton elements:
 * 1. Single static instance stored in a private static field.
 * 2. Private constructor to prevent direct instantiation with 'new'.
 * 3. Public static accessor method (instance() / getInstance()) to provide global access point.
 */
public class GameModel {

	// 1. Singleton Instance: Eager initialization ensures thread-safety without synchronization overhead.
	private static final GameModel INSTANCE = new GameModel();

	// 2. Single instance variable of type Deck
	private final Deck aDeck = new Deck();

	// 3. Private constructor prevents instantiation from outside
	private GameModel() {}

	/**
	 * Access point for the Singleton instance (official method in the textbook).
	 * @return The unique GameModel instance.
	 */
	public static GameModel instance() {
		return INSTANCE;
	}

	/**
	 * Standard alias for instance() adhering to common Java naming conventions.
	 * @return The unique GameModel instance.
	 */
	public static GameModel getInstance() {
		return INSTANCE;
	}

	/**
	 * Initializes / resets the deck whenever a new game begins.
	 * In Deck, calling shuffle() clears existing cards, re-populates all 52 cards,
	 * and shuffles them randomly.
	 */
	public void newGame() {
		aDeck.shuffle();
	}

	/**
	 * Provides access to the game's deck (useful for gameplay logic and testing).
	 * @return The current Deck instance.
	 */
	public Deck getDeck() {
		return aDeck;
	}
}
