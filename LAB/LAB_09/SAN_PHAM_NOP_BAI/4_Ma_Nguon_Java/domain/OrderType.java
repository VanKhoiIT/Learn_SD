package lab09.orderprocessing.domain;

/**
 * Định nghĩa các phân loại đơn hàng trong hệ thống thương mại điện tử.
 */
public enum OrderType {
    STANDARD("Đơn hàng Tiêu chuẩn (Nội địa)"),
    EXPRESS("Đơn hàng Hỏa tốc (2 Giờ)"),
    INTERNATIONAL("Đơn hàng Quốc tế (Vận chuyển Toàn cầu)"),
    DIGITAL("Đơn hàng Sản phẩm Kỹ thuật số (Phần mềm / Khóa học)");

    private final String description;

    OrderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
