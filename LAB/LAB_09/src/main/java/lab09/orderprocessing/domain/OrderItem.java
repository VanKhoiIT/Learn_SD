package lab09.orderprocessing.domain;

/**
 * Đại diện cho một mặt hàng chi tiết trong đơn hàng.
 */
public class OrderItem {
    private final String productName;
    private final int quantity;
    private final double unitPrice;

    public OrderItem(String productName, int quantity, double unitPrice) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được rỗng!");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0!");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Đơn giá không thể âm!");
        }
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return quantity * unitPrice;
    }

    @Override
    public String toString() {
        return String.format("%s (x%d) - %,.0f VNĐ", productName, quantity, getTotalPrice());
    }
}

