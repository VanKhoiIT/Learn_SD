package strategy.impl;

import strategy.PaymentStrategy;
import java.util.Locale;

/**
 * Minh họa nguyên lý OCP (Open/Closed Principle):
 * Khi hệ thống tích hợp thêm phương thức mới (Tiền mã hóa - Crypto),
 * ta chỉ cần thêm lớp mới mà không cần chỉnh sửa bất kỳ dòng mã nào trong PaymentService.
 */
public class CryptoPayment implements PaymentStrategy {
    private String walletAddress;
    private String network; // VD: ERC-20, TRC-20, Bitcoin Network

    public CryptoPayment(String walletAddress, String network) {
        this.walletAddress = walletAddress;
        this.network = network;
    }

    @Override
    public void pay(double amount) {
        String formattedAmount = String.format(Locale.US, "%,.0f VND", amount);
        System.out.println("-> [Crypto] Thanh toán " + formattedAmount + " qua ví điện tử Blockchain ["
                + maskAddress() + "] trên mạng lưới [" + network + "]");
    }

    private String maskAddress() {
        if (walletAddress != null && walletAddress.length() > 8) {
            return walletAddress.substring(0, 4) + "..." + walletAddress.substring(walletAddress.length() - 4);
        }
        return walletAddress;
    }
}
