package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

/**
 * OpenAPI: openapi-cart.yaml#/components/schemas/ProductRef
 */
public class ProductRefDTO {
    private String id;
    private String nickname;
    private String technicalName;
    private String sku;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getTechnicalName() { return technicalName; }
    public void setTechnicalName(String technicalName) { this.technicalName = technicalName; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
}
