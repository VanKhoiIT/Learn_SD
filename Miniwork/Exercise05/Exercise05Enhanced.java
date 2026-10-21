package designbook.solutions5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Phiên bản nâng cao hỗ trợ thêm các kiểu số mở rộng trong java.math (BigInteger, BigDecimal)
 * và cung cấp hàm parseNumber phục vụ Unit Testing độc lập.
 */
public class Exercise05Enhanced {

    public static void main(String[] args) {
        if (args.length != 2) {
            error("Cách dùng: java -cp . designbook.solutions5.Exercise05Enhanced <TênKiểu> <GiáTrị>");
            return;
        }

        try {
            Number number = parseNumber(args[0], args[1]);
            IO.println(number + " là đối tượng kiểu " + number.getClass().getName());
        }
        catch (ClassNotFoundException e) {
            error("Lớp không tồn tại: " + args[0]);
        }
        catch (NoSuchMethodException e) {
            error("Lớp không hỗ trợ phương thức tạo đối tượng từ chuỗi: " + args[0]);
        }
        catch (NumberFormatException e) {
            error("Định dạng số không hợp lệ: '" + args[1] + "'");
        }
        catch (IllegalArgumentException e) {
            error("Kiểu dữ liệu không hợp lệ: " + e.getMessage());
        }
        catch (ReflectiveOperationException e) {
            error("Lỗi Reflection: " + e.getMessage());
        }
        catch (Exception e) {
            error("Lỗi không xác định: " + e.getMessage());
        }
    }

    public static Number parseNumber(String pTypeName, String pLiteral) throws Exception {
        Class<?> clazz = resolveClass(pTypeName);

        if (!Number.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("'" + clazz.getName() + "' không kế thừa từ java.lang.Number");
        }

        try {
            Method valueOfMethod = null;
            try {
                valueOfMethod = clazz.getMethod("valueOf", String.class);
            }
            catch (NoSuchMethodException ex) {
                // Hỗ trợ BigInteger / BigDecimal thông qua Constructor(String)
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
        try {
            return Class.forName("java.lang." + pTypeName);
        }
        catch (ClassNotFoundException e) {
            try {
                return Class.forName("java.math." + pTypeName);
            }
            catch (ClassNotFoundException e2) {
                return Class.forName(pTypeName);
            }
        }
    }

    private static void error(String pMessage) {
        IO.println("Lỗi: " + pMessage);
        System.exit(1);
    }
}
