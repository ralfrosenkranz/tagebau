package de.ralfrosenkranz.springboot.tagebau.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pricing")
public class Pricing {

    @Id
    @Column(name = "product_id", length = 32)
    private String productId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "price_exorbitant", nullable = false)
    private Long priceExorbitant;

    @Column(name = "list_price_even_more_exorbitant", nullable = false)
    private Long listPriceEvenMoreExorbitant;

    @Column(name = "vat_note")
    private String vatNote;

    public String getProductId() { return productId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Long getPriceExorbitant() { return priceExorbitant; }
    public void setPriceExorbitant(Long priceExorbitant) { this.priceExorbitant = priceExorbitant; }
    public Long getListPriceEvenMoreExorbitant() { return listPriceEvenMoreExorbitant; }
    public void setListPriceEvenMoreExorbitant(Long listPriceEvenMoreExorbitant) { this.listPriceEvenMoreExorbitant = listPriceEvenMoreExorbitant; }
    public String getVatNote() { return vatNote; }
    public void setVatNote(String vatNote) { this.vatNote = vatNote; }
}
