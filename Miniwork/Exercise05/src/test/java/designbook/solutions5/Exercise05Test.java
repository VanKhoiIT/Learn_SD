package designbook.solutions5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 test suite for Exercise05 and Exercise05Enhanced.
 * Can be run via Maven (mvn test) or IDE (Eclipse, IntelliJ, VS Code).
 */
public class Exercise05Test {

	@Test
	@DisplayName("Test Integer parsing with valid integer literal")
	void testInteger() throws Exception {
		Number result = Exercise05Enhanced.parseNumber("Integer", "5");
		assertNotNull(result);
		assertEquals(Integer.valueOf(5), result);
		assertEquals("java.lang.Integer", result.getClass().getName());
	}

	@Test
	@DisplayName("Test Double parsing with valid double literal")
	void testDouble() throws Exception {
		Number result = Exercise05Enhanced.parseNumber("Double", "5.1");
		assertNotNull(result);
		assertEquals(Double.valueOf(5.1), result);
		assertEquals("java.lang.Double", result.getClass().getName());
	}

	@Test
	@DisplayName("Test Long parsing with large literal")
	void testLong() throws Exception {
		Number result = Exercise05Enhanced.parseNumber("Long", "99999999999");
		assertNotNull(result);
		assertEquals(Long.valueOf(99999999999L), result);
		assertEquals("java.lang.Long", result.getClass().getName());
	}

	@Test
	@DisplayName("Test Float parsing")
	void testFloat() throws Exception {
		Number result = Exercise05Enhanced.parseNumber("Float", "3.14");
		assertNotNull(result);
		assertEquals(Float.valueOf(3.14f), result);
		assertEquals("java.lang.Float", result.getClass().getName());
	}

	@Test
	@DisplayName("Test Short parsing")
	void testShort() throws Exception {
		Number result = Exercise05Enhanced.parseNumber("Short", "32000");
		assertNotNull(result);
		assertEquals(Short.valueOf((short) 32000), result);
		assertEquals("java.lang.Short", result.getClass().getName());
	}

	@Test
	@DisplayName("Test Byte parsing")
	void testByte() throws Exception {
		Number result = Exercise05Enhanced.parseNumber("Byte", "127");
		assertNotNull(result);
		assertEquals(Byte.valueOf((byte) 127), result);
		assertEquals("java.lang.Byte", result.getClass().getName());
	}

	@Test
	@DisplayName("Test BigInteger parsing")
	void testBigInteger() throws Exception {
		Number result = Exercise05Enhanced.parseNumber("BigInteger", "12345678901234567890");
		assertNotNull(result);
		assertEquals(new BigInteger("12345678901234567890"), result);
		assertEquals("java.math.BigInteger", result.getClass().getName());
	}

	@Test
	@DisplayName("Test BigDecimal parsing")
	void testBigDecimal() throws Exception {
		Number result = Exercise05Enhanced.parseNumber("BigDecimal", "123456.789012");
		assertNotNull(result);
		assertEquals(new BigDecimal("123456.789012"), result);
		assertEquals("java.math.BigDecimal", result.getClass().getName());
	}

	@Test
	@DisplayName("Test invalid number format throws NumberFormatException")
	void testInvalidNumberFormat() {
		assertThrows(NumberFormatException.class, () -> {
			Exercise05Enhanced.parseNumber("Integer", "abc");
		});

		assertThrows(NumberFormatException.class, () -> {
			Exercise05Enhanced.parseNumber("Integer", "5.5");
		});
	}

	@Test
	@DisplayName("Test non-existent class throws ClassNotFoundException")
	void testNonExistentClass() {
		assertThrows(ClassNotFoundException.class, () -> {
			Exercise05Enhanced.parseNumber("UnknownNumberType", "123");
		});
	}

	@Test
	@DisplayName("Test non-Number class throws IllegalArgumentException")
	void testNonNumberClass() {
		assertThrows(IllegalArgumentException.class, () -> {
			Exercise05Enhanced.parseNumber("Boolean", "true");
		});
	}
}
