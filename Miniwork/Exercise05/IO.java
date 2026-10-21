package designbook.solutions5;

import java.io.PrintStream;

/**
 * Lớp tiện ích IO hỗ trợ tương thích từ Java 8 đến Java 25.
 */
public class IO {

    private static final PrintStream OUT = System.out;

    public static void println(Object pObject) {
        OUT.println(pObject);
    }

    public static void println(String pString) {
        OUT.println(pString);
    }

    public static void print(String pString) {
        OUT.print(pString);
    }
}
