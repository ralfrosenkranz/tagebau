package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

import java.util.List;

/**
 * OpenAPI: openapi-product.yaml#/components/schemas/ProductDetail
 */
public class ProductDetailDTO {
    private String id;
    private String sku;
    private String categoryId;
    private String categoryName;
    private String technicalName;
    private String nickname;
    private String condition;
    private String shortDescription;
    private String longDescriptionMarkdown;
    private ProductCardDTO.PricingSummary pricing;
    private ProductCardDTO.InventorySummary inventory;
    private Shipping shipping;
    private ProductSpecs specs;
    private Media media;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getTechnicalName() { return technicalName; }
    public void setTechnicalName(String technicalName) { this.technicalName = technicalName; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getLongDescriptionMarkdown() { return longDescriptionMarkdown; }
    public void setLongDescriptionMarkdown(String longDescriptionMarkdown) { this.longDescriptionMarkdown = longDescriptionMarkdown; }

    public ProductCardDTO.PricingSummary getPricing() { return pricing; }
    public void setPricing(ProductCardDTO.PricingSummary pricing) { this.pricing = pricing; }

    public ProductCardDTO.InventorySummary getInventory() { return inventory; }
    public void setInventory(ProductCardDTO.InventorySummary inventory) { this.inventory = inventory; }

    public Shipping getShipping() { return shipping; }
    public void setShipping(Shipping shipping) { this.shipping = shipping; }

    public ProductSpecs getSpecs() { return specs; }
    public void setSpecs(ProductSpecs specs) { this.specs = specs; }

    public Media getMedia() { return media; }
    public void setMedia(Media media) { this.media = media; }

    public static class Shipping {
        private Integer shippingCostEur;
        private Integer leadTimeDays;
        private String incotermsSuggestion;
        private String notes;

        public Integer getShippingCostEur() { return shippingCostEur; }
        public void setShippingCostEur(Integer shippingCostEur) { this.shippingCostEur = shippingCostEur; }

        public Integer getLeadTimeDays() { return leadTimeDays; }
        public void setLeadTimeDays(Integer leadTimeDays) { this.leadTimeDays = leadTimeDays; }

        public String getIncotermsSuggestion() { return incotermsSuggestion; }
        public void setIncotermsSuggestion(String incotermsSuggestion) { this.incotermsSuggestion = incotermsSuggestion; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class ProductSpecs {
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

    public static class Media {
        private List<MediaImageDTO> images;

        public List<MediaImageDTO> getImages() { return images; }
        public void setImages(List<MediaImageDTO> images) { this.images = images; }
    }
}
