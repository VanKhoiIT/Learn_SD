package lab09.orderprocessing.concrete;

import lab09.orderprocessing.domain.Order;
import lab09.orderprocessing.template.OrderProcessor;

/**
 * Lớp cụ thể xử lý Đơn hàng Tiêu chuẩn (Standard Order) giao trong nước.
 * - Thanh toán: Cổng thanh toán nội địa (Thẻ ATM, Ví MoMo, VNPay, hoặc COD).
 * - Đóng gói: Thùng carton tiêu chuẩn, chèn giấy bọc khí chống sốc cơ bản.
 * - Vận chuyển: Giao hàng nhanh đường bộ (GHTK / GHN / ViettelPost) trong 2-4 ngày.
 */
public class StandardOrderProcessor extends OrderProcessor {

    @Override
    protected boolean processPayment(Order order) {
        order.addLog(String.format("[StandardOrderProcessor] Kết nối Cổng VNPay/MoMo: Trừ tiền %,.0f VNĐ từ tài khoản nội địa.", 
                order.getFinalTotal()));
        return true;
    }

    @Override
    protected void packItems(Order order) {
        order.addLog(String.format("[StandardOrderProcessor] Đóng hộp carton tiêu chuẩn cho %d sản phẩm, dán băng keo niêm phong kho.",
                order.getItems().size()));
    }

    @Override
    protected String assignCarrier(Order order) {
        String tracking = "VN-STD-" + System.currentTimeMillis() % 1000000;
        order.addLog("[StandardOrderProcessor] Bàn giao hãng vận chuyển GHTK đường bộ (Dự kiến giao 2-3 ngày).");
        return tracking;
    }

    @Override
    protected void postProcessing(Order order) {
        int points = (int) (order.getFinalTotal() / 10000);
        order.addLog(String.format("[StandardOrderProcessor - Hook] Tích lũy +%d điểm thưởng Shopee/Tiki Xu cho khách hàng.", points));
    }
}
