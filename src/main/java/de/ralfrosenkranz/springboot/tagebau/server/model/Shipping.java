package de.ralfrosenkranz.springboot.tagebau.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shipping")
public class Shipping {

    @Id
    @Column(name = "product_id", length = 32)
    private String productId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "shipping_cost_eur", nullable = false)
    private Integer shippingCostEur;

    @Column(name = "lead_time_days", nullable = false)
    private Integer leadTimeDays;

    @Column(name = "incoterms_suggestion", length = 64)
    private String incotermsSuggestion;

    @Column(name = "notes")
    private String notes;

    public String getProductId() { return productId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getShippingCostEur() { return shippingCostEur; }
    public void setShippingCostEur(Integer shippingCostEur) { this.shippingCostEur = shippingCostEur; }
    public Integer getLeadTimeDays() { return leadTimeDays; }
    public void setLeadTimeDays(Integer leadTimeDays) { this.leadTimeDays = leadTimeDays; }
    public String getIncotermsSuggestion() { return incotermsSuggestion; }
    public void setIncotermsSuggestion(String incotermsSuggestion) { this.incotermsSuggestion = incotermsSuggestion; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
