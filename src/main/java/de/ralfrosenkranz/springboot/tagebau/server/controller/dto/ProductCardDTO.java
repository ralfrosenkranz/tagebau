package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

/**
 * OpenAPI: openapi-catalog.yaml#/components/schemas/ProductCard
 * (Auch in openapi-landing.yaml & openapi-product.yaml verwendet.)
 */
public class ProductCardDTO {
    private String id;
    private String sku;
    private String categoryId;
    private String categoryName;
    private String technicalName;
    private String nickname;
    private String condition;
    private String shortDescription;
    private PricingSummary pricing;
    private InventorySummary inventory;
    private MediaThumbDTO thumbnail;

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

    public PricingSummary getPricing() { return pricing; }
    public void setPricing(PricingSummary pricing) { this.pricing = pricing; }

    public InventorySummary getInventory() { return inventory; }
    public void setInventory(InventorySummary inventory) { this.inventory = inventory; }

    public MediaThumbDTO getThumbnail() { return thumbnail; }
    public void setThumbnail(MediaThumbDTO thumbnail) { this.thumbnail = thumbnail; }

    public static class PricingSummary {
        private String currency;
        private Long priceExorbitant;
        private Long listPriceEvenMoreExorbitant;
        private String vatNote;

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }

        public Long getPriceExorbitant() { return priceExorbitant; }
        public void setPriceExorbitant(Long priceExorbitant) { this.priceExorbitant = priceExorbitant; }

        public Long getListPriceEvenMoreExorbitant() { return listPriceEvenMoreExorbitant; }
        public void setListPriceEvenMoreExorbitant(Long listPriceEvenMoreExorbitant) { this.listPriceEvenMoreExorbitant = listPriceEvenMoreExorbitant; }

        public String getVatNote() { return vatNote; }
        public void setVatNote(String vatNote) { this.vatNote = vatNote; }
    }

    public static class InventorySummary {
        private Integer stockQty;
        private String availability;

        public Integer getStockQty() { return stockQty; }
        public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }

        public String getAvailability() { return availability; }
        public void setAvailability(String availability) { this.availability = availability; }
    }
}
