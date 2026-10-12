package strategy.impl;

import strategy.PaymentStrategy;
import java.util.Locale;

/**
 * Chuyển khoản ngân hàng: Không triển khai OtpAuthenticatable và Refundable vì không áp dụng API tự động (Áp dụng ISP).
 */
public class BankTransferPayment implements PaymentStrategy {
    private String bankName;
    private String accountNumber;

    public BankTransferPayment(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    @Override
    public void pay(double amount) {
        String formattedAmount = String.format(Locale.US, "%,.0f VND", amount);
        System.out.println("-> [Bank Transfer] Tạo lệnh chuyển khoản " + bankName
                + " - STK: " + accountNumber + " - Số tiền: " + formattedAmount);
    }
}

