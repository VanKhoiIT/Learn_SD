package designbook.solutions5;

/**
 * Utility class providing IO helper methods for console output.
 * In Java 25+, java.lang.IO provides static println methods.
 * This class ensures compatibility across all Java versions (Java 8 to 25+).
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
