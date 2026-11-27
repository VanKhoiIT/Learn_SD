package designbook.solutions8;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import designbook.solutions8.exercise06.Exercise06Test;
import designbook.solutions8.exercise07.Exercise07Test;

/**
 * Self-contained Automated Test Runner for LAB_10.
 * Executes test suites for Exercises 8.6 and 8.7 via reflection.
 */
public class TestRunner {

	private static int totalPassed = 0;
	private static int totalFailed = 0;

	public static void main(String[] args) {
		System.out.println("================================================================================");
		System.out.println("            RUNNING AUTOMATED UNIT TESTS FOR LAB_10 (EXERCISES 8.6-8.7)         ");
		System.out.println("         Observer Pattern: Push vs Pull Data-Flow & Interface Segregation       ");
		System.out.println("================================================================================");

		runSuite("Exercise06Test (Observer Pattern - Push Strategy & Single Observer Interface)",
				Exercise06Test.class);

		runSuite("Exercise07Test (Observer Pattern - Pull Strategy, Segregated Interfaces & ISP)",
				Exercise07Test.class);

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
				} catch (Throwable t) {
					Throwable cause = t.getCause() != null ? t.getCause() : t;
					System.out.println("  [FAIL] " + method.getName() + " -> " + cause.getMessage());
					totalFailed++;
				}
			}
		} catch (Exception e) {
			System.out.println("  [ERROR] Could not execute suite " + suiteName + ": " + e.getMessage());
			totalFailed++;
		}
	}
}

