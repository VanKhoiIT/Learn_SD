package designbook.solutions8.common;

/**
 * Utility class providing IO helper methods for standardized console output.
 */
public final class IO {

	private IO() {}

	public static void println(Object pObject) {
		System.out.println(pObject);
	}

	public static void println(String pMessage) {
		System.out.println(pMessage);
	}

	public static void println() {
		System.out.println();
	}

	public static void print(Object pObject) {
		System.out.print(pObject);
	}

	public static void print(String pMessage) {
		System.out.print(pMessage);
	}
}

