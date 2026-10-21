# Miniwork - Exercise 05: Metaprogramming & Java Reflection

> **Môn học:** Thiết kế Phần mềm (Software Design)  
> **Tài liệu tham khảo:** *Introduction to Software Design with Java* (GS. Martin P. Robillard, Springer) — Chapter 5 Practice Exercises.

---

## 👥 Danh sách sinh viên thực hiện

| STT | Mã số sinh viên (MSSV) | Họ và tên sinh viên |
| :---: | :---: | :--- |
| 1 | **4651050107** | Nguyễn Tấn Hùng |
| 2 | **4651050048** | Huỳnh Hữu Đan |
| 3 | **4651050122** | Trương Văn Khôi |
| 4 | **4651050026** | Đào Duy Chính |
| 5 | **4651050131** | Huỳnh Gia Kiệt |
| 6 | **4651050118** | Nguyễn Nhật Khánh |
| 7 | **4651050201** | Huỳnh Gia Phong |

---

## 📁 1. Cấu trúc thư mục chuẩn dự án

Dự án được tổ chức theo chuẩn cấu trúc **Maven Standard Directory Layout**:

```
Miniwork/Exercise05/
├── pom.xml                                  # Cấu hình Maven chuẩn (JDK 21/25, JUnit 5)
├── run.bat                                  # Script menu tự động biên dịch & chạy trên Windows
├── .gitignore                               # Bỏ qua bin/, target/, *.class, *.docx, file IDE
├── README.md                                # Hướng dẫn chi tiết dự án
└── src/
    ├── main/java/designbook/solutions5/     # Mã nguồn chính
    │   ├── Exercise05.java                  # Lời giải chính sử dụng Reflection & OCP
    │   ├── Exercise05Enhanced.java          # Bản nâng cao hỗ trợ java.math & parseNumber()
    │   └── IO.java                          # Tiện ích xuất nhập tương thích mọi JDK
    └── test/java/designbook/solutions5/     # Mã nguồn kiểm thử
        ├── Exercise05Test.java              # Bộ kiểm thử chuẩn JUnit 5 (mvn test)
        └── TestRunner.java                  # Bộ kiểm thử độc lập 12 test cases (CLI không cần Maven)
```

---

## 📌 2. Đề bài (Exercise 05 - Chapter 5)

> *"Using metaprogramming, write a program that takes two command-line arguments: the short name of a subtype of Number and a number literal, and that creates a new instance of that type that represents the number. For example, Integer 5 to represent 5 as an integer, or Double 5.1 to represent 5.1 as a double."*

### Yêu cầu cốt lõi:
- Nhận 2 đối số từ dòng lệnh (command-line arguments):
  1. `args[0]`: Tên ngắn của một lớp con kế thừa từ `java.lang.Number` (ví dụ: `Integer`, `Double`, `Float`, `Long`, `Byte`, `Short`).
  2. `args[1]`: Giá trị số biểu diễn dưới dạng chuỗi ký tự (number literal, ví dụ: `"5"`, `"5.1"`).
- Vận dụng kỹ thuật **Metaprogramming / Java Reflection API** để nạp lớp và khởi tạo đối tượng động lúc runtime.
- **Không dùng câu lệnh rẽ nhánh điều kiện** (`if-else` hoặc `switch-case`) để phân loại từng kiểu dữ liệu, tuân thủ nghiêm ngặt **Nguyên lý Đóng - Mở (Open-Closed Principle — OCP)**.

---

## 🚀 3. Hướng dẫn biên dịch và chạy chương trình

### Cách 1: Sử dụng Menu tương tác nhanh trên Windows (Khuyến nghị)
Nhấp đúp chuột vào file `run.bat` hoặc mở Terminal tại thư mục này và gõ:
```cmd
run.bat
```
Menu tương tác sẽ tự động biên dịch toàn bộ mã nguồn vào thư mục `bin/` và cho phép chọn nhanh các ca kiểm thử:
```
1. Chạy ca kiểm thử mẫu: Integer 5
2. Chạy ca kiểm thử mẫu: Double 5.1
3. Chạy ca kiểm thử mẫu: Float 3.14
4. Chạy ca kiểm thử mẫu: Long 1000
5. Chạy ca kiểm thử mẫu: Byte 127
6. Chạy ca kiểm thử mẫu: Short 32000
7. Chạy ca kiểm thử lỗi: Integer abc (Sai định dạng số)
8. Chạy ca kiểm thử lỗi: String 123 (Không thuộc Number)
9. Chạy bộ kiểm thử tự động (TestRunner - 12 Unit Tests)
10. Nhập tham số tùy chọn (Kiểu và Giá trị)
```

Bạn cũng có thể truyền trực tiếp tham số vào `run.bat`:
```cmd
run.bat Integer 5
run.bat Double 5.1
run.bat --test
```

---

### Cách 2: Sử dụng dòng lệnh Terminal (`javac` & `java`)

#### Bước 1: Biên dịch mã nguồn
```bash
# Tạo thư mục bin nếu chưa có
mkdir bin

# Biên dịch toàn bộ file java trong src/main và src/test
javac -encoding UTF-8 -d bin src/main/java/designbook/solutions5/*.java src/test/java/designbook/solutions5/*.java
```

#### Bước 2: Chạy chương trình chính (`Exercise05`)
Cú pháp:
```bash
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 <TênKiểu> <GiáTrị>
```

Ví dụ thực tế:
```bash
# 1. Khởi tạo Integer
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 Integer 5
# Kết quả: 5 là đối tượng kiểu java.lang.Integer

# 2. Khởi tạo Double
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 Double 5.1
# Kết quả: 5.1 là đối tượng kiểu java.lang.Double

# 3. Khởi tạo Float
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 Float 3.14
# Kết quả: 3.14 là đối tượng kiểu java.lang.Float

# 4. Khởi tạo Long
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 Long 1000
# Kết quả: 1000 là đối tượng kiểu java.lang.Long

# 5. Khởi tạo Byte
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 Byte 127
# Kết quả: 127 là đối tượng kiểu java.lang.Byte

# 6. Khởi tạo Short
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 Short 32000
# Kết quả: 32000 là đối tượng kiểu java.lang.Short
```

#### Bước 3: Chạy các trường hợp kiểm tra lỗi ngoại lệ
```bash
# Sai định dạng số (NumberFormatException)
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 Integer abc
# Kết quả: Lỗi: Định dạng số không hợp lệ: 'abc'

# Lớp không thuộc Number (String không kế thừa Number)
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 String 123
# Kết quả: Lỗi: java.lang.String không phải là lớp con của java.lang.Number

# Lớp không tồn tại (ClassNotFoundException)
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.Exercise05 KhongTonTai 123
# Kết quả: Lỗi: Lớp không tồn tại: java.lang.KhongTonTai
```

#### Bước 4: Chạy bộ kiểm thử tự động (12 Unit Tests)
```bash
java -Dfile.encoding=UTF-8 -cp bin designbook.solutions5.TestRunner
```

---

### Cách 3: Sử dụng Apache Maven
Nếu máy đã cài đặt Maven:
```bash
# Biên dịch dự án
mvn compile

# Chạy toàn bộ Unit Tests bằng JUnit 5
mvn test

# Chạy chương trình chính với tham số mặc định
mvn exec:java
```

---

## 🔍 4. Kiến trúc kỹ thuật & Cơ sở lý thuyết

### 1. Luồng thực thi Metaprogramming
```
[Dòng lệnh: "Integer" "5"]
         │
         ▼
[1. Ghép chuỗi Fully-Qualified Name: "java.lang.Integer"]
         │
         ▼
[2. Nạp lớp động: Class.forName("java.lang.Integer")]
         │
         ▼
[3. Kiểm tra kế thừa an toàn: Number.class.isAssignableFrom(clazz)]
         │
         ▼
[4. Định vị phương thức tĩnh: clazz.getMethod("valueOf", String.class)]
         │
         ▼
[5. Kích hoạt thực thi: valueOfMethod.invoke(null, "5")]
         │
         ▼
[6. Trả về đối tượng kiểu java.lang.Integer mang giá trị 5]
```

### 2. Các điểm kỹ thuật nổi bật
- **Tại sao dùng `valueOf(String)` thay vì Constructor?**
  - Kể từ Java 9, các constructor kiểu số (như `new Integer("5")`) đã bị đánh dấu `@Deprecated(forRemoval=true)`.
  - `valueOf(String)` hỗ trợ cơ chế **Bộ đệm (IntegerCache từ -128 đến 127)**, giúp tái sử dụng vùng nhớ, tối ưu hiệu năng bộ nhớ Heap.
  - Phải truyền đối số `String.class` vào `getMethod("valueOf", String.class)` để phân biệt với các hàm nạp chồng khác (`valueOf(int)`, `valueOf(double)`...).
- **Phân biệt `getMethod` vs `getDeclaredMethod`**:
  - `getMethod`: Tìm kiếm các phương thức `public` (bao gồm cả phương thức kế thừa từ lớp cha).
  - `getDeclaredMethod`: Tìm kiếm mọi phương thức khai báo trực tiếp trong lớp (kể cả `private`, `protected`).
- **Xử lý bóc tách ngoại lệ (`InvocationTargetException`)**:
  - Khi gọi hàm qua `Method.invoke()`, bất kỳ ngoại lệ nào phát sinh từ phương thức đích (như `NumberFormatException`) đều bị JVM bọc bên trong `InvocationTargetException`.
  - Chương trình sử dụng `e.getCause()` để kiểm tra `instanceof NumberFormatException`, thông báo chính xác nguyên nhân lỗi tới người dùng thay vì báo lỗi Reflection mơ hồ.
