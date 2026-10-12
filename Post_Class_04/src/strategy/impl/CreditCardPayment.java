package strategy.impl;

import strategy.PaymentStrategy;
import strategy.OtpAuthenticatable;
import strategy.Refundable;
import java.util.Locale;

public class CreditCardPayment implements PaymentStrategy, OtpAuthenticatable, Refundable {
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private boolean otpVerified = false;

    public CreditCardPayment(String cardNumber, String cvv, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    @Override
    public void sendOtp() {
        System.out.println("-> [Credit Card] Đã gửi mã OTP 3D-Secure qua SMS tới số điện thoại đăng ký chủ thẻ.");
    }

    @Override
    public boolean verifyOtp(String otp) {
        if (otp != null && otp.length() == 6) {
            this.otpVerified = true;
            System.out.println("-> [Credit Card] Xác thực mã OTP [" + otp + "] thành công.");
            return true;
        } else {
            this.otpVerified = false;
            System.out.println("-> [Credit Card] Xác thực mã OTP [" + otp + "] thất bại! Mã OTP phải gồm 6 chữ số.");
            return false;
        }
    }

    @Override
    public void pay(double amount) {
        if (!otpVerified) {
            System.out.println("-> [Credit Card - Cảnh báo] Giao dịch thất bại: Vui lòng xác thực OTP trước khi thanh toán!");
            return;
        }
        String formattedAmount = String.format(Locale.US, "%,.0f VND", amount);
        System.out.println("-> [Credit Card] Thanh toán thành công " + formattedAmount + " qua thẻ [" + maskCardNumber() + "]");
        // Đặt lại trạng thái OTP để bảo vệ giao dịch tiếp theo (bảo mật thực tế)
        this.otpVerified = false;
    }

    @Override
    public void refund(double amount) {
        String formattedAmount = String.format(Locale.US, "%,.0f VND", amount);
        System.out.println("-> [Credit Card] Đã hoàn " + formattedAmount + " vào hạn mức thẻ [" + maskCardNumber() + "]");
    }

    private String maskCardNumber() {
        if (cardNumber != null && cardNumber.length() >= 4) {
            return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
        }
        return "****-****-****-****";
    }
}

