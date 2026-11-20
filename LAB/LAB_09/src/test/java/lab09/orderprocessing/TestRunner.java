package lab09.orderprocessing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Test Runner tự động chạy qua Reflection cho LAB_09 (không cần plugin IDE).
 */
public class TestRunner {

    private static int totalPassed = 0;
    private static int totalFailed = 0;

    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8.name()));
            System.setErr(new java.io.PrintStream(System.err, true, java.nio.charset.StandardCharsets.UTF_8.name()));
        } catch (Exception ignored) {}

        System.out.println("================================================================================");
        System.out.println("          RUNNING AUTOMATED UNIT TESTS FOR LAB_09 (TEMPLATE METHOD)             ");
        System.out.println("             OrderProcessor Pipeline & Multi-Channel Fulfillment                ");
        System.out.println("================================================================================");

        runSuite("OrderProcessorTest (Template Method Algorithm Skeleton, Hooks & Dispatch)", OrderProcessorTest.class);

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(String.format("OVERALL TEST RESULTS: %d PASSED, %d FAILED, TOTAL TESTS: %d",
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

            for (Method testMethod : testMethods) {
                Object testInstance = testClass.getDeclaredConstructor().newInstance();

                for (Method beforeMethod : beforeMethods) {
                    beforeMethod.invoke(testInstance);
                }

                String displayName = testMethod.getName();
                if (testMethod.isAnnotationPresent(org.junit.jupiter.api.DisplayName.class)) {
                    displayName = testMethod.getAnnotation(org.junit.jupiter.api.DisplayName.class).value();
                }

                try {
                    testMethod.invoke(testInstance);
                    System.out.println(String.format("  [PASS] %-70s", displayName));
                    totalPassed++;
                } catch (Exception e) {
                    System.out.println(String.format("  [FAIL] %-70s -> %s", displayName,
                            e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
                    totalFailed++;
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khởi tạo Test Suite: " + e.getMessage());
            totalFailed++;
        }
    }
}

