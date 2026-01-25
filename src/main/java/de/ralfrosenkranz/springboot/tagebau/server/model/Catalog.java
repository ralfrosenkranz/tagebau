package de.ralfrosenkranz.springboot.tagebau.server.model;

import de.ralfrosenkranz.springboot.tagebau.server.repository.ProductRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "catalogs")
public class Catalog {

    @Id
    private Long id = 1L; // single-root catalog

    @Column(name = "schema_version", nullable = false, length = 32)
    private String schemaVersion;

    @Column(name = "generated_at", nullable = false)
    private Instant generatedAt;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

//    public List<Product> getProductsByCategoryId(String categoryId) {
//        List<Product> result;
//        if (categoryId != null) {
//            result = products.stream()
//                    .filter(product -> product.getCategoryId().equals(categoryId))
//                    .collect(Collectors.toList());
//        } else {
//            result = Collections.emptyList();
//        }
//        return result;
//    }
//
//    public Product getProductByProductId(String productId) {
//        Product result = null;
//
//        try {
//            result = products.stream()
//                    .filter(product -> product.getId().equals(productId))
//                    .findFirst()
//                    .orElse(null);
//        } catch (Exception e) {
//
//        }
//
//
//        return result;
//    }
}
