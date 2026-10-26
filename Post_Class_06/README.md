# BÀI TẬP POST CLASS 06: KIỂM THỬ ĐƠN VỊ LỚP POKERHAND

Dự án kiểm thử phần mềm chuyên sâu áp dụng **Java Reflection API**, **JUnit 5 Jupiter**, cùng các kỹ thuật kiểm thử hộp trắng **Statement Coverage** và **Branch Coverage** cho phương thức private `isStraightFlush()` của lớp `PokerHand`.

Dự án đối chiếu và giải quyết trọn vẹn 3 bài tập trong giáo trình *Introduction to Software Design with Java* (3rd Edition - Martin P. Robillard):
- **Exercise 5.8:** Kiểm thử phương thức private bằng Reflection, xây dựng ca kiểm thử trả về `true`, phân tích 40 sảnh theo luật Poker vs 36 sảnh UUT nhận diện, và chứng minh giới hạn kiểm thử vét cạn.
- **Exercise 5.9:** Mở rộng bộ kiểm thử đạt 100% Statement Coverage với tối thiểu **2 lần gọi UUT**.
- **Exercise 5.10:** Mở rộng bộ kiểm thử đạt 100% Branch Coverage với tối thiểu **3 lần gọi UUT** (do tác giả coi hai vế của toán tử ngắn mạch `||` là hai nhánh rẽ riêng rẽ).

---

## 1. CẤU TRÚC THƯ MỤC DỰ ÁN

```text
Post_Class_06/
│
├── .classpath                          # Cấu hình Eclipse / VS Code Java
├── .project                            # Metadata Eclipse Project
├── pom.xml                             # Cấu hình Maven chuẩn Java 21, JUnit Jupiter 5.10.2
├── README.md                           # Tài liệu hướng dẫn và phân tích học thuật
│
├── build_and_run.bat                   # Script biên dịch và chạy kiểm thử tự động (CMD)
├── build_and_run.ps1                   # Script biên dịch và chạy kiểm thử tự động (PowerShell)
├── run.bat                             # Script chạy CoverageAnalyzerDemo
├── test.bat                            # Script chạy JUnit TestRunner
│
├── lib/                                # 6 thư viện JAR JUnit Jupiter độc lập (không cần mạng)
│   ├── apiguardian-api-1.1.2.jar
│   ├── junit-jupiter-api-5.10.2.jar
│   ├── junit-jupiter-engine-5.10.2.jar
│   ├── junit-platform-commons-1.10.2.jar
│   ├── junit-platform-engine-1.10.2.jar
│   └── opentest4j-1.2.0.jar
│
└── src/
    ├── main/java/e3/chapter5/          # Mã nguồn nghiệp vụ (Core Domain - Edition 3)
    │   ├── Card.java                   # Lá bài với Suit và Rank
    │   ├── Rank.java                   # Enum thứ bậc (ACE, TWO, ..., KING)
    │   ├── Suit.java                   # Enum chất (CLUBS, DIAMONDS, HEARTS, SPADES)
    │   └── PokerHand.java              # Lớp bài Poker chứa private isStraightFlush()
    │
    └── test/java/designbook/solutions5/ # Bộ kiểm thử và công cụ phân tích
        ├── TestPokerHand.java          # Bộ kiểm thử JUnit 5 tích hợp Reflection (6 test methods)
        ├── TestRunner.java             # Bộ chạy kiểm thử console tự xây dựng bằng Reflection
        └── CoverageAnalyzerDemo.java   # Phân tích luồng điều khiển và ma trận bao phủ
```

---

## 2. HƯỚNG DẪN BIÊN DỊCH VÀ CHẠY DỰ ÁN

Dự án được cấu hình biên dịch nhắm tới **Java 21 LTS** (`--release 21`, tạo bytecode major version 65) và có thể chạy hoàn toàn độc lập mà **không cần cài đặt Maven**:

### Cách 1: Sử dụng Script tự động (Khuyến nghị)
- **Windows Command Prompt (CMD):**
  ```cmd
  build_and_run.bat
  ```
- **PowerShell:**
  ```powershell
  .\build_and_run.ps1
  ```

### Cách 2: Biên dịch và chạy bằng lệnh tiêu chuẩn
```cmd
javac --release 21 -encoding UTF-8 -cp "lib/*" -d bin src/main/java/e3/chapter5/*.java src/test/java/designbook/solutions5/*.java
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.TestRunner
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" designbook.solutions5.CoverageAnalyzerDemo
```

---

## 3. CÁC NGUYÊN TẮC HỌC THUẬT VÀ HIỆU CHỈNH CHUẨN XÁC

### 1. Về mã nguồn UUT và lớp PokerHand:
- **Phương thức UUT `isStraightFlush()`:** Thuật toán kiểm thử và logic xử lý được **giữ nguyên vẹn 100%** theo đúng giáo trình để bảo đảm tính khách quan khi kiểm chứng hành vi của đề bài (bao gồm cả hạn chế bỏ sót sảnh Royal Flush do giả định ordinal).
- **Lớp `PokerHand`:** Có 2 hiệu chỉnh kỹ thuật phụ trợ nhỏ:
  1. Thêm điều kiện `pCards != null` trong `assert` của constructor để tăng tính lập trình phòng thủ (defensive programming).
  2. Bổ sung 2 phương thức getter `cards()` và `getCards()` trả về danh sách bất biến `Collections.unmodifiableList(aCards)` để hỗ trợ quan sát trạng thái mà vẫn duy trì tính đóng gói.
- **Lý do sử dụng package `e3.chapter5` (`rank()` / `suit()`) thay vì `designbook.chapter5` (`getRank()` / `getSuit()`):**
  Đề bài trích dẫn đoạn code ban đầu theo phong cách Java Beans truyền thống thuộc Edition 2. Tuy nhiên, trên kho mã nguồn GitHub chính thức của tác giả Martin P. Robillard (`prmr/DesignBook`), dự án đã nâng cấp lên bản mới nhất (3rd Edition - 2025, package `e3.chapter5`). Ở phiên bản này, lớp `Card` áp dụng phong cách thiết kế hiện đại (Record-style accessor `rank()` và `suit()`). Do đó, dự án nộp đồng bộ theo `e3.chapter5` để vừa hiện đại vừa khớp với giáo trình Edition 3 mới nhất.

### 2. Vấn đề 40 Sảnh theo luật Poker vs 36 Sảnh theo thuật toán UUT:
- Theo luật Poker tiêu chuẩn: Có 4 chất x 10 dãy = **40 Straight Flush** (bao gồm cả sảnh A cao 10-J-Q-K-A và sảnh A thấp A-2-3-4-5).
- Theo UUT gốc: Enum `Rank` định nghĩa `ACE(0)` đứng đầu và `KING(12)` đứng cuối. Khi sắp xếp tăng dần, sảnh Royal Flush `10-J-Q-K-A` có `ACE(0)` đứng trước `TEN(9)`, khoảng cách `9 != 1` nên UUT trả về `false`. UUT chỉ nhận diện được **36 dãy liên tiếp theo ordinal** (từ A-2-3-4-5 đến 9-10-J-Q-K).
- Dự án giữ nguyên thuật toán UUT và viết test case đặc tả `testRoyalFlushKnownLimitation` khẳng định `assertFalse(...)` để ghi nhận hạn chế này.

### 3. Phân tích Không gian mẫu và Kiểm thử vét cạn (Exhaustive Testing):
- Tổng số tổ hợp 5 lá bài khả dĩ từ bộ bài 52 lá là $C(52, 5) = 2,598,960$ bộ bài.
- Trong đó, có 40 bộ Straight Flush hợp lệ theo luật (mong đợi `true`) và 2,598,920 bộ còn lại không phải Straight Flush (mong đợi `false`).
- 36 ca của UUT (hay 40 ca theo luật) chỉ chiếm **~0.00138% - 0.00154%** không gian đầu vào. Hơn 2.59 triệu trường hợp có kết quả mong đợi là `false` hoàn toàn chưa được kiểm tra, do đó **tuyệt đối không thể đạt kiểm thử vét cạn**.

### 4. Statement Coverage (Bài 5.9):
- Cần tối thiểu **đúng 2 lần gọi UUT** (TC01 đi đến `return true;` và TC02 đi đến `return false;`).

### 5. Branch Coverage (Bài 5.10) - Đáp án chính thức:
- **Đáp án chốt:** Cần tối thiểu **3 LẦN THỰC THI UUT** (TC01: True, TC02: Sai chất, TC03: Sai bậc).
- **Lý do:** Tác giả giáo trình coi hai vế của toán tử ngắn mạch `||` trong câu lệnh `if (card.suit() != suit || card.rank().ordinal() - rank.ordinal() != 1)` là hai nhánh rẽ riêng rẽ cần được kích hoạt độc lập.
- *(Ghi chú học thuật mở rộng: Nếu chỉ xét quyết định IF tổng thể ở mức True/False mà không xét rẽ nhánh ngắn mạch bên trong, Decision Coverage thuần túy cần 2 lần gọi).*

### 6. Cơ chế thực thi của TestRunner:
- `TestRunner` là một bộ chạy kiểm thử dòng lệnh độc lập tự xây dựng bằng **Java Reflection API**.
- Cơ chế: Quét runtime toàn bộ các phương thức có gắn annotation `@Test` của JUnit Jupiter, kích hoạt thực thi (`invoke`), bắt ngoại lệ và `AssertionError`, đếm số lượng PASSED/FAILED và in báo cáo ra console.
- Không sử dụng `org.junit.platform.launcher` và không cần thêm thư viện ngoài danh mục 6 file JAR trong `lib/`.

---
- **Môn học:** Thiết kế Phần mềm (Software Design)
- **Giáo trình:** *Introduction to Software Design with Java* (3rd Edition - 2025) - Martin P. Robillard
- **Mã nguồn tham khảo:** [GitHub prmr/DesignBook](https://github.com/prmr/DesignBook)
