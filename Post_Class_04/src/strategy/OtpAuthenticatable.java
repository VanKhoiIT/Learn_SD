package strategy;

/**
 * Tuân thủ ISP: Chỉ các phương thức thanh toán yêu cầu xác thực 2 bước (OTP) mới cài đặt giao diện này.
 */
public interface OtpAuthenticatable {
    void sendOtp();
    boolean verifyOtp(String otp);
}
