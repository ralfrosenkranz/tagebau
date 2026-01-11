package de.ralfrosenkranz.springboot.tagebau.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "media_images")
public class MediaImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "media_id", nullable = false)
    private ProductMedia media;

    @Column(nullable = false, length = 32)
    private String role;

    @Column(nullable = false, length = 255)
    private String label;

    @Column(nullable = false, length = 512)
    private String file;

    @Column(name = "thumbnail_file", nullable = false, length = 512)
    private String thumbnailFile;

    @Column(name = "generation_prompt")
    private String generationPrompt;

    public Long getId() { return id; }
    public ProductMedia getMedia() { return media; }
    public void setMedia(ProductMedia media) { this.media = media; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getFile() { return file; }
    public void setFile(String file) { this.file = file; }
    public String getThumbnailFile() { return thumbnailFile; }
    public void setThumbnailFile(String thumbnailFile) { this.thumbnailFile = thumbnailFile; }
    public String getGenerationPrompt() { return generationPrompt; }
    public void setGenerationPrompt(String generationPrompt) { this.generationPrompt = generationPrompt; }
}
