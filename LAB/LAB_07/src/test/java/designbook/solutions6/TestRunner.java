package designbook.solutions6;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Self-contained Automated Test Runner for LAB_07.
 * Dispatches test suites for Exercise 6 (exercise06) and Exercise 7 (exercise07).
 */
public class TestRunner {

	private static int totalPassed = 0;
	private static int totalFailed = 0;

	public static void main(String[] args) {
		System.out.println("================================================================================");
		System.out.println("            RUNNING AUTOMATED UNIT TESTS FOR LAB_07 (EXERCISES 6 & 7)           ");
		System.out.println("       Composite Pattern: Safety (Ex 6.6) vs Transparency (Ex 6.7)              ");
		System.out.println("================================================================================");

		// Suite 1: Exercise 6.6 Tests (Safety Approach - Package exercise06)
		runSuite("Exercise06Test (Safety Approach - Giao diện Show giữ nguyên)", 
				designbook.solutions6.exercise06.Exercise06Test.class);

		// Suite 2: Exercise 6.7 Tests (Transparency Approach - Package exercise07)
		runSuite("Exercise07Test (Transparency Approach - Giao diện Show đã sửa đổi)", 
				designbook.solutions6.exercise07.Exercise07Test.class);

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
						|| method.getName().startsWith("test")) {
					testMethods.add(method);
				}
			}

			// Sort by method name for deterministic test execution order
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
