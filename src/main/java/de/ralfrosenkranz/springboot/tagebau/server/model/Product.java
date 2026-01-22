package de.ralfrosenkranz.springboot.tagebau.server.model;

import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(length = 32)
    private String id;

    @Column(nullable = false, length = 64)
    private String sku;

    @Column(name = "category_id", nullable = false, length = 32)
    private String categoryId;

    @Column(name = "category_name", nullable = false, length = 255)
    private String categoryName;

    @Column(name = "technical_name", nullable = false, length = 255)
    private String technicalName;

    @Column(nullable = false, length = 255)
    private String nickname;

    @Column(nullable = false, length = 64)
    private String condition;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "long_description_markdown")
    private String longDescriptionMarkdown;

    @ManyToOne(optional = false)
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductSpecs specs;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pricing pricing;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory inventory;

    //@OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    //private Shipping shipping;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductMedia media;

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
    public Catalog getCatalog() { return catalog; }
    public void setCatalog(Catalog catalog) { this.catalog = catalog; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public ProductSpecs getSpecs() { return specs; }
    public void setSpecs(ProductSpecs specs) { this.specs = specs; }
    public Pricing getPricing() { return pricing; }
    public void setPricing(Pricing pricing) { this.pricing = pricing; }
    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }
    //public Shipping getShipping() { return shipping; }
    //public void setShipping(Shipping shipping) { this.shipping = shipping; }
    
public Object getShipping() { return null; }
public void setShipping(Object shipping) {  }

    public ProductMedia getMedia() { return media; }
    public void setMedia(ProductMedia media) { this.media = media; }
}
