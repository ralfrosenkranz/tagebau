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
    private ProductSpecsDTO specs;
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

    public ProductSpecsDTO getSpecs() { return specs; }
    public void setSpecs(ProductSpecsDTO specs) { this.specs = specs; }

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
        private String machineType;
        private Double operatingWeightT;
        private Double bucketCapacityM3;
        private Integer enginePowerKw;
        private Integer hoursUsed;
        private Double boomLengthM;
        private Double payloadT;
        private String tireSize;
        private Double throughputTph;
        private Integer beltWidthMm;
        private Double wheelDiameterM;
        private Integer bucketCount;
        private Double bladeCapacityM3;
        private Integer holeDiameterMm;
        private Double maxHoleDepthM;

        // Getter and Setter methods
        public String getMachineType() { return machineType; }
        public void setMachineType(String machineType) { this.machineType = machineType; }

        public Double getOperatingWeightT() { return operatingWeightT; }
        public void setOperatingWeightT(Double operatingWeightT) { this.operatingWeightT = operatingWeightT; }

        public Double getBucketCapacityM3() { return bucketCapacityM3; }
        public void setBucketCapacityM3(Double bucketCapacityM3) { this.bucketCapacityM3 = bucketCapacityM3; }

        public Integer getEnginePowerKw() { return enginePowerKw; }
        public void setEnginePowerKw(Integer enginePowerKw) { this.enginePowerKw = enginePowerKw; }

        public Integer getHoursUsed() { return hoursUsed; }
        public void setHoursUsed(Integer hoursUsed) { this.hoursUsed = hoursUsed; }

        public Double getBoomLengthM() { return boomLengthM; }
        public void setBoomLengthM(Double boomLengthM) { this.boomLengthM = boomLengthM; }

        public Double getPayloadT() { return payloadT; }
        public void setPayloadT(Double payloadT) { this.payloadT = payloadT; }

        public String getTireSize() { return tireSize; }
        public void setTireSize(String tireSize) { this.tireSize = tireSize; }

        public Double getThroughputTph() { return throughputTph; }
        public void setThroughputTph(Double throughputTph) { this.throughputTph = throughputTph; }

        public Integer getBeltWidthMm() { return beltWidthMm; }
        public void setBeltWidthMm(Integer beltWidthMm) { this.beltWidthMm = beltWidthMm; }

        public Double getWheelDiameterM() { return wheelDiameterM; }
        public void setWheelDiameterM(Double wheelDiameterM) { this.wheelDiameterM = wheelDiameterM; }

        public Integer getBucketCount() { return bucketCount; }
        public void setBucketCount(Integer bucketCount) { this.bucketCount = bucketCount; }

        public Double getBladeCapacityM3() { return bladeCapacityM3; }
        public void setBladeCapacityM3(Double bladeCapacityM3) { this.bladeCapacityM3 = bladeCapacityM3; }

        public Integer getHoleDiameterMm() { return holeDiameterMm; }
        public void setHoleDiameterMm(Integer holeDiameterMm) { this.holeDiameterMm = holeDiameterMm; }

        public Double getMaxHoleDepthM() { return maxHoleDepthM; }
        public void setMaxHoleDepthM(Double maxHoleDepthM) { this.maxHoleDepthM = maxHoleDepthM; }
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
