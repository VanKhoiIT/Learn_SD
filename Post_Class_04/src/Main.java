import service.PaymentService;
import strategy.PaymentStrategy;
import strategy.impl.CreditCardPayment;
import strategy.impl.PayPalPayment;
import strategy.impl.EWalletPayment;
import strategy.impl.BankTransferPayment;
import strategy.impl.CryptoPayment;

/**
 * Client code minh họa việc sử dụng mẫu thiết kế STRATEGY và các nguyên lý SOLID (đặc biệt là ISP và OCP).
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("        HỆ THỐNG THANH TOÁN TRỰC TUYẾN (STRATEGY PATTERN & ISP)        ");
        System.out.println("======================================================================\n");

        PaymentService paymentService = new PaymentService();

        // ------------------------------------------------------------------
        // KỊCH BẢN 1: Thanh toán bằng Thẻ tín dụng (Áp dụng OTP & Refundable)
        // ------------------------------------------------------------------
        System.out.println("--- 1. GIAO DỊCH THẺ TÍN DỤNG (CREDIT CARD) ---");
        CreditCardPayment creditCard = new CreditCardPayment("4111222233331234", "888", "12/28");
        paymentService.setPaymentStrategy(creditCard);

        // Minh họa thử thanh toán khi chưa xác thực OTP
        System.out.println("[Thử nghiệm bảo mật] Gọi pay() trước khi xác thực OTP:");
        paymentService.pay(2_500_000);

        // Xác thực OTP hợp lệ
        creditCard.sendOtp();
        creditCard.verifyOtp("123456");

        // Tiến hành thanh toán và hoàn tiền
        paymentService.pay(2_500_000);
        paymentService.processRefund(500_000);

        // Kiểm tra cơ chế One-Time Password: Sau khi đã thanh toán, OTP được reset
        System.out.println("[Kiểm tra One-Time Password] Gọi pay() lần tiếp theo khi chưa xin mã OTP mới:");
        paymentService.pay(1_000_000);
        System.out.println();

        // ------------------------------------------------------------------
        // KỊCH BẢN 2: Chuyển đổi sang PayPal (Không OTP, Hỗ trợ Refundable)
        // ------------------------------------------------------------------
        System.out.println("--- 2. GIAO DỊCH PAYPAL (RUNTIME STRATEGY SWITCH) ---");
        PaymentStrategy paypal = new PayPalPayment("khachhang@example.com");
        paymentService.setPaymentStrategy(paypal);
        paymentService.pay(850_000);
        paymentService.processRefund(850_000);
        System.out.println();

        // ------------------------------------------------------------------
        // KỊCH BẢN 3: Chuyển đổi sang Ví điện tử (E-Wallet MoMo - OTP & Refundable)
        // ------------------------------------------------------------------
        System.out.println("--- 3. GIAO DỊCH VÍ ĐIỆN TỬ (E-WALLET MOMO) ---");
        EWalletPayment momo = new EWalletPayment("MoMo", "0987654321");
        paymentService.setPaymentStrategy(momo);
        momo.sendOtp();
        momo.verifyOtp("998877");
        paymentService.pay(350_000);
        paymentService.processRefund(50_000);
        System.out.println();

        // ------------------------------------------------------------------
        // KỊCH BẢN 4: Chuyển đổi sang Chuyển khoản ngân hàng (Bank Transfer)
        // Minh họa rõ ràng ISP: Không có OTP và Không hỗ trợ hoàn tiền tự động
        // ------------------------------------------------------------------
        System.out.println("--- 4. GIAO DỊCH CHUYỂN KHOẢN NGÂN HÀNG (MINH CHỨNG ISP) ---");
        PaymentStrategy bank = new BankTransferPayment("Vietcombank", "0071001234567");
        paymentService.setPaymentStrategy(bank);
        paymentService.pay(10_000_000);

        System.out.println("-> Thử yêu cầu hoàn tiền tự động qua API ngân hàng:");
        paymentService.processRefund(1_000_000);
        System.out.println();

        // ------------------------------------------------------------------
        // KỊCH BẢN 5: Kiểm tra cơ chế an toàn khi chưa chọn chiến lược
        // ------------------------------------------------------------------
        System.out.println("--- 5. KIỂM TRA NGOẠI LỆ KHI CHƯA CHỌN PHƯƠNG THỨC ---");
        PaymentService unconfiguredService = new PaymentService();
        try {
            unconfiguredService.pay(100_000);
        } catch (IllegalStateException e) {
            System.out.println("-> Bắt ngoại lệ thành công: " + e.getMessage());
        }
        System.out.println();

        // ------------------------------------------------------------------
        // KỊCH BẢN 6: Kiểm tra xác thực số tiền không hợp lệ (amount <= 0)
        // ------------------------------------------------------------------
        System.out.println("--- 6. KIỂM TRA XÁC THỰC SỐ TIỀN KHÔNG HỢP LỆ ---");
        try {
            paymentService.pay(-50_000);
        } catch (IllegalArgumentException e) {
            System.out.println("-> Bắt ngoại lệ số tiền âm: " + e.getMessage());
        }
        System.out.println();

        // ------------------------------------------------------------------
        // KỊCH BẢN 7: Minh họa nguyên lý Open/Closed (OCP)
        // Tích hợp phương thức mới (Crypto) mà không sửa một dòng nào của PaymentService
        // ------------------------------------------------------------------
        System.out.println("--- 7. MINH HỌA MỞ RỘNG PHƯƠNG THỨC MỚI (NGUYÊN LÝ OCP) ---");
        PaymentStrategy crypto = new CryptoPayment("0x71C...9A23", "Ethereum ERC-20");
        paymentService.setPaymentStrategy(crypto);
        paymentService.pay(5_200_000);
        System.out.println();

        System.out.println("======================================================================");
        System.out.println("            TẤT CẢ CÁC KỊCH BẢN ĐÃ THỰC THI THÀNH CÔNG!               ");
        System.out.println("======================================================================");
    }
}
