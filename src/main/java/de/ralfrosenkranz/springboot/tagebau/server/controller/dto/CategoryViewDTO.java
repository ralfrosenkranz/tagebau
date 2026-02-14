package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

/**
 * OpenAPI: openapi-landing.yaml#/components/schemas/Category
 * (Auch in openapi-catalog.yaml verwendet.)
 */
public class CategoryViewDTO {
    private String id;
    private String name;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
