package designbook.solutions5;

import java.lang.reflect.Method;

/**
 * Trình chạy kiểm thử tự động độc lập cho dự án Post_Class_06.
 */
public class TestRunner {

	private static int totalPassed = 0;
	private static int totalFailed = 0;

	public static void main(String[] args) {
		System.out.println("================================================================================");
		System.out.println("             RUNNING AUTOMATED UNIT TESTS FOR POST_CLASS_06                     ");
		System.out.println("       (Exercises 5.8, 5.9, 5.10 - Reflection, Statements & Branches)           ");
		System.out.println("================================================================================");

		runSuite("TestPokerHand (Exercise 8, 9, 10 Suite)", TestPokerHand.class);

		System.out.println("--------------------------------------------------------------------------------");
		System.out.println(String.format("OVERALL RESULTS: %d PASSED, %d FAILED, TOTAL TESTS: %d",
				totalPassed, totalFailed, totalPassed + totalFailed));
		System.out.println("================================================================================");

		if (totalFailed > 0) {
			System.exit(1);
		}
	}

	private static void runSuite(String suiteName, Class<?> testClass) {
		System.out.println("\n[TEST SUITE] " + suiteName);
		try {
			Object instance = testClass.getDeclaredConstructor().newInstance();
			for (Method method : testClass.getDeclaredMethods()) {
				if (method.isAnnotationPresent(org.junit.jupiter.api.Test.class) 
						|| method.getName().startsWith("test")) {
					try {
						method.setAccessible(true);
						method.invoke(instance);
						System.out.println("  [PASS] " + method.getName());
						totalPassed++;
					}
					catch (Throwable t) {
						Throwable cause = t.getCause() != null ? t.getCause() : t;
						System.out.println("  [FAIL] " + method.getName() + " -> " + cause.getMessage());
						cause.printStackTrace();
						totalFailed++;
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println("  [ERROR] Could not instantiate suite " + suiteName + ": " + e.getMessage());
			totalFailed++;
		}
	}
}
