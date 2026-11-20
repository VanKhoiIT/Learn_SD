package lab09.orderprocessing.concrete;

import java.util.UUID;
import lab09.orderprocessing.domain.Order;
import lab09.orderprocessing.template.OrderProcessor;

/**
 * Lớp cụ thể xử lý Đơn hàng Kỹ thuật số (Digital Order - E-Book, Khóa học trực tuyến, Bản quyền phần mềm).
 * - Thanh toán: Bắt buộc 100% trực tuyến (không hỗ trợ COD).
 * - Đóng gói: Hoàn toàn không đóng gói vật lý, thay vào đó tạo mã License Key / Token kích hoạt điện tử.
 * - Vận chuyển: Phân phối tức thời qua hạ tầng Cloud (AWS S3) và gửi đường dẫn tải về qua Email/SMS trong 30 giây.
 */
public class DigitalOrderProcessor extends OrderProcessor {

    private String generatedLicenseKey;

    @Override
    protected boolean processPayment(Order order) {
        order.addLog(String.format("[DigitalOrderProcessor] Cổng thanh toán trực tuyến: Xác nhận thanh toán số tiền %,.0f VNĐ ngay lập tức.",
                order.getFinalTotal()));
        return true;
    }

    @Override
    protected void packItems(Order order) {
        this.generatedLicenseKey = "LIC-" + UUID.randomUUID().toString().substring(0, 18).toUpperCase();
        order.addLog(String.format("[DigitalOrderProcessor] Khởi tạo mã bản quyền số (License Key) và chữ ký số SHA-256: %s", generatedLicenseKey));
    }

    @Override
    protected String assignCarrier(Order order) {
        order.addLog("[DigitalOrderProcessor] Kênh phân phối số: Tạo đường link tải tốc độ cao từ Cloud CDN có hiệu lực trong 7 ngày.");
        return generatedLicenseKey;
    }

    @Override
    protected boolean isGiftWrappingRequested(Order order) {
        return false;
    }

    @Override
    protected void postProcessing(Order order) {
        order.addLog(String.format("[DigitalOrderProcessor - Hook] Tự động kích hoạt bảo hành điện tử trên hệ thống License Server cho tài khoản: %s",
                order.getCustomerEmail()));
    }
}

