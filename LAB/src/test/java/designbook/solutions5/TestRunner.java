package designbook.solutions5;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Trình chạy kiểm thử tự động độc lập cho LAB (Exercise 5.11).
 * Sử dụng Java Reflection API với cơ chế xUnit Isolated Execution (new instance + @BeforeEach).
 */
public class TestRunner {

	private static int totalPassed = 0;
	private static int totalFailed = 0;

	public static void main(String[] args) {
		System.out.println("================================================================================");
		System.out.println("        RUNNING AUTOMATED UNIT TESTS FOR LAB (EXERCISE 5.11)                  ");
		System.out.println("     BiCardSource Test Suite with Stubs & 100% Branch Coverage (code-e3)        ");
		System.out.println("================================================================================");

		// Run fixed Exercise 11 suite
		runSuite("Exercise11 (BiCardSource Fixed - 100% Branch Coverage)", Exercise11.class);

		// Run Seeded Bug Demonstrator
		runSuite("Exercise11BugDemonstrator (Seeded Bug Verification)", Exercise11BugDemonstrator.class);

		System.out.println("--------------------------------------------------------------------------------");
		System.out.println(String.format("OVERALL RESULTS: %d PASSED, %d FAILED, TOTAL TESTS: %d",
				totalPassed, totalFailed, totalPassed + totalFailed));
		System.out.println("================================================================================");

		if (totalFailed > 0) {
			System.exit(1);
		}
	}

	public static void runSuite(String suiteName, Class<?> testClass) {
		System.out.println("\n[TEST SUITE] " + suiteName);
		try {
			List<Method> beforeMethods = new ArrayList<>();
			List<Method> testMethods = new ArrayList<>();

			for (Method method : testClass.getDeclaredMethods()) {
				if (method.isAnnotationPresent(org.junit.jupiter.api.BeforeEach.class)) {
					beforeMethods.add(method);
				}
				if (method.isAnnotationPresent(org.junit.jupiter.api.Test.class) 
						|| method.getName().startsWith("test")
						|| method.getName().startsWith("demonstrate")) {
					testMethods.add(method);
				}
			}

			// Sort by name for deterministic order
			testMethods.sort((m1, m2) -> m1.getName().compareTo(m2.getName()));

			for (Method method : testMethods) {
				Object instance = testClass.getDeclaredConstructor().newInstance();
				try {
					for (Method bm : beforeMethods) {
						bm.setAccessible(true);
						bm.invoke(instance);
					}
					method.setAccessible(true);
					method.invoke(instance);
					System.out.println("  [PASS] " + method.getName());
					totalPassed++;
				}
				catch (Throwable t) {
					Throwable cause = t.getCause() != null ? t.getCause() : t;
					System.out.println("  [FAIL] " + method.getName() + " -> " + cause.getMessage());
					totalFailed++;
				}
			}
		}
		catch (Exception e) {
			System.out.println("  [ERROR] Could not execute suite " + suiteName + ": " + e.getMessage());
			totalFailed++;
		}
	}
}
