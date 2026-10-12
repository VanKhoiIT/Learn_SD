package test;

import service.PaymentService;
import strategy.OtpAuthenticatable;
import strategy.PaymentStrategy;
import strategy.Refundable;
import strategy.impl.BankTransferPayment;
import strategy.impl.CreditCardPayment;
import strategy.impl.PayPalPayment;

/**
 * Kiểm thử tự động (Unit Test / Integration Test) độc lập,
 * chứng minh tính dễ kiểm thử (High Testability) và tính đúng đắn của thiết kế mới.
 */
public class PaymentTest {

    private static int testsPassed = 0;
    private static int testsTotal = 0;

    private static void assertTrue(String testName, boolean condition) {
        testsTotal++;
        if (condition) {
            testsPassed++;
            System.out.println("  [PASS] " + testName);
        } else {
            System.err.println("  [FAIL] " + testName);
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       CHẠY BỘ KIỂM THỬ TỰ ĐỘNG (UNIT TESTS)      ");
        System.out.println("==================================================");

        testContextThrowsExceptionWhenNoStrategy();
        testInvalidAmountThrowsException();
        testCreditCardOtpVerification();
        testIspSeparation();
        testMockPaymentStrategy();

        System.out.println("\n--------------------------------------------------");
        System.out.println("KẾT QUẢ: " + testsPassed + "/" + testsTotal + " tests passed!");
        System.out.println("==================================================");
    }

    private static void testContextThrowsExceptionWhenNoStrategy() {
        System.out.println("\n1. Test: PaymentService ném ngoại lệ khi chưa cấu hình Strategy:");
        PaymentService service = new PaymentService();
        boolean caught = false;
        try {
            service.pay(100_000);
        } catch (IllegalStateException e) {
            caught = true;
        }
        assertTrue("Chưa set strategy phải ném IllegalStateException", caught);
    }

    private static void testInvalidAmountThrowsException() {
        System.out.println("\n2. Test: PaymentService ném ngoại lệ khi số tiền <= 0:");
        PaymentService service = new PaymentService(new PayPalPayment("test@example.com"));
        boolean caught = false;
        try {
            service.pay(-500);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        assertTrue("Số tiền âm phải ném IllegalArgumentException", caught);
    }

    private static void testCreditCardOtpVerification() {
        System.out.println("\n3. Test: OTP Authentication trên Thẻ tín dụng:");
        CreditCardPayment card = new CreditCardPayment("1111222233334444", "123", "12/25");
        assertTrue("Mã OTP sai (không đủ 6 số) phải trả về false", !card.verifyOtp("123"));
        assertTrue("Mã OTP đúng (6 số) phải trả về true", card.verifyOtp("123456"));
    }

    private static void testIspSeparation() {
        System.out.println("\n4. Test: Kiểm tra nguyên lý phân tách giao diện ISP:");
        PaymentStrategy bank = new BankTransferPayment("Vietcombank", "123456");
        PaymentStrategy paypal = new PayPalPayment("test@example.com");

        assertTrue("BankTransferPayment KHÔNG được triển khai OtpAuthenticatable (ISP)", !(bank instanceof OtpAuthenticatable));
        assertTrue("BankTransferPayment KHÔNG được triển khai Refundable (ISP)", !(bank instanceof Refundable));
        assertTrue("PayPalPayment KHÔNG triển khai OtpAuthenticatable", !(paypal instanceof OtpAuthenticatable));
        assertTrue("PayPalPayment CÓ triển khai Refundable", paypal instanceof Refundable);
    }

    private static void testMockPaymentStrategy() {
        System.out.println("\n5. Test: Dễ dàng Mock Object để kiểm thử độc lập mà không cần cổng thanh toán thật:");
        class MockStrategy implements PaymentStrategy {
            boolean wasCalled = false;
            double paidAmount = 0;
            @Override
            public void pay(double amount) {
                wasCalled = true;
                paidAmount = amount;
            }
        }

        MockStrategy mock = new MockStrategy();
        PaymentService service = new PaymentService(mock);
        service.pay(750_000);

        assertTrue("Mock Strategy được gọi thành công từ PaymentService", mock.wasCalled);
        assertTrue("Mock Strategy nhận đúng số tiền 750,000", mock.paidAmount == 750_000);
    }
}
