# LAB 09: THIẾT KẾ VÀ CÀI ĐẶT MẪU THIẾT KẾ TEMPLATE METHOD
**Chủ đề:** Hệ thống Xử lý Đơn hàng Thương mại Điện tử Đa kênh (E-Commerce Multi-Channel Order Processing Pipeline)  
**Môn học:** Thiết kế Phần mềm (Software Design with Java)

---

## 1. Giới thiệu bài toán thực tế
Trong các sàn thương mại điện tử hiện đại (như Shopee, Lazada, Amazon), quy trình xử lý đơn đặt hàng gồm một chuỗi các bước chuẩn hóa nghiêm ngặt:
1. **Kiểm tra tính hợp lệ của đơn hàng (Validate Order):** Kiểm tra mã khách hàng, danh sách mặt hàng, số lượng > 0.
2. **Tính toán giảm giá & phụ phí (Apply Discounts):** Chiết khấu theo cấp độ thành viên, phụ thu đặc thù.
3. **Xử lý thanh toán (Process Payment):** Tùy từng loại hình (Tiền mặt/Thẻ/MoMo, Apple Pay, Cổng Stripe đa ngoại tệ, Tự động hóa qua ví).
4. **Đóng gói sản phẩm (Pack Items):** Quy cách đóng gói (Thùng carton, Hộp carton ưu tiên chống sốc, Thùng kiện kiểm định IATA quốc tế, Đóng gói kỹ thuật số mã hóa license).
5. **Gói quà tặng kèm thiệp (Hook - Gift Wrapping):** Bước tùy chọn (Hook), mặc định bỏ qua nếu khách hàng không yêu cầu.
6. **Điều phối đơn vị vận chuyển (Assign Carrier):** Giao Tiết Kiệm (GHTK đường bộ), GrabExpress 2h, Hàng không DHL Express Quốc tế, Phân phối qua đường link CDN đám mây.
7. **Gửi hóa đơn & Thông báo điện tử (Send Notification):** Gửi email/SMS xác nhận kèm e-Invoice chuẩn hóa.
8. **Hậu xử lý mở rộng (Hook - Post Processing):** Tích điểm thưởng loyalty, kích hoạt bảo hiểm hỏa tốc, khai báo hải quan điện tử, hoặc kích hoạt bản quyền trên License Server.

---

## 2. Kiến trúc áp dụng Template Method Pattern

### 2.1. Phân loại các bước trong Template Method
Lớp trừu tượng `OrderProcessor` định nghĩa khung thuật toán trong phương thức `final`:
```java
public abstract class OrderProcessor {
    // TEMPLATE METHOD: Đóng băng khung thuật toán, không cho lớp con ghi đè
    public final OrderResult processOrder(Order order) {
        // Step 1 (Concrete): Xác thực đơn hàng
        validateOrder(order);

        // Step 2 (Concrete): Áp dụng giảm giá
        double finalAmount = applyDiscounts(order);

        // Step 3 (Primitive Abstract): Xử lý thanh toán
        String paymentInfo = processPayment(order, finalAmount);

        // Step 4 (Primitive Abstract): Đóng gói
        String packagingInfo = packItems(order);

        // Step 5 (Hook Method): Gói quà tùy chọn
        if (isGiftWrappingRequested(order)) {
            packagingInfo += " | " + wrapGift(order);
        }

        // Step 6 (Primitive Abstract): Điều phối vận chuyển
        String trackingCode = assignCarrier(order);

        // Step 7 (Concrete): Gửi thông báo & e-Invoice
        sendNotification(order, trackingCode, finalAmount);

        // Step 8 (Hook Method): Hậu xử lý tùy biến
        postProcessing(order);

        return new OrderResult(...);
    }
}
```

### 2.2. Các lớp dẫn xuất cụ thể (Concrete Classes)
1. **`StandardOrderProcessor`**: Đơn hàng nội địa tiêu chuẩn (Thanh toán Thẻ nội địa/MoMo, đóng gói thùng carton tiêu chuẩn, giao hàng GHTK đường bộ, tích điểm khách hàng thân thiết).
2. **`ExpressOrderProcessor`**: Đơn hàng hỏa tốc nội đô 2h (Apple Pay/Thẻ quốc tế, hộp chống sốc dán tem đỏ "HỎA TỐC 2H", hook gói quà nơ nhung & thiệp viết tay, GrabExpress, kích hoạt gói bảo hiểm giao trễ 2h).
3. **`InternationalOrderProcessor`**: Đơn hàng xuyên biên giới (Stripe Gateway quy đổi tỷ giá USD 25,000 VND, đóng gói chuẩn IATA, hook khai báo hải quan điện tử e-Customs, DHL Express Air Waybill).
4. **`DigitalOrderProcessor`**: Đơn hàng sản phẩm số / E-Book / Bản quyền phần mềm (Thanh toán trực tuyến 100%, đóng gói số sinh License Key SHA-256, link tải Cloudflare CDN, hook kích hoạt License Server).

---

## 3. Cấu trúc thư mục dự án
```text
LAB_09/
│
├── diagrams/                                   # Sơ đồ UML
│   ├── order_processor_class_diagram.mmd       # Mermaid Class Diagram
│   ├── order_processor_class_diagram.png       # Ảnh Class Diagram (High-res)
│   ├── order_processor_sequence_diagram.mmd    # Mermaid Sequence Diagram
│   └── order_processor_sequence_diagram.png    # Ảnh Sequence Diagram (High-res)
│
├── lib/                                        # Thư viện JUnit 5 (Stand-alone)
│   ├── junit-jupiter-api-5.10.2.jar
│   ├── junit-jupiter-engine-5.10.2.jar
│   ├── junit-platform-commons-1.10.2.jar
│   ├── junit-platform-engine-1.10.2.jar
│   ├── opentest4j-1.2.0.jar
│   └── apiguardian-api-1.1.2.jar
│
├── src/
│   ├── main/java/lab09/orderprocessing/        # Mã nguồn nghiệp vụ chính
│   │   ├── domain/                             # 1. Dữ liệu thực thể nghiệp vụ
│   │   │   ├── Order.java                      # Model đơn hàng
│   │   │   ├── OrderItem.java                  # Model mặt hàng chi tiết
│   │   │   ├── OrderResult.java                # Model kết quả xử lý & audit logs
│   │   │   └── OrderType.java                  # Enum phân loại đơn hàng
│   │   │
│   │   ├── template/                           # 2. Khung trừu tượng Template Method
│   │   │   └── OrderProcessor.java             # AbstractClass định nghĩa final processOrder
│   │   │
│   │   ├── concrete/                           # 3. Các lớp hiện thực hóa cụ thể (ConcreteClasses)
│   │   │   ├── StandardOrderProcessor.java     # Lớp xử lý đơn tiêu chuẩn nội địa
│   │   │   ├── ExpressOrderProcessor.java      # Lớp xử lý đơn hỏa tốc 2h
│   │   │   ├── InternationalOrderProcessor.java# Lớp xử lý đơn quốc tế xuyên biên giới
│   │   │   └── DigitalOrderProcessor.java      # Lớp xử lý đơn sản phẩm số
│   │   │
│   │   └── MainDemo.java                       # 4. Chương trình chạy 5 kịch bản thực nghiệm
│   │
│   └── test/java/lab09/orderprocessing/        # Kiểm thử tự động
│       ├── OrderProcessorTest.java             # 6 ca kiểm thử JUnit 5 chi tiết
│       └── TestRunner.java                     # Bộ chạy test console độc lập
│
├── SAN_PHAM_NOP_BAI/                           # Toàn bộ sản phẩm nộp bài đã phân loại
│   ├── 1_Bao_Cao/                              # Báo cáo chuẩn Word (2-3 trang chuyên nghiệp)
│   │   └── BAO_CAO_LAB_09_TEMPLATE_METHOD.docx
│   ├── 2_So_Do_UML/                            # Ảnh sơ đồ UML lớp và sơ đồ tuần tự
│   │   ├── order_processor_class_diagram.png
│   │   └── order_processor_sequence_diagram.png
│   ├── 3_Ket_Qua_Thuc_Nghiem/                  # Ảnh chụp màn hình và log chạy thực nghiệm
│   │   ├── anh_chup_ket_qua_thuc_nghiem.png
│   │   └── ket_qua_thuc_nghiem.txt
│   └── 4_Ma_Nguon_Java/                        # Toàn bộ mã nguồn Java chuẩn bị nộp
│       ├── domain/
│       ├── template/
│       ├── concrete/
│       └── MainDemo.java
│
├── build_and_run.bat                           # Script tự động biên dịch và chạy demo + test
├── run.bat / run.ps1                           # Script chạy nhanh MainDemo
├── test.bat / test.ps1                         # Script chạy nhanh JUnit Test
├── pom.xml                                     # File cấu hình Maven tiêu chuẩn
└── README.md                                   # Tài liệu dự án
```

---

## 4. Hướng dẫn biên dịch và thực thi

### Cách 1: Sử dụng script Batch (Khuyên dùng trên Windows)
Chỉ cần nhấp đúp chuột hoặc chạy trên Terminal:
```powershell
.\build_and_run.bat
```
Script sẽ tự động:
1. Biên dịch toàn bộ mã nguồn Java với `javac --release 21`.
2. Chạy 5 kịch bản trong `MainDemo`.
3. Chạy toàn bộ 6 ca kiểm thử `OrderProcessorTest` với JUnit 5 Engine.

### Cách 2: Biên dịch và chạy thủ công qua Terminal
```powershell
# Tạo thư mục bin nếu chưa có
mkdir bin

# Biên dịch mã nguồn và test
javac --release 21 -encoding UTF-8 -cp "lib/*" -d bin src/main/java/lab09/orderprocessing/*.java src/test/java/lab09/orderprocessing/*.java

# Chạy Demo
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" lab09.orderprocessing.MainDemo

# Chạy Test tự động
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" lab09.orderprocessing.TestRunner
```

---

## 5. Kết quả kiểm thử JUnit 5
Cả 7 ca kiểm thử đều đạt kết quả **PASSED (100%)**:
1. `testStandardOrderProcessing`: Kiểm tra luồng xử lý đơn nội địa tiêu chuẩn, giảm giá 5%, GHTK, tích điểm loyalty.
2. `testExpressOrderWithGiftWrapHook`: Kiểm tra đơn hỏa tốc 2h, hook gói quà nơ thiệp, phụ thu hỏa tốc +20,000 VND, GrabExpress.
3. `testInternationalOrderCurrencyAndCustoms`: Kiểm tra đơn hàng xuyên biên giới, tỷ giá quy đổi USD (25,000 VND/USD), đóng gói IATA, DHL Express.
4. `testDigitalOrderLicenseKeyGeneration`: Kiểm tra đơn hàng sản phẩm số, sinh License Key SHA-256, Cloud CDN, không bao giờ gói quà vật lý.
5. `testInvalidOrderValidationFailure`: Kiểm tra tính toàn vẹn của Template Method khi quăng ngoại lệ `IllegalArgumentException` lúc danh sách mặt hàng rỗng hoặc số lượng không hợp lệ.
6. `testNullOrderThrowsException`: Kiểm tra truyền tham số null kích hoạt ngoại lệ `IllegalArgumentException`.
7. `testTemplateMethodIsFinal`: Xác nhận phương thức `processOrder()` là `final` (bằng Java Reflection), bảo vệ quy trình thuật toán không bị sửa đổi bởi các lớp con.

