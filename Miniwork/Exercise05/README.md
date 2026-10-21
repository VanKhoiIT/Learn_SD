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

## 📌 1. Đề bài (Exercise 05 - Chapter 5)

> *"Using metaprogramming, write a program that takes two command-line arguments: the short name of a subtype of Number and a number literal, and that creates a new instance of that type that represents the number. For example, Integer 5 to represent 5 as an integer, or Double 5.1 to represent 5.1 as a double."*

### Yêu cầu cốt lõi:
- Nhận 2 đối số từ dòng lệnh (command-line arguments):
  1. `args[0]`: Tên ngắn của một lớp con kế thừa từ `java.lang.Number` (ví dụ: `Integer`, `Double`, `Float`, `Long`, `Byte`, `Short`).
  2. `args[1]`: Giá trị số biểu diễn dưới dạng chuỗi ký tự (number literal, ví dụ: `"5"`, `"5.1"`).
- Vận dụng kỹ thuật **Metaprogramming / Java Reflection API** để nạp lớp và khởi tạo đối tượng động lúc runtime.
- **Không dùng câu lệnh rẽ nhánh điều kiện** (`if-else` hoặc `switch-case`) để phân loại từng kiểu dữ liệu, tuân thủ nghiêm ngặt **Nguyên lý Đóng - Mở (Open-Closed Principle — OCP)**.

---

## 🚀 2. Hướng dẫn biên dịch và chạy chương trình

### Cách 1: Sử dụng Menu tự động (Khuyến nghị trên Windows)
Nhấp đúp chuột vào file `run.bat` hoặc mở Terminal tại thư mục này và gõ:
```cmd
run.bat
```
Menu tương tác sẽ tự động biên dịch mã nguồn và cung cấp các lựa chọn kiểm thử nhanh các trường hợp (Integer, Double, Byte, Short, Long, lỗi ngoại lệ, hoặc chạy 12 Unit Tests tự động).

Bạn cũng có thể truyền trực tiếp tham số vào `run.bat`:
```cmd
run.bat Integer 5
run.bat Double 5.1
run.bat --test
```

---

### Cách 2: Sử dụng dòng lệnh Terminal chuẩn (`javac` & `java`)

#### Bước 1: Biên dịch mã nguồn
```bash
javac -encoding UTF-8 -d . *.java
```
*Lệnh trên sẽ biên dịch toàn bộ các file `.java` và sinh ra cấu trúc package `designbook/solutions5/` ngay tại thư mục hiện hành.*

#### Bước 2: Chạy chương trình chính (`Exercise05`)
Cú pháp:
```bash
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 <TênKiểu> <GiáTrị>
```

Ví dụ thực tế:
```bash
# 1. Khởi tạo số nguyên Integer
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Integer 5
# Kết quả: 5 là đối tượng kiểu java.lang.Integer

# 2. Khởi tạo số thực Double
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Double 5.1
# Kết quả: 5.1 là đối tượng kiểu java.lang.Double

# 3. Khởi tạo số thực Float
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Float 3.14
# Kết quả: 3.14 là đối tượng kiểu java.lang.Float

# 4. Khởi tạo số nguyên lớn Long
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Long 1000
# Kết quả: 1000 là đối tượng kiểu java.lang.Long

# 5. Khởi tạo số nguyên Byte
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Byte 127
# Kết quả: 127 là đối tượng kiểu java.lang.Byte

# 6. Khởi tạo số nguyên Short
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Short 32000
# Kết quả: 32000 là đối tượng kiểu java.lang.Short
```

#### Bước 3: Chạy các trường hợp kiểm tra lỗi ngoại lệ
```bash
# Kiểm thử sai định dạng số
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 Integer abc
# Kết quả: Lỗi: Định dạng số không hợp lệ: 'abc'

# Kiểm thử lớp không kế thừa từ java.lang.Number
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 String 123
# Kết quả: Lỗi: java.lang.String không phải là lớp con của java.lang.Number

# Kiểm thử lớp không tồn tại
java -Dfile.encoding=UTF-8 designbook.solutions5.Exercise05 KhongTonTai 123
# Kết quả: Lỗi: Lớp không tồn tại: java.lang.KhongTonTai
```

#### Bước 4: Chạy bộ kiểm thử tự động (Unit Tests)
```bash
java -Dfile.encoding=UTF-8 designbook.solutions5.TestRunner
```

---

## 🔍 3. Kiến trúc kỹ thuật & Cơ sở lý thuyết

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

---

## 📁 4. Cấu trúc thư mục

```
Miniwork/Exercise05/
├── Exercise05.java          # Mã nguồn chính giải quyết bài toán theo chuẩn OCP
├── Exercise05Enhanced.java  # Bản nâng cao (hỗ trợ java.math, phục vụ Unit Test)
├── IO.java                  # Lớp tiện ích xuất nhập tương thích mọi JDK
├── TestRunner.java          # Bộ kiểm thử tự động 12 test cases
├── run.bat                  # Script biên dịch & menu chạy nhanh trên Windows
├── .gitignore               # Loại bỏ file rác .class, bin/, .docx
└── README.md                # Tài liệu hướng dẫn chi tiết
```
