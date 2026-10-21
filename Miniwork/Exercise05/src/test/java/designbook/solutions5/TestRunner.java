package designbook.solutions5;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Self-contained Automated Test Runner for Exercise05 and Exercise05Enhanced.
 * Can be run directly without external testing libraries or via IDE.
 */
public class TestRunner {

	private static int passedCount = 0;
	private static int failedCount = 0;

	public static void main(String[] args) {
		IO.println("=================================================");
		IO.println("  RUNNING AUTOMATED UNIT TESTS FOR EXERCISE 05   ");
		IO.println("=================================================");

		testValidTypes();
		testExtendedTypes();
		testInvalidFormats();
		testInvalidTypes();

		IO.println("-------------------------------------------------");
		IO.println(String.format("Results: %d Passed, %d Failed, Total: %d", 
				passedCount, failedCount, passedCount + failedCount));
		IO.println("=================================================");

		if (failedCount > 0) {
			System.exit(1);
		}
	}

	private static void testValidTypes() {
		IO.println("\n[Suite 1: Standard java.lang Number Types]");
		
		// 1. Integer
		assertTest("Integer 5", () -> {
			Number num = Exercise05Enhanced.parseNumber("Integer", "5");
			assertEquals(Integer.valueOf(5), num);
			assertEquals("java.lang.Integer", num.getClass().getName());
		});

		// 2. Double
		assertTest("Double 5.1", () -> {
			Number num = Exercise05Enhanced.parseNumber("Double", "5.1");
			assertEquals(Double.valueOf(5.1), num);
			assertEquals("java.lang.Double", num.getClass().getName());
		});

		// 3. Float
		assertTest("Float 3.14", () -> {
			Number num = Exercise05Enhanced.parseNumber("Float", "3.14");
			assertEquals(Float.valueOf(3.14f), num);
			assertEquals("java.lang.Float", num.getClass().getName());
		});

		// 4. Long
		assertTest("Long 99999999999", () -> {
			Number num = Exercise05Enhanced.parseNumber("Long", "99999999999");
			assertEquals(Long.valueOf(99999999999L), num);
			assertEquals("java.lang.Long", num.getClass().getName());
		});

		// 5. Short
		assertTest("Short 32000", () -> {
			Number num = Exercise05Enhanced.parseNumber("Short", "32000");
			assertEquals(Short.valueOf((short) 32000), num);
			assertEquals("java.lang.Short", num.getClass().getName());
		});

		// 6. Byte
		assertTest("Byte 120", () -> {
			Number num = Exercise05Enhanced.parseNumber("Byte", "120");
			assertEquals(Byte.valueOf((byte) 120), num);
			assertEquals("java.lang.Byte", num.getClass().getName());
		});
	}

	private static void testExtendedTypes() {
		IO.println("\n[Suite 2: Extended java.math Number Types]");

		// 7. BigInteger
		assertTest("BigInteger 12345678901234567890", () -> {
			Number num = Exercise05Enhanced.parseNumber("BigInteger", "12345678901234567890");
			assertEquals(new BigInteger("12345678901234567890"), num);
			assertEquals("java.math.BigInteger", num.getClass().getName());
		});

		// 8. BigDecimal
		assertTest("BigDecimal 123456.789012", () -> {
			Number num = Exercise05Enhanced.parseNumber("BigDecimal", "123456.789012");
			assertEquals(new BigDecimal("123456.789012"), num);
			assertEquals("java.math.BigDecimal", num.getClass().getName());
		});
	}

	private static void testInvalidFormats() {
		IO.println("\n[Suite 3: Invalid Number Formats]");

		// 9. Integer with non-numeric string
		assertTest("Integer with 'abc' should throw NumberFormatException", () -> {
			try {
				Exercise05Enhanced.parseNumber("Integer", "abc");
				throw new AssertionError("Expected NumberFormatException");
			} catch (NumberFormatException e) {
				// expected
			}
		});

		// 10. Integer with decimal point
		assertTest("Integer with '5.5' should throw NumberFormatException", () -> {
			try {
				Exercise05Enhanced.parseNumber("Integer", "5.5");
				throw new AssertionError("Expected NumberFormatException");
			} catch (NumberFormatException e) {
				// expected
			}
		});
	}

	private static void testInvalidTypes() {
		IO.println("\n[Suite 4: Invalid / Non-Number Types]");

		// 11. Unknown class
		assertTest("Non-existent class 'NoSuchNumber' should throw ClassNotFoundException", () -> {
			try {
				Exercise05Enhanced.parseNumber("NoSuchNumber", "123");
				throw new AssertionError("Expected ClassNotFoundException");
			} catch (ClassNotFoundException e) {
				// expected
			}
		});

		// 12. Valid class, but not a subtype of Number (e.g. String or Boolean)
		assertTest("Class 'Boolean' is not a Number subtype", () -> {
			try {
				Exercise05Enhanced.parseNumber("Boolean", "true");
				throw new AssertionError("Expected IllegalArgumentException");
			} catch (IllegalArgumentException e) {
				// expected
			}
		});
	}

	private static void assertTest(String testName, TestCase testCase) {
		try {
			testCase.run();
			IO.println("  [PASS] " + testName);
			passedCount++;
		}
		catch (Throwable t) {
			IO.println("  [FAIL] " + testName + " -> " + t.getMessage());
			failedCount++;
		}
	}

	private static void assertEquals(Object expected, Object actual) {
		if (expected == null && actual == null) return;
		if (expected != null && expected.equals(actual)) return;
		throw new AssertionError(String.format("Expected: %s, Actual: %s", expected, actual));
	}

	@FunctionalInterface
	private interface TestCase {
		void run() throws Throwable;
	}
}
