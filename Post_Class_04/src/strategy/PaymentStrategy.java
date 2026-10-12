package strategy;

/**
 * Giao diện cơ sở cho tất cả các chiến lược thanh toán.
 */
public interface PaymentStrategy {
    void pay(double amount);
}
