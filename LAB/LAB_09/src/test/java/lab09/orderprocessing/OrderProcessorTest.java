package lab09.orderprocessing;

import lab09.orderprocessing.domain.*;
import lab09.orderprocessing.template.*;
import lab09.orderprocessing.concrete.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bộ kiểm thử tự động JUnit 5 cho Mẫu thiết kế Template Method (OrderProcessor).
 */
public class OrderProcessorTest {

    private Order standardOrder;
    private Order expressOrder;
    private Order internationalOrder;
    private Order digitalOrder;

    @BeforeEach
    void setUp() {
        standardOrder = new Order("TEST-STD-1", "Nguyen Van A", "a@test.com", "Hà Nội", OrderType.STANDARD);
        standardOrder.addItem(new OrderItem("Sách Design Patterns", 2, 150000.0));

        expressOrder = new Order("TEST-EXP-2", "Tran Thi B", "b@test.com", "TP HCM", OrderType.EXPRESS);
        expressOrder.addItem(new OrderItem("Bánh kem sinh nhật", 1, 500000.0));

        internationalOrder = new Order("TEST-INT-3", "David Smith", "david@us.com", "New York, USA", OrderType.INTERNATIONAL);
        internationalOrder.addItem(new OrderItem("Lụa tơ tằm Hà Đông", 1, 2000000.0));

        digitalOrder = new Order("TEST-DIG-4", "Pham Van C", "c@digital.com", "Online", OrderType.DIGITAL);
        digitalOrder.addItem(new OrderItem("Khóa học Java", 1, 990000.0));
    }

    @Test
    @DisplayName("Kiểm thử Đơn hàng Tiêu chuẩn: Quy trình chạy thành công và đúng thứ tự")
    void testStandardOrderProcessing() {
        OrderProcessor processor = new StandardOrderProcessor();
        standardOrder.applyDiscount("SALE10", 0.0);
        OrderResult result = processor.processOrder(standardOrder);

        assertTrue(result.isSuccess());
        assertTrue(standardOrder.isPaid());
        assertNotNull(result.getTrackingNumber());
        assertTrue(result.getTrackingNumber().startsWith("VN-STD-"));
        assertEquals(30000.0, standardOrder.getDiscountAmount()); // 10% của 300,000
        assertEquals(270000.0, standardOrder.getFinalTotal());

        // Kiểm tra audit logs có đủ các bước
        boolean hasCommonStep = result.getLogs().stream().anyMatch(l -> l.contains("[BƯỚC 1 - CHUNG]"));
        boolean hasCustomPayment = result.getLogs().stream().anyMatch(l -> l.contains("[StandardOrderProcessor] Kết nối Cổng VNPay/MoMo"));
        boolean hasHookPoints = result.getLogs().stream().anyMatch(l -> l.contains("Tích lũy +27 điểm thưởng"));

        assertTrue(hasCommonStep);
        assertTrue(hasCustomPayment);
        assertTrue(hasHookPoints);
    }

    @Test
    @DisplayName("Kiểm thử Đơn hàng Hỏa tốc có kích hoạt Hook gói quà tặng")
    void testExpressOrderWithGiftWrapHook() {
        OrderProcessor processor = new ExpressOrderProcessor();
        expressOrder.setGiftWrappingRequested(true);
        OrderResult result = processor.processOrder(expressOrder);

        assertTrue(result.isSuccess());
        assertTrue(result.getTrackingNumber().startsWith("GRAB-EXP-"));
        
        // Kiểm tra hook gói quà tặng được kích hoạt
        boolean giftWrapped = result.getLogs().stream().anyMatch(l -> l.contains("[HOOK - QUÀ TẶNG] Đã bọc giấy quà"));
        boolean insuranceHook = result.getLogs().stream().anyMatch(l -> l.contains("Kích hoạt bảo hiểm cam kết đúng giờ"));
        
        assertTrue(giftWrapped);
        assertTrue(insuranceHook);
    }

    @Test
    @DisplayName("Kiểm thử Đơn hàng Quốc tế: Quy đổi tỷ giá ngoại tệ và khai báo hải quan")
    void testInternationalOrderCurrencyAndCustoms() {
        OrderProcessor processor = new InternationalOrderProcessor();
        OrderResult result = processor.processOrder(internationalOrder);

        assertTrue(result.isSuccess());
        assertTrue(result.getTrackingNumber().startsWith("DHL-GLOBAL-"));

        // Kiểm tra quy đổi ngoại tệ và tờ khai hải quan
        boolean hasUsdConversion = result.getLogs().stream().anyMatch(l -> l.contains("Stripe: Quy đổi 2,000,000 VNĐ = $80.00 USD"));
        boolean hasCustomsManifest = result.getLogs().stream().anyMatch(l -> l.contains("Tờ khai Hải quan (Customs Clearance Declaration)"));

        assertTrue(hasUsdConversion);
        assertTrue(hasCustomsManifest);
    }

    @Test
    @DisplayName("Kiểm thử Đơn hàng Kỹ thuật số: Cấp phát License Key, không bọc quà vật lý")
    void testDigitalOrderLicenseKeyGeneration() {
        OrderProcessor processor = new DigitalOrderProcessor();
        digitalOrder.setGiftWrappingRequested(true); // Cố tình yêu cầu gói quà vật lý
        OrderResult result = processor.processOrder(digitalOrder);

        assertTrue(result.isSuccess());
        assertNotNull(result.getTrackingNumber());
        assertTrue(result.getTrackingNumber().startsWith("LIC-")); // Mã license

        // Kiểm tra đơn số bỏ qua hook gói quà vật lý
        boolean giftWrapped = result.getLogs().stream().anyMatch(l -> l.contains("[HOOK - QUÀ TẶNG]"));
        assertFalse(giftWrapped, "Đơn hàng kỹ thuật số không được kích hoạt hook gói quà vật lý");

        boolean licenseAudit = result.getLogs().stream().anyMatch(l -> l.contains("License Server"));
        assertTrue(licenseAudit);
    }

    @Test
    @DisplayName("Kiểm thử Đơn hàng không hợp lệ: Bị chặn ngay từ Bước 1 của Template Method")
    void testInvalidOrderValidationFailure() {
        OrderProcessor processor = new StandardOrderProcessor();
        Order emptyOrder = new Order("EMPTY-1", "No Name", "bad-email", "", OrderType.STANDARD);
        
        OrderResult result = processor.processOrder(emptyOrder);

        assertFalse(result.isSuccess());
        assertFalse(emptyOrder.isPaid());
        assertNull(result.getTrackingNumber());
        assertEquals("Xác thực đơn hàng thất bại", result.getMessage());
    }

    @Test
    @DisplayName("Kiểm thử tham số Null ném ra ngoại lệ IllegalArgumentException")
    void testNullOrderThrowsException() {
        OrderProcessor processor = new StandardOrderProcessor();
        assertThrows(IllegalArgumentException.class, () -> processor.processOrder(null));
    }

    @Test
    @DisplayName("Kiểm thử tính bất biến của Template Method: Phương thức processOrder() bắt buộc phải là 'final'")
    void testTemplateMethodIsFinal() throws NoSuchMethodException {
        java.lang.reflect.Method method = OrderProcessor.class.getDeclaredMethod("processOrder", Order.class);
        assertTrue(java.lang.reflect.Modifier.isFinal(method.getModifiers()), 
                "Template Method 'processOrder' bắt buộc phải khai báo 'final' để bảo toàn khung thuật toán (Hollywood Principle)");
    }
}

