package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

import java.util.List;

public class CartDTO {
    private Long orderId;
    private String status;
    private String currency;
    private String totalAmount;
    private List<CartItem> items;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getTotalAmount() { return totalAmount; }
    public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public static class CartItem {
        private Long itemId;
        private ProductRef product;
        private int quantity;
        private String lineAmount;

        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        public ProductRef getProduct() { return product; }
        public void setProduct(ProductRef product) { this.product = product; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getLineAmount() { return lineAmount; }
        public void setLineAmount(String lineAmount) { this.lineAmount = lineAmount; }
    }

    public static class ProductRef {
        private String id;
        private String nickname;
        private String thumbnailUrl;
        private String unitAmount;
        private String currency;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }
        public String getThumbnailUrl() { return thumbnailUrl; }
        public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
        public String getUnitAmount() { return unitAmount; }
        public void setUnitAmount(String unitAmount) { this.unitAmount = unitAmount; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
    }
}
