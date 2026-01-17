package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

public class ProductCardDTO {
    private String id;
    private String nickname;
    private String technicalName;
    private String condition;
    private PriceView price;
    private String thumbnailUrl;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getTechnicalName() { return technicalName; }
    public void setTechnicalName(String technicalName) { this.technicalName = technicalName; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public PriceView getPrice() { return price; }
    public void setPrice(PriceView price) { this.price = price; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public static class PriceView {
        private String currency;
        private String amount;
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
    }
}
