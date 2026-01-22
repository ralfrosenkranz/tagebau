package de.ralfrosenkranz.springboot.tagebau.server.model;

import jakarta.persistence.*;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "product_id", length = 32)
    private String productId;

    @OneToOne(optional = false)
    @MapsId
    @PrimaryKeyJoinColumn
    private Product product;

    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty;

    @Column(nullable = false, length = 64)
    private String availability;

    public String getProductId() { return productId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}
