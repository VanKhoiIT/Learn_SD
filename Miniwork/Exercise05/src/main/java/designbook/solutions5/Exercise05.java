package designbook.solutions5;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class Exercise05 {
    public static void main(String[] args) {
        // Bước 1: Kiểm tra số lượng tham số đầu vào
        if (args.length != 2) {
            error("Cách dùng: java Exercise05 <TênKiểu> <Giá trị>");
            return;
        }

        try {
            // Bước 2: Xác định tên lớp đầy đủ và nạp lớp động
            String fullClassName = "java.lang." + args[0];
            Class<?> numberClass = Class.forName(fullClassName);

            // Bước 3: Kiểm tra lớp có kế thừa từ java.lang.Number
            if (!Number.class.isAssignableFrom(numberClass)) {
                error(fullClassName + " không phải là lớp con của java.lang.Number");
                return;
            }

            // Bước 4: Tìm phương thức tĩnh valueOf(String)
            Method valueOfMethod = numberClass.getMethod("valueOf", String.class);

            // Bước 5: Gọi phương thức — đối số đầu là null vì là phương thức tĩnh
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
                error("Định dạng số không hợp lệ: " + args[1]);
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
