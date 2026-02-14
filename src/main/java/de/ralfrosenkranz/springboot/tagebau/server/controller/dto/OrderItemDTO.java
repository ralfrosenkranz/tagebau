package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

/**
 * OpenAPI: openapi-cart.yaml#/components/schemas/OrderItem
 */
public class OrderItemDTO {
    private Long id;
    private ProductRefDTO product;
    private Integer quantity;
    private String price;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProductRefDTO getProduct() { return product; }
    public void setProduct(ProductRefDTO product) { this.product = product; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}
