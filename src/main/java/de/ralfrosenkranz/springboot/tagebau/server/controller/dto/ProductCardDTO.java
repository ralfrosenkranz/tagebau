package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

import java.util.List;

public class ProductCardDTO {
    private String id;
    private String nickname;
    private String technicalName;
    private String condition;
    private PricingDTO pricingDTO;
    private String thumbnailUrl;
    private String description;
    private String shortDescription;
    private String longDescriptionMarkdown;
    private String category;
    private String brand;
    private String status;
    private String createdAt;
    private String updatedAt;
    private ProductSpecsDTO productSpecs;
    private InventoryDTO inventory;
    private ShippingDTO shipping;
    private List<MediaImageDTO> mediaImages;
    private List<ProductCardDTO> relatedProducts;

    // Getter and Setter methods
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getTechnicalName() { return technicalName; }
    public void setTechnicalName(String technicalName) { this.technicalName = technicalName; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public PricingDTO getPricing() { return pricingDTO; }
    public void setPricing(PricingDTO price) { this.pricingDTO = price; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getLongDescriptionMarkdown() { return longDescriptionMarkdown; }
    public void setLongDescriptionMarkdown(String longDescriptionMarkdown) { this.longDescriptionMarkdown = longDescriptionMarkdown; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public ProductSpecsDTO getProductSpecs() { return productSpecs; }
    public void setProductSpecs(ProductSpecsDTO productSpecs) { this.productSpecs = productSpecs; }

    public InventoryDTO getInventory() { return inventory; }
    public void setInventory(InventoryDTO inventory) { this.inventory = inventory; }

    public ShippingDTO getShipping() { return shipping; }
    public void setShipping(ShippingDTO shipping) { this.shipping = shipping; }

    public List<MediaImageDTO> getMediaImages() { return mediaImages; }
    public void setMediaImages(List<MediaImageDTO> mediaImages) { this.mediaImages = mediaImages; }

    public List<ProductCardDTO> getRelatedProducts() { return relatedProducts; }
    public void setRelatedProducts(List<ProductCardDTO> relatedProducts) { this.relatedProducts = relatedProducts; }

    // Price and currency getters (existing methods)
    public String getPrice() {
        if (pricingDTO != null) {
            return pricingDTO.priceExorbitant;
        } else {
            return "";
        }
    }

    public String getCurrency() {
        if (pricingDTO != null) {
            return pricingDTO.currency;
        } else {
            return "";
        }
    }

    // Nested DTO classes
    public static class PricingDTO {
        private String currency;
        private String priceExorbitant;
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public String getPriceExorbitant() { return priceExorbitant; }
        public void setPriceExorbitant(String priceExorbitant) { this.priceExorbitant = priceExorbitant; }
    }

    public static class ProductSpecsDTO {
        private String weight;
        private String dimensions;
        private String material;
        private String color;
        public String getWeight() { return weight; }
        public void setWeight(String weight) { this.weight = weight; }
        public String getDimensions() { return dimensions; }
        public void setDimensions(String dimensions) { this.dimensions = dimensions; }
        public String getMaterial() { return material; }
        public void setMaterial(String material) { this.material = material; }
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
    }

    public static class InventoryDTO {
        private Integer stock;
        private String availability;
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
        public String getAvailability() { return availability; }
        public void setAvailability(String availability) { this.availability = availability; }
    }

    public static class ShippingDTO {
        private String shippingTime;
        private String shippingCost;
        private Boolean isFreeShipping;
        public String getShippingTime() { return shippingTime; }
        public void setShippingTime(String shippingTime) { this.shippingTime = shippingTime; }
        public String getShippingCost() { return shippingCost; }
        public void setShippingCost(String shippingCost) { this.shippingCost = shippingCost; }
        public Boolean getFreeShipping() { return isFreeShipping; }
        public void setFreeShipping(Boolean freeShipping) { isFreeShipping = freeShipping; }
    }

    public static class MediaImageDTO {
        private String id;
        private String url;
        private String role;
        private String altText;
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getAltText() { return altText; }
        public void setAltText(String altText) { this.altText = altText; }
    }
}
