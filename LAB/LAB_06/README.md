# LAB 06: Unit Testing with Test Stubs & 100% Branch Coverage
> **Bài tập:** Exercise 5.11 - Kiểm thử đơn vị lớp `BiCardSource` (Giáo trình *Introduction to Software Design with Java*, 3rd Edition)  
> **Môi trường:** Java 21 LTS / Java 25 | JUnit Jupiter 5.10.2 | Cấu trúc gói `code-e3`

---

## 🚀 Hướng dẫn Chạy Kiểm thử (Quick Start)

Dự án tích hợp sẵn toàn bộ thư viện JUnit 5 trong thư mục `lib/`, có thể biên dịch và chạy ngay lập tức mà không cần cài đặt thêm phần mềm phụ trợ:

### Cách 1: Chạy bộ kiểm thử tự động (Khuyến nghị)
Nhấp đúp chuột hoặc chạy từ cửa sổ dòng lệnh:
```cmd
test.bat
```
*Tự động biên dịch và thực thi 13 ca kiểm thử: 9 ca kiểm thử `Exercise11` đạt 100% Branch Coverage và 4 ca đối chứng `Exercise11BugDemonstrator` xác nhận lỗi cài cắm (Seeded Bug).*

### Cách 2: Chạy phân tích ma trận độ bao phủ nhánh
```cmd
run.bat
```
*In chi tiết đồ thị luồng điều khiển (CFG), phân tích toán tử ngắn mạch `&&` và ma trận bao phủ nhánh.*

### Cách 3: Sử dụng Maven tiêu chuẩn
```cmd
mvn test
```

### Cách 4: Dòng lệnh thủ công javac / java
```bash
# Biên dịch mã nguồn
javac --release 21 -encoding UTF-8 -cp "lib/*" -d bin src/main/java/e3/*/*.java src/test/java/designbook/solutions5/*.java

# Chạy TestRunner
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.TestRunner
```

---

## 📝 Danh sách các File Sửa đổi & Thêm mới trong Bài tập Exercise 11

Để hoàn thành trọn vẹn tất cả yêu cầu của **Exercise 5.11** (đạt 100% Branch Coverage, áp dụng kỹ thuật Test Stubbing, phát hiện và sửa Seeded Bug, đồng bộ kiến trúc `code-e3`), dự án đã thực hiện các thay đổi cụ thể sau:

### 1. File SỬA ĐỔI (Modified File)

* **`src/main/java/e3/chapter5/BiCardSource.java`**
  * **Vai trò:** Lớp đối tượng kiểm thử chính của bài tập (Unit Under Test - UUT).
  * **Các kỹ thuật & thay đổi mã nguồn đã áp dụng:**
    1. **Khắc phục lỗi cài cắm (Fix Seeded Bug) trong phương thức `draw()`:**
       * *Mã gốc đề bài:* Bị đảo ngược logic rút bài (`return aSource2.draw()` khi `!aSource1.isEmpty()` và `return aSource1.draw()` khi `else`).
       * *Mã sửa đổi chuẩn:* Hoán đổi lại đúng theo đặc tả: Ưu tiên rút từ `aSource1` khi `!aSource1.isEmpty()`, và chỉ rút từ `aSource2` khi `aSource1` đã rỗng.
    2. **Áp dụng kỹ thuật Design by Contract (Precondition Assertion):**
       * Thêm câu lệnh phòng thủ: `assert !isEmpty() : "Cannot draw from an empty BiCardSource";` để đảm bảo an toàn, không cho phép rút khi cả hai nguồn đều rỗng.
    3. **Đồng bộ hóa kiến trúc `code-e3` (Edition 3 - 2025):**
       * Chuyển package sang `e3.chapter5`, import interface `e3.chapter4.CardSource` và lớp Flyweight `e3.chapter4.Card`.

---

### 2. Các File THÊM MỚI (Added Files)

#### A. Mã nguồn kiểm thử (Test Code)
* **`src/test/java/designbook/solutions5/Exercise11.java`**
  * **Vai trò:** Bộ kiểm thử đơn vị chính thức đạt **100% Branch Coverage**.
  * **Kỹ thuật áp dụng:**
    * **Áp dụng kỹ thuật Test Stubbing (`CardSourceStub`):** Hiện thực lớp tĩnh nội bộ `CardSourceStub` dùng `Optional<Card>` để giả lập chính xác trạng thái rỗng hoặc có bài mà không cần phụ thuộc vào lớp `Deck`.
    * **Áp dụng kỹ thuật xUnit Test Isolation:** Cung cấp cơ chế cô lập qua `@BeforeEach` tái tạo mới các stub trước mỗi ca kiểm thử nhằm tránh ô nhiễm trạng thái chia sẻ (State Pollution).
    * **Bao phủ toàn bộ nhánh rẽ:** Xây dựng 9 ca kiểm thử: 4 ca cho bảng chân trị `isEmpty()` (bao gồm ngắn mạch), 3 ca cho nhánh rẽ `draw()`, 1 ca cho `isNull()`, và 1 ca kiểm tra rút bài chuyển tiếp tuần tự từ nguồn 1 sang nguồn 2.
* **`src/main/java/e3/chapter5/BiCardSourceBuggy.java`**
  * **Vai trò:** Lưu trữ nguyên bản mã nguồn gốc chứa Seeded Bug từ sách để làm đối chứng.
* **`src/test/java/designbook/solutions5/Exercise11BugDemonstrator.java`**
  * **Vai trò:** Bộ kiểm thử tự động chứng minh sự tồn tại của Seeded Bug trên `BiCardSourceBuggy` (vạch trần rõ ràng 3 ca `testDraw_*` ném ngoại lệ `NoSuchElementException` hoặc rút sai lá bài nếu chưa sửa lỗi logic).
* **`src/test/java/designbook/solutions5/CoverageAnalyzerDemo.java`**
  * **Vai trò:** Chương trình độc lập phân tích đồ thị luồng điều khiển (CFG), giải thích cơ chế ngắn mạch của toán tử `&&` và in ma trận độ bao phủ nhánh.
* **`src/test/java/designbook/solutions5/TestRunner.java`**
  * **Vai trò:** Trình chạy kiểm thử tự động độc lập xây dựng bằng Java Reflection API, quét và thực thi các phương thức `@Test` mà không phụ thuộc vào Maven hay IDE.

#### B. Thư viện và Cấu hình dự án
* **`lib/`:** Thư viện JAR độc lập JUnit Jupiter 5.10.2 (API, Engine, Platform Commons, Platform Engine, OpenTest4j, APIGuardian).
* **`pom.xml`:** Cấu hình Maven chuẩn Java 21 LTS (`maven-compiler-plugin`, `maven-surefire-plugin`).
* **`test.bat` / `run.bat` / `build_and_run.bat` / `build_and_run.ps1`:** Các script tự động hóa biên dịch và thực thi trên Windows.
* **`BAO_CAO_LAB_06_EXERCISE_11.docx`:** Báo cáo thực hành hoàn chỉnh cấu trúc học thuật 6 Chương theo chuẩn `Post_Class_06`.

---

## 📁 Cấu trúc Thư mục Dự án (Project Layout)

```text
LAB_06/
├── lib/                                                # Bộ thư viện JUnit Jupiter 5.10.2 độc lập
│   ├── apiguardian-api-1.1.2.jar
│   ├── junit-jupiter-api-5.10.2.jar
│   ├── junit-jupiter-engine-5.10.2.jar
│   ├── junit-platform-commons-1.10.2.jar
│   ├── junit-platform-engine-1.10.2.jar
│   └── opentest4j-1.2.0.jar
│
├── src/
│   ├── main/java/                                      # Mã nguồn sản phẩm (Production Code)
│   │   ├── e3/chapter4/                                # Domain Model chuẩn Edition 3 (code-e3)
│   │   │   ├── CardSource.java                         # Interface CardSource + Null Object pattern
│   │   │   ├── Card.java                               # Lá bài Flyweight (card.rank(), card.suit())
│   │   │   ├── Rank.java                               # Enum thứ bậc (ACE..KING)
│   │   │   └── Suit.java                               # Enum chất (CLUBS, DIAMONDS, HEARTS, SPADES)
│   │   └── e3/chapter5/
│   │       ├── BiCardSource.java                       # [SỬA ĐỔI] Đã sửa Seeded Bug & Precondition
│   │       └── BiCardSourceBuggy.java                  # [THÊM MỚI] Bản gốc chứa Seeded Bug để đối chứng
│   │
│   └── test/java/designbook/solutions5/                # Mã nguồn kiểm thử (Test Code)
│       ├── Exercise11.java                             # [THÊM MỚI] Test Suite với Stubs đạt 100% Branch Coverage
│       ├── Exercise11BugDemonstrator.java              # [THÊM MỚI] Suite chứng minh vạch trần Seeded Bug
│       ├── TestRunner.java                             # [THÊM MỚI] Runner chạy test độc lập qua Reflection
│       └── CoverageAnalyzerDemo.java                   # [THÊM MỚI] Phân tích luồng CFG & ma trận nhánh
│
├── pom.xml                                             # [THÊM MỚI] Cấu hình Maven tiêu chuẩn (Java 21 LTS)
├── test.bat                                            # [THÊM MỚI] Script 1-click chạy TestRunner
├── run.bat                                             # [THÊM MỚI] Script 1-click chạy CoverageAnalyzerDemo
├── build_and_run.bat / .ps1                            # [THÊM MỚI] Script biên dịch và chạy toàn diện
├── README.md                                           # Hướng dẫn kỹ thuật và danh mục dự án
└── BAO_CAO_LAB_06_EXERCISE_11.docx                     # Báo cáo học thuật 6 Chương hoàn chỉnh
```

---

## 🔍 Phân tích Kỹ thuật Cốt lõi

### 1. Kỹ thuật Test Stub (`CardSourceStub`)
- Sử dụng `Optional<Card>` làm trạng thái nội bộ, mô phỏng hoàn hảo hành vi của một nguồn bài chỉ chứa 0 hoặc 1 lá bài:
  ```java
  private static class CardSourceStub implements CardSource {
      private Optional<Card> aCard = Optional.empty();
      CardSourceStub() {}
      CardSourceStub(Card pCard) { aCard = Optional.of(pCard); }
      public Card draw() {
          assert !isEmpty() : "Cannot draw from an empty CardSourceStub";
          Card card = aCard.get();
          aCard = Optional.empty();
          return card;
      }
      public boolean isEmpty() { return !aCard.isPresent(); }
  }
  ```
- **Lợi ích:** Tách biệt hoàn toàn khỏi lớp `Deck`, loại bỏ tính ngẫu nhiên của `shuffle()`, tốc độ thực thi nano giây và đơn định 100%.

### 2. Ma trận Độ bao phủ Nhánh (100% Branch Coverage)
- **`isEmpty()` (`aSource1.isEmpty() && aSource2.isEmpty()`):**
  - Nhánh 1: `True && True` -> `true` (`testEmpty_True`).
  - Nhánh 2: `True && False` -> `false` (`testEmpty_False_FirstEmpty`).
  - Nhánh 3: `False && _` (ngắt ngắn mạch) -> `false` (`testEmpty_False_SecondEmpty`, `testEmpty_False_NeitherEmpty`).
- **`draw()` (`if (!aSource1.isEmpty())`):**
  - Nhánh True: Nguồn 1 còn bài -> rút từ Nguồn 1 (`testDraw_SecondEmpty`, `testDraw_NeitherEmpty`).
  - Nhánh False: Nguồn 1 rỗng -> rút từ Nguồn 2 (`testDraw_FirstEmpty`).

---

## 📊 Bảng Tổng hợp các Ca Kiểm thử (Test Suite)

| STT | Phương thức Test | Trạng thái Nguồn 1 | Trạng thái Nguồn 2 | Nhánh Logic Thực thi | Kết quả mong đợi |
| :---: | :--- | :--- | :--- | :--- | :---: |
| 1 | `testEmpty_True` | Rỗng | Rỗng | `isEmpty()` T && T | `true` |
| 2 | `testEmpty_False_FirstEmpty` | Rỗng | Chứa ACE | `isEmpty()` T && F | `false` |
| 3 | `testEmpty_False_SecondEmpty` | Chứa ACE | Rỗng | `isEmpty()` F (ngắn mạch) | `false` |
| 4 | `testEmpty_False_NeitherEmpty` | Chứa ACE | Chứa TWO | `isEmpty()` F (ngắn mạch) | `false` |
| 5 | `testDraw_FirstEmpty` | Rỗng | Chứa ACE | `draw()` Nhánh Else (Nguồn 2) | Trả về `ACE` |
| 6 | `testDraw_SecondEmpty` | Chứa ACE | Rỗng | `draw()` Nhánh If (Nguồn 1) | Trả về `ACE` |
| 7 | `testDraw_NeitherEmpty` | Chứa ACE | Chứa TWO | `draw()` Nhánh If (Nguồn 1) | Trả về `ACE` |
| 8 | `testIsNull` | Rỗng | Rỗng | Phương thức mặc định interface | `false` |
| 9 | `testDraw_SequentialTransition` | Chứa ACE | Chứa TWO | Chuyển tiếp tuần tự S1 -> S2 | Rút ACE rồi rút TWO |
| 10-13 | `Exercise11BugDemonstrator` | Đa dạng | Đa dạng | 4 test đối chứng trên bản lỗi | Xác nhận Seeded Bug |
