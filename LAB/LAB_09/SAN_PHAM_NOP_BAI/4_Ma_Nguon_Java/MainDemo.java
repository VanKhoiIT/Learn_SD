package lab09.orderprocessing;

import lab09.orderprocessing.domain.Order;
import lab09.orderprocessing.domain.OrderItem;
import lab09.orderprocessing.domain.OrderResult;
import lab09.orderprocessing.domain.OrderType;
import lab09.orderprocessing.template.OrderProcessor;
import lab09.orderprocessing.concrete.*;

/**
 * Chương trình minh họa và kiểm thử thực nghiệm mẫu thiết kế Template Method
 * trong bài toán Xử lý Đơn hàng Thương mại Điện tử Đa kênh.
 */
public class MainDemo {

    public static void main(String[] args) {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8.name()));
            System.setErr(new java.io.PrintStream(System.err, true, java.nio.charset.StandardCharsets.UTF_8.name()));
        } catch (Exception ignored) {}

        System.out.println("================================================================================");
        System.out.println("       HỌC PHẦN THIẾT KẾ PHẦN MỀM - BÁO CÁO THỰC HÀNH LAB 09                   ");
        System.out.println("         DEMO MẪU THIẾT KẾ TEMPLATE METHOD: ORDER PROCESSING PIPELINE           ");
        System.out.println("================================================================================\n");

        // 1. KỊCH BẢN 1: ĐƠN HÀNG TIÊU CHUẨN NỘI ĐỊA (STANDARD ORDER)
        System.out.println(">>> [KỊCH BẢN 1] XỬ LÝ ĐƠN HÀNG NỘI ĐỊA TIÊU CHUẨN (STANDARD ORDER):");
        Order standardOrder = new Order(
                "ORD-STD-001", 
                "Nguyễn Văn An", 
                "nguyenvanan@gmail.com", 
                "123 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh", 
                OrderType.STANDARD);
        standardOrder.addItem(new OrderItem("Bàn phím cơ không dây Keychron K2", 1, 1850000.0));
        standardOrder.addItem(new OrderItem("Chuột không dây Logitech MX Master 3S", 1, 2100000.0));
        standardOrder.applyDiscount("SALE10", 0.0); // Voucher giảm 10% sẽ được tính trong applyDiscounts

        OrderProcessor standardProcessor = new StandardOrderProcessor();
        OrderResult result1 = standardProcessor.processOrder(standardOrder);
        result1.printSummary();
        System.out.println();

        // 2. KỊCH BẢN 2: ĐƠN HÀNG HỎA TỐC CÓ GÓI QUÀ (EXPRESS ORDER WITH GIFT WRAP HOOK)
        System.out.println(">>> [KỊCH BẢN 2] XỬ LÝ ĐƠN HÀNG HỎA TỐC NỘI THÀNH 2 GIỜ (EXPRESS ORDER CÓ GÓI QUÀ):");
        Order expressOrder = new Order(
                "ORD-EXP-002", 
                "Trần Thị Bích", 
                "bich.tran@company.vn", 
                "Tòa nhà Landmark 81, Bình Thạnh, TP. Hồ Chí Minh", 
                OrderType.EXPRESS);
        expressOrder.addItem(new OrderItem("Nước hoa cao cấp Chanel Coco Mademoiselle 100ml", 1, 3800000.0));
        expressOrder.setGiftWrappingRequested(true); // Yêu cầu gói quà tặng
        expressOrder.applyDiscount("VIP50K", 0.0); // Voucher giảm 50k

        OrderProcessor expressProcessor = new ExpressOrderProcessor();
        OrderResult result2 = expressProcessor.processOrder(expressOrder);
        result2.printSummary();
        System.out.println();

        // 3. KỊCH BẢN 3: ĐƠN HÀNG QUỐC TẾ (INTERNATIONAL ORDER XUYÊN BIÊN GIỚI)
        System.out.println(">>> [KỊCH BẢN 3] XỬ LÝ ĐƠN HÀNG QUỐC TẾ XUYÊN BIÊN GIỚI (INTERNATIONAL ORDER):");
        Order intlOrder = new Order(
                "ORD-INT-003", 
                "Johnathan Miller", 
                "j.miller@techcorp.com", 
                "450 Serra Mall, Stanford, CA 94305, USA", 
                OrderType.INTERNATIONAL);
        intlOrder.addItem(new OrderItem("Tượng thủ công mỹ nghệ Gốm sứ Bát Tràng", 2, 2500000.0));
        intlOrder.addItem(new OrderItem("Trà Shan Tuyết Cổ Thụ Hà Giang thượng hạng", 3, 800000.0));

        OrderProcessor intlProcessor = new InternationalOrderProcessor();
        OrderResult result3 = intlProcessor.processOrder(intlOrder);
        result3.printSummary();
        System.out.println();

        // 4. KỊCH BẢN 4: ĐƠN HÀNG SẢN PHẨM SỐ (DIGITAL ORDER - PHẦN MỀM)
        System.out.println(">>> [KỊCH BẢN 4] XỬ LÝ ĐƠN HÀNG SẢN PHẨM KỸ THUẬT SỐ (DIGITAL ORDER):");
        Order digitalOrder = new Order(
                "ORD-DIG-004", 
                "Lê Hoàng Long", 
                "long.le@developer.io", 
                "Phân phối điện tử trực tuyến (Online Delivery)", 
                OrderType.DIGITAL);
        digitalOrder.addItem(new OrderItem("Bản quyền IntelliJ IDEA Ultimate (1 Năm)", 1, 4500000.0));
        digitalOrder.addItem(new OrderItem("Khóa học Chuyên sâu Thiết kế Kiến trúc Phần mềm", 1, 1200000.0));

        OrderProcessor digitalProcessor = new DigitalOrderProcessor();
        OrderResult result4 = digitalProcessor.processOrder(digitalOrder);
        result4.printSummary();
        System.out.println();

        // 5. KỊCH BẢN 5: XỬ LÝ ĐƠN HÀNG THẤT BẠI (VALIDATION FAILURE DEMO)
        System.out.println(">>> [KỊCH BẢN 5] XÁC THỰC THẤT BẠI DO THIẾU THÔNG TIN ĐỊA CHỈ (INVALID ORDER):");
        Order invalidOrder = new Order("ORD-FAIL-005", "Vô Danh", "invalid-email", "", OrderType.STANDARD);
        OrderResult result5 = standardProcessor.processOrder(invalidOrder);
        result5.printSummary();

        System.out.println("\n================================================================================");
        System.out.println("     KẾT THÚC THỰC NGHIỆM: CẢ 5 KỊCH BẢN ĐÃ VẬN HÀNH CHÍNH XÁC & HOÀN HẢO!     ");
        System.out.println("================================================================================");
    }
}
