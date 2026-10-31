/*******************************************************************************
 * Companion code for the book "Introduction to Software Design with Java",
 * 3rd edition by Martin P. Robillard.
 *
 * Exercise 5.11 - Original textbook version containing the seeded bug.
 *******************************************************************************/
package e3.chapter5;

import e3.chapter4.Card;
import e3.chapter4.CardSource;

/**
 * Original version of BiCardSource containing the seeded bug from textbook Exercise 5.11.
 * Notice the bug in draw(): It returns from aSource2 when aSource1 is not empty,
 * and returns from aSource1 when aSource1 is empty!
 */
public class BiCardSourceBuggy implements CardSource {

	private final CardSource aSource1;
	private final CardSource aSource2;

	public BiCardSourceBuggy(CardSource pSource1, CardSource pSource2) {
		aSource1 = pSource1;
		aSource2 = pSource2;
	}

	/*
	 * Draws from source 1 if not empty, and from source 2 if source 1 is empty.
	 * SEEDED BUG:
	 * Inverted logic: if source 1 is NOT empty, it draws from source 2!
	 * If source 1 IS empty, it draws from source 1 (which is empty)!
	 */
	@Override
	public Card draw() {
		if (!aSource1.isEmpty()) {
			return aSource2.draw(); // Seeded bug!
		}
		else {
			return aSource1.draw(); // Seeded bug!
		}
	}

	@Override
	public boolean isEmpty() {
		return aSource1.isEmpty() && aSource2.isEmpty();
	}
}
