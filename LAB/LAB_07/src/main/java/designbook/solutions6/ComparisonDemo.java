package designbook.solutions6;

import java.util.Iterator;

/**
 * Architectural Comparison Demonstration:
 * Directly compares Exercise 6.6 (Safety Approach) vs Exercise 6.7 (Transparency Approach).
 */
public class ComparisonDemo {

	public static void main(String[] args) {
		IO.println("================================================================================");
		IO.println("       ARCHITECTURAL COMPARISON: EXERCISE 6.6 VS EXERCISE 6.7 (COMPOSITE)       ");
		IO.println("           Safety Approach (Ex 6.6) vs Transparency Approach (Ex 6.7)           ");
		IO.println("================================================================================");

		// 1. Setup sample hierarchy for Exercise 6.6 (Package exercise06)
		designbook.solutions6.exercise06.Concert concert1 = 
				new designbook.solutions6.exercise06.Concert("Rock Fest", "Band Alpha", 90);
		designbook.solutions6.exercise06.Movie movie1_1 = 
				new designbook.solutions6.exercise06.Movie("Inception", 2010, 148);
		designbook.solutions6.exercise06.Movie movie1_2 = 
				new designbook.solutions6.exercise06.Movie("Interstellar", 2014, 169);
		designbook.solutions6.exercise06.CompositeShow2 safetyRoot = 
				new designbook.solutions6.exercise06.CompositeShow2(concert1, 
						new designbook.solutions6.exercise06.CompositeShow2(movie1_1, movie1_2));

		// 2. Setup sample hierarchy for Exercise 6.7 (Package exercise07)
		designbook.solutions6.exercise07.Concert concert2 = 
				new designbook.solutions6.exercise07.Concert("Rock Fest", "Band Alpha", 90);
		designbook.solutions6.exercise07.Movie movie2_1 = 
				new designbook.solutions6.exercise07.Movie("Inception", 2010, 148);
		designbook.solutions6.exercise07.Movie movie2_2 = 
				new designbook.solutions6.exercise07.Movie("Interstellar", 2014, 169);
		designbook.solutions6.exercise07.CompositeShow transRoot = 
				new designbook.solutions6.exercise07.CompositeShow(concert2, 
						new designbook.solutions6.exercise07.CompositeShow(movie2_1, movie2_2));

		// SECTION 1: CLIENT CODE INTERACTION
		IO.println("\n--- [SECTION 1] CLIENT CODE INTERACTION ---");
		IO.println("[Exercise 6.6 - Safety]: Client must check 'instanceof' and downcast:");
		designbook.solutions6.exercise06.Show abstractShow1 = safetyRoot;
		if (abstractShow1 instanceof designbook.solutions6.exercise06.CompositeShow2 comp) {
			IO.println("  ✓ Type check succeeded: 'instanceof CompositeShow2'");
			for (designbook.solutions6.exercise06.Show s : comp) {
				IO.println("    -> Found: " + s.description());
			}
		}

		IO.println("\n[Exercise 6.7 - Transparency]: Client iterates directly on abstract interface (Show):");
		designbook.solutions6.exercise07.Show abstractShow2 = transRoot;
		IO.println("  ✓ No 'instanceof' required. Direct iteration via Show:");
		for (designbook.solutions6.exercise07.Show s : abstractShow2) {
			IO.println("    -> Found: " + s.description());
		}

		// SECTION 2: HANDLING LEAF NODES
		IO.println("\n--- [SECTION 2] ITERATION ON LEAF NODES (SINGLE MOVIE) ---");
		IO.println("[Exercise 6.6 - Safety]: Leaf 'Movie' does NOT implement Iterable.");
		designbook.solutions6.exercise06.Show leaf1 = movie1_1;
		boolean isIterable6 = (leaf1 instanceof Iterable<?>);
		IO.println("  Does leaf implement Iterable? " + isIterable6 + " (Interface is clean, no unused methods)");

		IO.println("\n[Exercise 6.7 - Transparency]: Leaf 'Movie' implements Show (thus Iterable<Show>).");
		designbook.solutions6.exercise07.Show leaf2 = movie2_1;
		int loopIterations = 0;
		for (designbook.solutions6.exercise07.Show ignored : leaf2) {
			loopIterations++;
		}
		IO.println("  Does leaf implement Iterable? true (Returns Collections.emptyIterator())");
		IO.println("  Iterations executed on Leaf: " + loopIterations + " (Safe execution, 0 iterations)");

		// SECTION 3: IMMUTABILITY & ENCAPSULATION AUDIT
		IO.println("\n--- [SECTION 3] IMMUTABILITY & ENCAPSULATION AUDIT ---");
		auditImmutability("CompositeShow2 (Ex 6.6)", safetyRoot.iterator());
		auditImmutability("CompositeShow (Ex 6.7)", transRoot.iterator());

		// SECTION 4: ARCHITECTURAL COMPARISON MATRIX
		IO.println("\n--- [SECTION 4] ARCHITECTURAL COMPARISON MATRIX ---");
		IO.println(String.format("%-25s | %-25s | %-25s", "CRITERIA", "EXERCISE 6.6 (SAFETY)", "EXERCISE 6.7 (TRANSPARENCY)"));
		IO.println("-".repeat(82));
		IO.println(String.format("%-25s | %-25s | %-25s", "Show Interface", "Unmodified (clean)", "Modified (extends Iterable)"));
		IO.println(String.format("%-25s | %-25s | %-25s", "Leaf Nodes", "No iterator method", "Inherits empty iterator"));
		IO.println(String.format("%-25s | %-25s | %-25s", "Client Unpacking", "Requires 'instanceof'", "Pure Polymorphic for-each"));
		IO.println(String.format("%-25s | %-25s | %-25s", "Interface Segregation", "Strictly Respected", "Polluted (Leaf has iterator)"));
		IO.println(String.format("%-25s | %-25s | %-25s", "Open-Closed Principle", "Client modified on new type", "Client unchanged for all"));
		IO.println(String.format("%-25s | %-25s | %-25s", "Liskov Substitution", "Strictly Respected", "Respected via default method"));
		IO.println(String.format("%-25s | %-25s | %-25s", "Recommended In Practice", "When safety & ISP matter", "When uniform client code is key"));
		IO.println("================================================================================");
	}

	private static void auditImmutability(String name, Iterator<?> it) {
		try {
			if (it.hasNext()) {
				it.next();
				it.remove();
				IO.println("  [FAIL] " + name + " allowed iterator.remove()!");
			}
		} catch (UnsupportedOperationException e) {
			IO.println("  [PASS] " + name + " protected via Collections.unmodifiableList -> Threw UnsupportedOperationException");
		} catch (Exception e) {
			IO.println("  [WARN] " + name + " threw unexpected exception: " + e.getMessage());
		}
	}
}
