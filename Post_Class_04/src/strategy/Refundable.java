package strategy;

/**
 * Tuân thủ ISP: Chỉ các phương thức hỗ trợ hoàn tiền trực tuyến mới cài đặt giao diện này.
 */
public interface Refundable {
    void refund(double amount);
}
