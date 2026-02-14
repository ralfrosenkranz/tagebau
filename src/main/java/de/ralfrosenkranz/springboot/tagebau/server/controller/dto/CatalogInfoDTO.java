package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

import java.time.OffsetDateTime;

/**
 * OpenAPI: openapi-catalog.yaml#/components/schemas/CatalogInfo
 */
public class CatalogInfoDTO {
    private Long id;
    private String schemaVersion;
    private OffsetDateTime generatedAt;
    private String note;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSchemaVersion() { return schemaVersion; }
    public void setSchemaVersion(String schemaVersion) { this.schemaVersion = schemaVersion; }

    public OffsetDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(OffsetDateTime generatedAt) { this.generatedAt = generatedAt; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
