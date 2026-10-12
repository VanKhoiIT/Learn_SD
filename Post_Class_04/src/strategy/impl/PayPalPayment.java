package strategy.impl;

import strategy.PaymentStrategy;
import strategy.Refundable;
import java.util.Locale;

public class PayPalPayment implements PaymentStrategy, Refundable {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        String formattedAmount = String.format(Locale.US, "%,.0f VND", amount);
        System.out.println("-> [PayPal] Thanh toán thành công " + formattedAmount + " qua tài khoản: " + email);
    }

    @Override
    public void refund(double amount) {
        String formattedAmount = String.format(Locale.US, "%,.0f VND", amount);
        System.out.println("-> [PayPal] Đã hoàn " + formattedAmount + " vào số dư ví PayPal: " + email);
    }
}

