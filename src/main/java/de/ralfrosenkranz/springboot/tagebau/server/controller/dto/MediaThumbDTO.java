package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

/**
 * OpenAPI: openapi-catalog.yaml#/components/schemas/MediaThumb
 * (Auch in openapi-landing.yaml verwendet.)
 */
public class MediaThumbDTO {
    private String thumbnailFile;

    public MediaThumbDTO() {}
    public MediaThumbDTO(String thumbnailFile) { this.thumbnailFile = thumbnailFile; }

    public String getThumbnailFile() { return thumbnailFile; }
    public void setThumbnailFile(String thumbnailFile) { this.thumbnailFile = thumbnailFile; }
}
