package lab09.orderprocessing.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Kết quả sau khi thực thi quy trình xử lý đơn hàng.
 */
public class OrderResult {
    private final boolean success;
    private final String orderId;
    private final String trackingNumber;
    private final String message;
    private final LocalDateTime processedAt;
    private final List<String> logs;

    public OrderResult(boolean success, String orderId, String trackingNumber, String message, List<String> logs) {
        this.success = success;
        this.orderId = orderId;
        this.trackingNumber = trackingNumber;
        this.message = message;
        this.processedAt = LocalDateTime.now();
        this.logs = logs;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void printSummary() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("┌────────────────────────────────────────────────────────────────────────┐");
        System.out.println(String.format("│ KẾT QUẢ ĐƠN HÀNG: %-15s TRẠNG THÁI: %-26s │", 
                orderId, (success ? "✅ THÀNH CÔNG" : "❌ THẤT BẠI")));
        System.out.println(String.format("│ Thời gian xử lý : %-52s │", processedAt.format(dtf)));
        if (trackingNumber != null) {
            System.out.println(String.format("│ Mã vận đơn/Key  : %-52s │", trackingNumber));
        }
        System.out.println(String.format("│ Thông điệp      : %-52s │", message));
        System.out.println("├────────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ NHẬT KÝ QUY TRÌNH (TEMPLATE METHOD AUDIT LOG):                         │");
        for (String log : logs) {
            System.out.println(String.format("│  • %-67s │", log));
        }
        System.out.println("└────────────────────────────────────────────────────────────────────────┘");
    }
}

