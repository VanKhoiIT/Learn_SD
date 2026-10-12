package strategy.impl;

import strategy.PaymentStrategy;
import strategy.OtpAuthenticatable;
import strategy.Refundable;
import java.util.Locale;

public class EWalletPayment implements PaymentStrategy, OtpAuthenticatable, Refundable {
    private String walletType; // MoMo, ZaloPay, ViettelMoney...
    private String phoneNumber;
    private boolean otpVerified = false;

    public EWalletPayment(String walletType, String phoneNumber) {
        this.walletType = walletType;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void sendOtp() {
        System.out.println("-> [E-Wallet " + walletType + "] Đã gửi mã OTP xác nhận tới SĐT: " + phoneNumber);
    }

    @Override
    public boolean verifyOtp(String otp) {
        if (otp != null && otp.length() == 6) {
            this.otpVerified = true;
            System.out.println("-> [E-Wallet " + walletType + "] Xác thực mã OTP [" + otp + "] thành công.");
            return true;
        } else {
            this.otpVerified = false;
            System.out.println("-> [E-Wallet " + walletType + "] Xác thực mã OTP [" + otp + "] không hợp lệ!");
            return false;
        }
    }

    @Override
    public void pay(double amount) {
        if (!otpVerified) {
            System.out.println("-> [E-Wallet " + walletType + " - Cảnh báo] Giao dịch thất bại: Vui lòng nhập OTP để xác thực ví!");
            return;
        }
        String formattedAmount = String.format(Locale.US, "%,.0f VND", amount);
        System.out.println("-> [E-Wallet " + walletType + "] Thanh toán thành công " + formattedAmount + " từ tài khoản ví SĐT: " + phoneNumber);
        // Đặt lại trạng thái OTP để bảo vệ giao dịch tiếp theo
        this.otpVerified = false;
    }

    @Override
    public void refund(double amount) {
        String formattedAmount = String.format(Locale.US, "%,.0f VND", amount);
        System.out.println("-> [E-Wallet " + walletType + "] Đã hoàn " + formattedAmount + " về ví " + walletType + " (SĐT: " + phoneNumber + ")");
    }
}

