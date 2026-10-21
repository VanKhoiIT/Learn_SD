package designbook.solutions5;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Trình chạy tự động kiểm thử đơn vị (Automated Unit Test Runner) cho Exercise05.
 * Kiểm tra 12 kịch bản từ cơ bản đến nâng cao.
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
        IO.println(String.format("Kết quả: %d Passed, %d Failed, Tổng số: %d", 
                passedCount, failedCount, passedCount + failedCount));
        IO.println("=================================================");

        if (failedCount > 0) {
            System.exit(1);
        }
    }

    private static void testValidTypes() {
        IO.println("\n[Nhóm 1: Các kiểu số chuẩn trong java.lang]");
        
        assertTest("Integer 5", () -> {
            Number num = Exercise05Enhanced.parseNumber("Integer", "5");
            assertEquals(Integer.valueOf(5), num);
            assertEquals("java.lang.Integer", num.getClass().getName());
        });

        assertTest("Double 5.1", () -> {
            Number num = Exercise05Enhanced.parseNumber("Double", "5.1");
            assertEquals(Double.valueOf(5.1), num);
            assertEquals("java.lang.Double", num.getClass().getName());
        });

        assertTest("Float 3.14", () -> {
            Number num = Exercise05Enhanced.parseNumber("Float", "3.14");
            assertEquals(Float.valueOf(3.14f), num);
            assertEquals("java.lang.Float", num.getClass().getName());
        });

        assertTest("Long 99999999999", () -> {
            Number num = Exercise05Enhanced.parseNumber("Long", "99999999999");
            assertEquals(Long.valueOf(99999999999L), num);
            assertEquals("java.lang.Long", num.getClass().getName());
        });

        assertTest("Short 32000", () -> {
            Number num = Exercise05Enhanced.parseNumber("Short", "32000");
            assertEquals(Short.valueOf((short) 32000), num);
            assertEquals("java.lang.Short", num.getClass().getName());
        });

        assertTest("Byte 120", () -> {
            Number num = Exercise05Enhanced.parseNumber("Byte", "120");
            assertEquals(Byte.valueOf((byte) 120), num);
            assertEquals("java.lang.Byte", num.getClass().getName());
        });
    }

    private static void testExtendedTypes() {
        IO.println("\n[Nhóm 2: Các kiểu số mở rộng trong java.math]");

        assertTest("BigInteger 12345678901234567890", () -> {
            Number num = Exercise05Enhanced.parseNumber("BigInteger", "12345678901234567890");
            assertEquals(new BigInteger("12345678901234567890"), num);
            assertEquals("java.math.BigInteger", num.getClass().getName());
        });

        assertTest("BigDecimal 123456.789012", () -> {
            Number num = Exercise05Enhanced.parseNumber("BigDecimal", "123456.789012");
            assertEquals(new BigDecimal("123456.789012"), num);
            assertEquals("java.math.BigDecimal", num.getClass().getName());
        });
    }

    private static void testInvalidFormats() {
        IO.println("\n[Nhóm 3: Kiểm thử sai định dạng số]");

        assertTest("Integer với chuỗi chữ 'abc'", () -> {
            try {
                Exercise05Enhanced.parseNumber("Integer", "abc");
                throw new AssertionError("Mong đợi ngoại lệ NumberFormatException");
            } catch (NumberFormatException e) {
                // Đạt yêu cầu
            }
        });

        assertTest("Integer với số thực '5.5'", () -> {
            try {
                Exercise05Enhanced.parseNumber("Integer", "5.5");
                throw new AssertionError("Mong đợi ngoại lệ NumberFormatException");
            } catch (NumberFormatException e) {
                // Đạt yêu cầu
            }
        });
    }

    private static void testInvalidTypes() {
        IO.println("\n[Nhóm 4: Kiểm thử kiểu không hợp lệ / không tồn tại]");

        assertTest("Lớp không tồn tại 'NoSuchNumber'", () -> {
            try {
                Exercise05Enhanced.parseNumber("NoSuchNumber", "123");
                throw new AssertionError("Mong đợi ngoại lệ ClassNotFoundException");
            } catch (ClassNotFoundException e) {
                // Đạt yêu cầu
            }
        });

        assertTest("Lớp 'Boolean' không kế thừa từ Number", () -> {
            try {
                Exercise05Enhanced.parseNumber("Boolean", "true");
                throw new AssertionError("Mong đợi ngoại lệ IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Đạt yêu cầu
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
        throw new AssertionError(String.format("Mong đợi: %s, Thực tế: %s", expected, actual));
    }

    @FunctionalInterface
    private interface TestCase {
        void run() throws Throwable;
    }
}
