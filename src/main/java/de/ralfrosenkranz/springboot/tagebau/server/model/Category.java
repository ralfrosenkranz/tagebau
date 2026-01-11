package de.ralfrosenkranz.springboot.tagebau.server.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(length = 32)
    private String id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Catalog getCatalog() { return catalog; }
    public void setCatalog(Catalog catalog) { this.catalog = catalog; }
    public List<Product> getProducts() { return products; }
}
