package lab09.orderprocessing.template;

import lab09.orderprocessing.domain.Order;
import lab09.orderprocessing.domain.OrderResult;

/**
 * Lớp trừu tượng đóng vai trò then chốt trong Mẫu thiết kế Template Method (AbstractClass).
 * Định nghĩa bộ khung thuật toán (Algorithm Skeleton) xử lý đơn hàng bất biến,
 * đồng thời phân định rạch ròi giữa các bước dùng chung (Common Steps),
 * các bước đặc thù bắt buộc lớp con cài đặt (Primitive Operations),
 * và các bước mở rộng có thể can thiệp tùy chọn (Hook Methods).
 */
public abstract class OrderProcessor {

    /**
     * TEMPLATE METHOD: Xác định tuần tự các bước trong quy trình xử lý đơn hàng.
     * Khai báo 'final' để đảm bảo các lớp con không thể phá vỡ khung điều phối chung (Inversion of Control).
     *
     * @param order Đơn hàng cần xử lý
     * @return Kết quả xử lý đơn hàng chi tiết
     */
    public final OrderResult processOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Đơn hàng không được null!");
        }

        order.addLog("=== KHỞI ĐỘNG QUY TRÌNH XỬ LÝ: " + order.getOrderType().getDescription() + " ===");

        // BƯỚC 1: Xác thực thông tin đơn hàng (Bước chung - Concrete Step)
        if (!validateOrder(order)) {
            order.addLog("[BƯỚC 1 - THẤT BẠI] Thông tin đơn hàng không hợp lệ!");
            return new OrderResult(false, order.getOrderId(), null, "Xác thực đơn hàng thất bại", order.getProcessingLogs());
        }
        order.addLog("[BƯỚC 1 - CHUNG] Xác thực thông tin đơn hàng và giỏ hàng thành công.");

        // BƯỚC 2: Áp dụng chính sách ưu đãi / voucher (Bước chung - Concrete Step)
        applyDiscounts(order);
        order.addLog(String.format("[BƯỚC 2 - CHUNG] Khuyến mãi áp dụng: -%,.0f VNĐ. Tổng thanh toán: %,.0f VNĐ.",
                order.getDiscountAmount(), order.getFinalTotal()));

        // BƯỚC 3: Xử lý thanh toán (Bước thay đổi - Abstract Primitive Operation)
        boolean paymentSuccess = processPayment(order);
        if (!paymentSuccess) {
            order.addLog("[BƯỚC 3 - THẤT BẠI] Giao dịch thanh toán không thành công!");
            return new OrderResult(false, order.getOrderId(), null, "Thanh toán thất bại", order.getProcessingLogs());
        }
        order.setPaid(true);
        order.addLog("[BƯỚC 3 - RIÊNG] Xác nhận thanh toán thành công qua cổng thanh toán tương ứng.");

        // BƯỚC 4: Đóng gói hàng hóa (Bước thay đổi - Abstract Primitive Operation)
        packItems(order);

        // BƯỚC 5: Kiểm tra và xử lý gói quà tặng (Hook Method - Tùy chọn)
        if (isGiftWrappingRequested(order)) {
            wrapGift(order);
        }

        // BƯỚC 6: Điều phối đơn vị vận chuyển & Lấy mã vận đơn (Bước thay đổi - Abstract Primitive Operation)
        String trackingCode = assignCarrier(order);
        order.setTrackingCode(trackingCode);
        order.addLog(String.format("[BƯỚC 6 - RIÊNG] Điều phối vận chuyển thành công. Mã vận đơn: %s", trackingCode));

        // BƯỚC 7: Gửi thông báo xác nhận và hóa đơn điện tử e-Invoice (Bước chung - Concrete Step)
        sendNotification(order);
        order.addLog(String.format("[BƯỚC 7 - CHUNG] Đã phát hành hóa đơn điện tử gửi tới email: %s", order.getCustomerEmail()));

        // BƯỚC 8: Hậu xử lý mở rộng (Hook Method - Tùy chọn)
        postProcessing(order);

        order.addLog("=== HOÀN TẤT QUY TRÌNH XỬ LÝ ĐƠN HÀNG " + order.getOrderId() + " ===");

        return new OrderResult(true, order.getOrderId(), trackingCode, 
                "Xử lý thành công theo quy trình " + order.getOrderType().name(), order.getProcessingLogs());
    }

    // =========================================================================
    // 1. CÁC BƯỚC DÙNG CHUNG (CONCRETE METHODS)
    // =========================================================================

    /**
     * Bước 1: Kiểm tra tính hợp lệ cơ bản của đơn hàng (Địa chỉ, khách hàng, giỏ hàng).
     */
    protected boolean validateOrder(Order order) {
        if (order.getItems().isEmpty()) {
            return false;
        }
        if (order.getCustomerEmail() == null || !order.getCustomerEmail().contains("@")) {
            return false;
        }
        return order.getShippingAddress() != null && !order.getShippingAddress().trim().isEmpty();
    }

    /**
     * Bước 2: Kiểm tra và áp dụng mã giảm giá.
     */
    protected void applyDiscounts(Order order) {
        if (order.getDiscountCode() != null && !order.getDiscountCode().trim().isEmpty()) {
            if ("SALE10".equalsIgnoreCase(order.getDiscountCode())) {
                double discount = order.getSubtotal() * 0.10;
                order.applyDiscount("SALE10", discount);
            } else if ("VIP50K".equalsIgnoreCase(order.getDiscountCode())) {
                order.applyDiscount("VIP50K", 50000.0);
            }
        }
    }

    /**
     * Bước 7: Gửi thông báo xác nhận qua Email / SMS cho khách hàng.
     */
    protected void sendNotification(Order order) {
        // Mô phỏng tích hợp dịch vụ Email (SendGrid/Amazon SES)
    }

    // =========================================================================
    // 2. CÁC BƯỚC BẮT BUỘC LỚP CON PHẢI CÀI ĐẶT (ABSTRACT PRIMITIVE OPERATIONS)
    // =========================================================================

    /**
     * Bước 3: Phương thức thanh toán đặc thù cho từng loại đơn hàng.
     */
    protected abstract boolean processPayment(Order order);

    /**
     * Bước 4: Phương thức đóng gói hàng hóa theo quy chuẩn của từng loại đơn hàng.
     */
    protected abstract void packItems(Order order);

    /**
     * Bước 6: Điều phối kênh vận chuyển (Đường bộ, Hỏa tốc, Hàng không quốc tế, hoặc Kỹ thuật số).
     * @return Mã vận đơn (Tracking Code) hoặc Mã kích hoạt
     */
    protected abstract String assignCarrier(Order order);

    // =========================================================================
    // 3. CÁC PHƯƠNG THỨC MÓC (HOOK METHODS)
    // =========================================================================

    /**
     * Hook kiểm tra xem đơn hàng có yêu cầu đóng gói quà tặng không.
     * Mặc định trả về giá trị trong order, lớp con có thể ghi đè logic nếu cần.
     */
    protected boolean isGiftWrappingRequested(Order order) {
        return order.isGiftWrappingRequested();
    }

    /**
     * Thao tác đóng gói quà tặng kèm nơ và thiệp chúc mừng.
     */
    protected void wrapGift(Order order) {
        order.addLog("[HOOK - QUÀ TẶNG] Đã bọc giấy quà cao cấp, gắn nơ ruy băng và kèm thiệp chúc mừng cá nhân hóa.");
    }

    /**
     * Hook hậu xử lý (Audit log, tích điểm thưởng, khai báo hải quan...).
     * Mặc định là phương thức rỗng, các lớp con có thể tùy biến mà không bắt buộc.
     */
    protected void postProcessing(Order order) {
        // Mặc định không làm gì
    }
}

