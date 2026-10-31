# BÁO CÁO & HƯỚNG DẪN THỰC HÀNH LAB 06 (EXERCISE 5.11)
## KIỂM THỬ ĐƠN VỊ VỚI TEST STUBS VÀ 100% BRANCH COVERAGE TRÊN LỚP BICARDSOURCE
## 1. TỔNG QUAN BÀI TOÁN (EXERCISE 5.11)
Đề bài trích nguyên văn từ giáo trình (`exercises/e-chapter5.md`):
> *"Write a test suite for all the public methods of class `BiCardSource` to achieve 100% branch coverage, using stubs to help isolate the behavior to test."*  
> *(Solution note: "This solution will reveal a seeded bug. Once you fix the bug, the suite should achieve complete branch coverage.")*

### Các mục tiêu cần đạt:
1. **Áp dụng Test Stubs:** Sử dụng lớp `CardSourceStub` với `Optional<Card>` để cô lập hành vi kiểm thử mà không cần phụ thuộc vào `Deck` hay hàm xáo bài (shuffle).
2. **100% Branch Coverage:** Bao phủ toàn bộ mọi nhánh rẽ logic của các phương thức công khai (`isEmpty()`, `draw()`, `isNull()`, constructor).
3. **Vạch trần Seeded Bug:** Chỉ rõ lỗi cài cắm sẵn trong mã của tác giả và sửa lỗi đúng đắn.
4. **Đồng bộ hóa với `code-e3` (Edition 3):** Sử dụng các gói mới nhất (`e3.chapter4.CardSource`, `e3.chapter4.Card` với kiểu accessor `card.rank()`, `card.suit()`, Flyweight `Card.get(...)`) thay vì kiểu cũ Edition 2 (`designbook.chapter4.*`).

---

## 2. CẤU TRÚC THƯ MỤC DỰ ÁN

```text
LAB/
├── lib/                                      # Thư viện JAR JUnit Jupiter 5.10.2 độc lập
│   ├── apiguardian-api-1.1.2.jar
│   ├── junit-jupiter-api-5.10.2.jar
│   ├── junit-jupiter-engine-5.10.2.jar
│   ├── junit-platform-commons-1.10.2.jar
│   ├── junit-platform-engine-1.10.2.jar
│   └── opentest4j-1.2.0.jar
│
├── src/
│   ├── main/java/
│   │   ├── e3/chapter4/                      # Domain Core từ code-e3
│   │   │   ├── CardSource.java               # Interface nguồn bài + Null Object
│   │   │   ├── Card.java                     # Lá bài Flyweight (card.rank(), card.suit())
│   │   │   ├── Rank.java                     # Enum thứ bậc (ACE..KING)
│   │   │   └── Suit.java                     # Enum chất (CLUBS, DIAMONDS, HEARTS, SPADES)
│   │   │
│   │   └── e3/chapter5/
│   │       ├── BiCardSource.java             # Lớp BiCardSource đã được SỬA LỖI hoàn chỉnh
│   │       └── BiCardSourceBuggy.java        # Bản gốc chứa Seeded Bug để kiểm chứng đối sánh
│   │
│   └── test/java/designbook/solutions5/
│       ├── Exercise11.java                   # Bộ kiểm thử chuẩn với Stubs (100% Branch Coverage)
│       ├── Exercise11BugDemonstrator.java    # Bộ kiểm chứng vạch trần lỗi cài cắm
│       ├── TestRunner.java                   # Bộ chạy kiểm thử console tự động (Java Reflection)
│       └── CoverageAnalyzerDemo.java         # Trình phân tích luồng điều khiển & ma trận nhánh
│
├── pom.xml                                   # Cấu hình chuẩn Maven (Java 21 LTS)
├── build_and_run.bat                         # Script biên dịch và chạy toàn diện (Windows CMD)
├── build_and_run.ps1                         # Script biên dịch và chạy toàn diện (PowerShell)
├── run.bat                                   # Script chạy riêng CoverageAnalyzerDemo
├── test.bat                                  # Script chạy riêng TestRunner
├── generate_docx.py                          # Script Python tạo báo cáo Word chuyên nghiệp
└── BAO_CAO_LAB_06_EXERCISE_11.docx           # Báo cáo Word hoàn chỉnh chuẩn học thuật
```

---

## 3. PHÂN TÍCH LỖI CÀI CẮM (SEEDED BUG) VÀ CÁCH SỬA

### 3.1. Đặc tả thiết kế (Specification):
> *"Draws from source 1 if not empty, and from source 2 if source 1 is empty."*  
*(Rút bài từ Nguồn 1 nếu Nguồn 1 chưa rỗng; chỉ rút từ Nguồn 2 khi Nguồn 1 đã rỗng).*

### 3.2. Lỗi cài cắm trong mã gốc (`BiCardSourceBuggy`):
```java
@Override
public Card draw() {
    if (!aSource1.isEmpty()) {
        return aSource2.draw(); // SAI LẦM: Nguồn 1 còn bài lại rút từ Nguồn 2!
    }
    else {
        return aSource1.draw(); // SAI LẦM: Nguồn 1 đã rỗng lại cố rút từ Nguồn 1!
    }
}
```

### 3.3. Hậu quả khi chạy bộ kiểm thử trên mã có lỗi:
1. `testDraw_FirstEmpty`: Nguồn 1 rỗng, Nguồn 2 có ACE. Do `!aSource1.isEmpty()` là false nên nhảy vào nhánh `else` gọi `aSource1.draw()`. Do nguồn 1 rỗng, văng ngoại lệ `NoSuchElementException`! => **FAILED**.
2. `testDraw_SecondEmpty`: Nguồn 1 có ACE, Nguồn 2 rỗng. Điều kiện `!aSource1.isEmpty()` là true nên nhảy vào nhánh `if` gọi `aSource2.draw()`. Do nguồn 2 rỗng, văng ngoại lệ! => **FAILED**.
3. `testDraw_NeitherEmpty`: Nguồn 1 có ACE, Nguồn 2 có TWO. Nhảy vào nhánh `if` rút từ `aSource2` và trả về `TWO`. Phép so sánh `assertSame(ACE, drawn)` bị sai lệch! => **FAILED**.

### 3.4. Bản sửa lỗi chuẩn xác (`BiCardSource`):
```java
@Override
public Card draw() {
    assert !isEmpty() : "Cannot draw from an empty BiCardSource";
    if (!aSource1.isEmpty()) {
        return aSource1.draw(); // Ưu tiên rút từ Nguồn 1
    }
    else {
        return aSource2.draw(); // Nguồn 1 rỗng thì rút từ Nguồn 2
    }
}
```

---

## 4. MA TRẬN ĐỘ BAO PHỦ NHÁNH (100% BRANCH COVERAGE)

### 4.1. Phương thức `isEmpty()`:
- Biểu thức logic: `return aSource1.isEmpty() && aSource2.isEmpty();`
- Toán tử `&&` tạo ra 3 nhánh rẽ do ngắt ngắn mạch (short-circuit):
  1. **Nhánh True && True -> True:** Kích hoạt bởi `testEmpty_True()` (Cả 2 nguồn đều rỗng).
  2. **Nhánh True && False -> False:** Kích hoạt bởi `testEmpty_False_FirstEmpty()` (Nguồn 1 rỗng, Nguồn 2 có bài).
  3. **Nhánh False -> False (Ngắt ngắn mạch):** Kích hoạt bởi `testEmpty_False_SecondEmpty()` (Nguồn 1 có bài, Nguồn 2 rỗng) và `testEmpty_False_NeitherEmpty()` (Cả 2 nguồn đều có bài). Không cần duyệt Nguồn 2.

### 4.2. Phương thức `draw()`:
- Cấu trúc điều khiển: `if (!aSource1.isEmpty())`
  1. **Nhánh True:** Kích hoạt bởi `testDraw_SecondEmpty()` và `testDraw_NeitherEmpty()`. Thực thi `return aSource1.draw()`.
  2. **Nhánh False:** Kích hoạt bởi `testDraw_FirstEmpty()`. Thực thi `return aSource2.draw()`.

### 4.3. Bảng tổng hợp kiểm thử:
| Test Method | aSource1 | aSource2 | Nhánh thực thi | Kết quả mong đợi |
| :--- | :--- | :--- | :--- | :--- |
| `testEmpty_True` | Rỗng | Rỗng | `isEmpty() T && T` | `true` |
| `testEmpty_False_FirstEmpty` | Rỗng | Có bài (ACE) | `isEmpty() T && F` | `false` |
| `testEmpty_False_SecondEmpty` | Có bài (ACE) | Rỗng | `isEmpty() F (short-circuit)` | `false` |
| `testEmpty_False_NeitherEmpty` | Có bài (ACE) | Có bài (TWO) | `isEmpty() F (short-circuit)` | `false` |
| `testDraw_FirstEmpty` | Rỗng | Có bài (ACE) | `draw() Else branch` | Trả về ACE (từ Nguồn 2) |
| `testDraw_SecondEmpty` | Có bài (ACE) | Rỗng | `draw() If branch` | Trả về ACE (từ Nguồn 1) |
| `testDraw_NeitherEmpty` | Có bài (ACE) | Có bài (TWO) | `draw() If branch` | Trả về ACE (từ Nguồn 1) |

---

## 5. HƯỚNG DẪN CHẠY VÀ KIỂM TRA

### Cách 1: Sử dụng Script tự động (Khuyến nghị)
- **Windows Command Prompt:**
  ```cmd
  build_and_run.bat
  ```
- **PowerShell:**
  ```powershell
  .\build_and_run.ps1
  ```

### Cách 2: Sử dụng Maven
```cmd
mvn clean test
```

### Cách 3: Lệnh javac / java tiêu chuẩn
```cmd
javac --release 21 -encoding UTF-8 -cp "lib/*" -d bin src/main/java/e3/*/*.java src/test/java/designbook/solutions5/*.java
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.TestRunner
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.CoverageAnalyzerDemo
```

---
**Tác giả thực hiện:** Antigravity AI Assistant  
**Đồng bộ dự án:** Hoàn chỉnh 100% tại cả `Post_Class_06/LAB` và `Post_Class_06`
