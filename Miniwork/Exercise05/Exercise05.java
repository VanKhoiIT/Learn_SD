package designbook.solutions5;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * Exercise 05 - Chapter 5: Metaprogramming & Reflection
 * Giáo trình: Introduction to Software Design with Java (Martin P. Robillard)
 * 
 * Chương trình nhận 2 đối số dòng lệnh:
 * - args[0]: Tên ngắn của lớp con thuộc java.lang.Number (ví dụ: Integer, Double, Byte, Short, Long, Float)
 * - args[1]: Giá trị số biểu diễn dưới dạng chuỗi (ví dụ: "5", "5.1")
 * 
 * Sử dụng Java Reflection để nạp lớp và khởi tạo đối tượng động mà không dùng rẽ nhánh if-else/switch-case.
 */
public class Exercise05 {

    public static void main(String[] args) {
        // Bước 1: Kiểm tra số lượng tham số đầu vào
        if (args.length != 2) {
            error("Cách dùng: java -cp . designbook.solutions5.Exercise05 <TênKiểu> <GiáTrị>\nVí dụ: java -cp . designbook.solutions5.Exercise05 Integer 5");
            return;
        }

        try {
            // Bước 2: Xác định tên lớp đầy đủ (Fully Qualified Name) và nạp lớp động
            String fullClassName = "java.lang." + args[0];
            Class<?> numberClass = Class.forName(fullClassName);

            // Bước 3: Kiểm tra tính an toàn kiểu (Type Safety) - xác minh lớp con của Number
            if (!Number.class.isAssignableFrom(numberClass)) {
                error(fullClassName + " không phải là lớp con của java.lang.Number");
                return;
            }

            // Bước 4: Tìm phương thức tĩnh valueOf(String) bằng Reflection
            Method valueOfMethod = numberClass.getMethod("valueOf", String.class);

            // Bước 5: Gọi thực thi phương thức tĩnh (đối số đối tượng là null vì là phương thức static)
            Number number = (Number) valueOfMethod.invoke(null, args[1]);

            // Bước 6: Hiển thị kết quả đối tượng và kiểu thực tế
            IO.println(number + " là đối tượng kiểu " + number.getClass().getName());

        } catch (ClassNotFoundException e) {
            error("Lớp không tồn tại: java.lang." + args[0]);
        } catch (NoSuchMethodException e) {
            error("Lớp không hỗ trợ phương thức valueOf(String)");
        } catch (InvocationTargetException e) {
            // Unwrap ngoại lệ nguyên nhân bên trong phương thức đích
            Throwable cause = e.getCause();
            if (cause instanceof NumberFormatException) {
                error("Định dạng số không hợp lệ: '" + args[1] + "'");
            } else {
                error("Lỗi khi tạo đối tượng: " + (cause != null ? cause.getMessage() : e.getMessage()));
            }
        } catch (ReflectiveOperationException e) {
            error("Lỗi Reflection: " + e.getMessage());
        }
    }

    private static void error(String message) {
        IO.println("Lỗi: " + message);
        System.exit(1);
    }
}
