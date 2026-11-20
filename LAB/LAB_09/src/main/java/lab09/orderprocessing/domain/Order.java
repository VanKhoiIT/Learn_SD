package lab09.orderprocessing.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Đại diện cho thông tin đơn hàng trong hệ thống thương mại điện tử.
 */
public class Order {
    private final String orderId;
    private final String customerName;
    private final String customerEmail;
    private final String shippingAddress;
    private final OrderType orderType;
    private final List<OrderItem> items = new ArrayList<>();
    
    private String discountCode;
    private double discountAmount = 0.0;
    private boolean giftWrappingRequested = false;
    private boolean isPaid = false;
    private String trackingCode;
    private final List<String> processingLogs = new ArrayList<>();

    public Order(String orderId, String customerName, String customerEmail, String shippingAddress, OrderType orderType) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ!");
        }
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.shippingAddress = shippingAddress;
        this.orderType = orderType;
    }

    public void addItem(OrderItem item) {
        if (item != null) {
            items.add(item);
        }
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    public double getFinalTotal() {
        double total = getSubtotal() - discountAmount;
        return Math.max(0.0, total);
    }

    public void applyDiscount(String code, double amount) {
        this.discountCode = code;
        this.discountAmount = amount;
    }

    public void addLog(String message) {
        processingLogs.add(message);
    }

    // Getters & Setters
    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getShippingAddress() { return shippingAddress; }
    public OrderType getOrderType() { return orderType; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public String getDiscountCode() { return discountCode; }
    public double getDiscountAmount() { return discountAmount; }
    public boolean isGiftWrappingRequested() { return giftWrappingRequested; }
    public void setGiftWrappingRequested(boolean giftWrappingRequested) { this.giftWrappingRequested = giftWrappingRequested; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
    public String getTrackingCode() { return trackingCode; }
    public void setTrackingCode(String trackingCode) { this.trackingCode = trackingCode; }
    public List<String> getProcessingLogs() { return Collections.unmodifiableList(processingLogs); }
}

