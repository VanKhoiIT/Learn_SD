package service;

import strategy.PaymentStrategy;
import strategy.Refundable;

/**
 * Lớp Context trong mô hình Strategy Pattern:
 * - Duy trì tham chiếu đến một đối tượng PaymentStrategy.
 * - Cho phép thay đổi chiến lược thanh toán linh hoạt tại thời điểm thực thi (Runtime).
 */
public class PaymentService {
    private PaymentStrategy strategy;

    public PaymentService() {}

    public PaymentService(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public PaymentStrategy getPaymentStrategy() {
        return this.strategy;
    }

    /**
     * Phương thức thanh toán chuẩn, tương thích với chữ ký hàm ban đầu theo đề bài.
     */
    public void pay(double amount) {
        processPayment(amount);
    }

    /**
     * Thực hiện thanh toán theo chiến lược đã được cấu hình.
     */
    public void processPayment(double amount) {
        if (strategy == null) {
            throw new IllegalStateException("Lỗi: Chưa thiết lập phương thức thanh toán!");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Lỗi: Số tiền thanh toán phải lớn hơn 0!");
        }
        strategy.pay(amount);
    }

    /**
     * Thực hiện hoàn tiền nếu phương thức thanh toán có hỗ trợ giao diện Refundable (ISP).
     */
    public void processRefund(double amount) {
        if (strategy == null) {
            throw new IllegalStateException("Lỗi: Chưa thiết lập phương thức thanh toán!");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Lỗi: Số tiền hoàn trả phải lớn hơn 0!");
        }
        if (strategy instanceof Refundable refundable) {
            refundable.refund(amount);
        } else {
            System.out.println("-> [Thông báo] Phương thức thanh toán này không hỗ trợ hoàn tiền tự động.");
        }
    }
}

