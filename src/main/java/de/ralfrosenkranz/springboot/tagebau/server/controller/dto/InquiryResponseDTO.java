package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

import java.time.OffsetDateTime;

/**
 * OpenAPI: openapi-product.yaml#/components/schemas/InquiryResponse
 */
public class InquiryResponseDTO {
    private String id;
    private OffsetDateTime createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
