package designbook.solutions8;

import designbook.solutions8.common.IO;
import designbook.solutions8.exercise06.Exercise06;
import designbook.solutions8.exercise07.Exercise07;

/**
 * Runs both Exercise 8.6 and Exercise 8.7 demonstrations sequentially.
 */
public class AllDemosRunner {

	public static void main(String[] args) {
		IO.println("################################################################################");
		IO.println("                LAB 10 - COMPREHENSIVE DEMONSTRATION RUNNER                     ");
		IO.println("       Exercises 8.6 & 8.7: Observer Pattern (Push vs Pull & ISP)               ");
		IO.println("################################################################################\n");

		Exercise06.main(args);
		IO.println("\n--------------------------------------------------------------------------------\n");
		Exercise07.main(args);

		IO.println("\n################################################################################");
		IO.println("               ALL LAB 10 DEMONSTRATIONS EXECUTED SUCCESSFULLY!                 ");
		IO.println("################################################################################");
	}
}

