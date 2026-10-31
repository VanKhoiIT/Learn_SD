/*******************************************************************************
 * Companion code for the book "Introduction to Software Design with Java",
 * 3rd edition by Martin P. Robillard.
 *
 * Exercise 5.11 - Fixed version achieving 100% Branch Coverage.
 *******************************************************************************/
package e3.chapter5;

import e3.chapter4.Card;
import e3.chapter4.CardSource;

/**
 * A composite {@link CardSource} that sequentially draws cards from two card sources.
 * It draws from source 1 until source 1 is empty, then draws from source 2 until
 * source 2 is empty.
 */
public class BiCardSource implements CardSource {

	private final CardSource aSource1;
	private final CardSource aSource2;

	/**
	 * Creates a new BiCardSource from two delegate sources.
	 * 
	 * @param pSource1 The primary card source to draw from first.
	 * @param pSource2 The secondary card source to draw from after primary is exhausted.
	 * @pre pSource1 != null && pSource2 != null
	 */
	public BiCardSource(CardSource pSource1, CardSource pSource2) {
		assert pSource1 != null && pSource2 != null : "Source components cannot be null";
		aSource1 = pSource1;
		aSource2 = pSource2;
	}

	/**
	 * Draws from source 1 if not empty, and from source 2 if source 1 is empty.
	 * 
	 * @return The next card from source 1, or from source 2 if source 1 is empty.
	 * @pre !isEmpty()
	 */
	@Override
	public Card draw() {
		assert !isEmpty() : "Cannot draw from an empty BiCardSource";
		if (!aSource1.isEmpty()) {
			return aSource1.draw();
		}
		else {
			return aSource2.draw();
		}
	}

	/**
	 * Returns true if both underlying card sources are empty.
	 * 
	 * @return True if both sources are empty, false otherwise.
	 */
	@Override
	public boolean isEmpty() {
		return aSource1.isEmpty() && aSource2.isEmpty();
	}
}
