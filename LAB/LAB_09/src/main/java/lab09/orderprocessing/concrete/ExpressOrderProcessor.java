package lab09.orderprocessing.concrete;

import lab09.orderprocessing.domain.Order;
import lab09.orderprocessing.template.OrderProcessor;

/**
 * Lớp cụ thể xử lý Đơn hàng Hỏa tốc (Express Order) giao nội thành trong 2 giờ.
 * - Thanh toán: Bắt buộc thanh toán trực tuyến tức thì (Thẻ tín dụng / Apple Pay) để xuất kho ngay.
 * - Đóng gói: Dán nhãn nhận diện ưu tiên "HỎA TỐC 2H", hỗ trợ Hook gói quà tặng nếu khách yêu cầu.
 * - Vận chuyển: Điều phối shipper công nghệ (GrabExpress / Ahamove) lấy hàng và giao ngay lập tức.
 */
public class ExpressOrderProcessor extends OrderProcessor {

    @Override
    protected boolean processPayment(Order order) {
        order.addLog(String.format("[ExpressOrderProcessor] Xác thực thanh toán trực tuyến tức thì qua Apple Pay/Visa: Trừ %,.0f VNĐ.",
                order.getFinalTotal()));
        return true;
    }

    @Override
    protected void packItems(Order order) {
        order.addLog(String.format("[ExpressOrderProcessor] Line đóng gói ưu tiên: Bọc bảo vệ nhanh, dán tem cảnh báo 'HỎA TỐC GIAO TRONG 2 GIỜ'.",
                order.getItems().size()));
    }

    @Override
    protected String assignCarrier(Order order) {
        String tracking = "GRAB-EXP-" + System.currentTimeMillis() % 1000000;
        order.addLog("[ExpressOrderProcessor] Kết nối API GrabExpress: Đã gán tài xế nhận hàng trực tiếp tại kho nội thành.");
        return tracking;
    }

    @Override
    protected boolean isGiftWrappingRequested(Order order) {
        return order.isGiftWrappingRequested();
    }

    @Override
    protected void postProcessing(Order order) {
        order.addLog("[ExpressOrderProcessor - Hook] Kích hoạt bảo hiểm cam kết đúng giờ: Hoàn tiền 100% cước phí nếu giao quá 2 giờ.");
    }
}

