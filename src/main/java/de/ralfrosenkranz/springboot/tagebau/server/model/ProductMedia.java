package de.ralfrosenkranz.springboot.tagebau.server.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_media")
public class ProductMedia {

    @Id
    @Column(name = "product_id", length = 32)
    private String productId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaImage> images = new ArrayList<>();

    public String getProductId() { return productId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public List<MediaImage> getImages() { return images; }
    public void setImages(List<MediaImage> images) { this.images = images; }
}
