package de.ralfrosenkranz.springboot.tagebau.server.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "data_catalog")
public class DataCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private String path;
    private Integer level;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DataCatalog parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<DataCatalog> children;
    
    // Konstruktoren
    public DataCatalog() {}
    
    public DataCatalog(String name, String description, Integer level) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public DataCatalog getParent() { return parent; }
    public void setParent(DataCatalog parent) { this.parent = parent; }
    
    public List<DataCatalog> getChildren() { return children; }
    public void setChildren(List<DataCatalog> children) { this.children = children; }
    
    public void addChild(DataCatalog child) {
        this.children.add(child);
        child.setParent(this);
    }
}
