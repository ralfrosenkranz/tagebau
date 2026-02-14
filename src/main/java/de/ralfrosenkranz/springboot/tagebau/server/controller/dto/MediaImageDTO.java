package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

/**
 * OpenAPI: openapi-product.yaml#/components/schemas/MediaImage
 */
public class MediaImageDTO {
    private Long id;
    private String role;
    private String label;
    private String file;
    private String thumbnailFile;
    private String generationPrompt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
