package lab09.orderprocessing.concrete;

import lab09.orderprocessing.domain.Order;
import lab09.orderprocessing.template.OrderProcessor;

/**
 * Lớp cụ thể xử lý Đơn hàng Quốc tế (International Order).
 * - Thanh toán: Cổng thanh toán quốc tế (PayPal / Stripe / Wire Transfer) kèm quy đổi tỷ giá ngoại tệ (USD/EUR).
 * - Đóng gói: Đạt chuẩn vận tải hàng không quốc tế (IATA), bọc chống va đập 3 lớp màng khí, dán nhãn song ngữ.
 * - Vận chuyển: Hãng chuyển phát nhanh toàn cầu (DHL Express / FedEx) kèm tạo vận đơn hàng không (Air Waybill).
 * - Hậu xử lý (Hook): Tự động tạo hồ sơ khai báo hải quan (Customs Declaration Manifest).
 */
public class InternationalOrderProcessor extends OrderProcessor {

    private static final double EXCHANGE_RATE_VND_TO_USD = 25000.0;

    @Override
    protected boolean processPayment(Order order) {
        double usdAmount = order.getFinalTotal() / EXCHANGE_RATE_VND_TO_USD;
        order.addLog(String.format("[InternationalOrderProcessor] Cổng quốc tế Stripe: Quy đổi %,.0f VNĐ = $%.2f USD (Tỷ giá: %,.0f). Trừ thẻ quốc tế thành công.",
                order.getFinalTotal(), usdAmount, EXCHANGE_RATE_VND_TO_USD));
        return true;
    }

    @Override
    protected void packItems(Order order) {
        order.addLog("[InternationalOrderProcessor] Đóng gói chuẩn IATA vận tải hàng không quốc tế: Màng co chống thấm 3 lớp, hút chân không, dán nhãn song ngữ Fragile/This Way Up.");
    }

    @Override
    protected String assignCarrier(Order order) {
        String tracking = "DHL-GLOBAL-" + System.currentTimeMillis() % 1000000;
        order.addLog(String.format("[InternationalOrderProcessor] Phát hành Vận đơn Hàng không (Air Waybill) của hãng DHL Express quốc tế tới: %s",
                order.getShippingAddress()));
        return tracking;
    }

    @Override
    protected void postProcessing(Order order) {
        order.addLog("[InternationalOrderProcessor - Hook] Tự động xuất điện tử Tờ khai Hải quan (Customs Clearance Declaration) và nộp thuế quan xuyên biên giới.");
    }
}
