package designbook.solutions5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Enhanced version of Exercise05 demonstrating advanced metaprogramming and robust error handling.
 * 
 * Key improvements over the basic version:
 * 1. Validates that the loaded class is actually a subtype of Number (Number.class.isAssignableFrom).
 * 2. Properly handles InvocationTargetException: when Method.invoke() is called, any
 *    NumberFormatException thrown by valueOf() is wrapped inside InvocationTargetException.
 *    By inspecting getCause(), we accurately detect NumberFormatException.
 * 3. Supports types in both java.lang (Integer, Double, Float, Long, Short, Byte)
 *    and java.math (BigInteger, BigDecimal).
 * 4. Provides a reusable method parseNumber(...) for unit testing without System.exit().
 */
public class Exercise05Enhanced {

	public static void main(String[] args) {
		if (args.length != 2) {
			error("Invalid number of arguments. Usage: java Exercise05Enhanced <Type> <Literal>");
		}

		try {
			Number number = parseNumber(args[0], args[1]);
			IO.println(number + " is a number of type " + number.getClass().getName());
		}
		catch (ClassNotFoundException e) {
			error("Invalid number type: class not found for '" + args[0] + "'");
		}
		catch (NoSuchMethodException e) {
			error("Invalid number type: no valueOf(String) method in '" + args[0] + "'");
		}
		catch (NumberFormatException e) {
			error("Invalid number format: '" + args[1] + "' is not a valid representation");
		}
		catch (IllegalArgumentException e) {
			error("Invalid number type: " + e.getMessage());
		}
		catch (ReflectiveOperationException e) {
			error("Reflection error: " + e.getMessage());
		}
		catch (Exception e) {
			error("Unexpected error: " + e.getMessage());
		}
	}

	/**
	 * Parses a string literal into a subtype of Number using reflection.
	 * 
	 * @param pTypeName Short name (e.g. "Integer", "Double") or fully qualified name
	 * @param pLiteral  The string representation of the number (e.g. "5", "3.14")
	 * @return An instance of the requested Number subtype
	 */
	public static Number parseNumber(String pTypeName, String pLiteral) throws Exception {
		Class<?> clazz = resolveClass(pTypeName);

		// Verify that the requested class actually extends java.lang.Number
		if (!Number.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException("'" + clazz.getName() + "' is not a subtype of java.lang.Number");
		}

		try {
			// For BigInteger/BigDecimal, valueOf(long/double) exists, or constructor/valueOf(String)
			Method valueOfMethod = null;
			try {
				valueOfMethod = clazz.getDeclaredMethod("valueOf", String.class);
			}
			catch (NoSuchMethodException ex) {
				// Fallback to constructor(String) if valueOf(String) doesn't exist (e.g., BigInteger/BigDecimal)
				return (Number) clazz.getConstructor(String.class).newInstance(pLiteral);
			}

			return (Number) valueOfMethod.invoke(null, pLiteral);
		}
		catch (InvocationTargetException ex) {
			Throwable cause = ex.getCause();
			if (cause instanceof NumberFormatException) {
				throw (NumberFormatException) cause;
			}
			if (cause instanceof Exception) {
				throw (Exception) cause;
			}
			throw ex;
		}
	}

	private static Class<?> resolveClass(String pTypeName) throws ClassNotFoundException {
		// First try java.lang.<Type>
		try {
			return Class.forName("java.lang." + pTypeName);
		}
		catch (ClassNotFoundException e) {
			// Then try java.math.<Type> (BigInteger, BigDecimal)
			try {
				return Class.forName("java.math." + pTypeName);
			}
			catch (ClassNotFoundException e2) {
				// Finally try as fully-qualified name
				return Class.forName(pTypeName);
			}
		}
	}

	private static void error(String pMessage) {
		IO.println("Error: " + pMessage);
		System.exit(1);
	}
}
