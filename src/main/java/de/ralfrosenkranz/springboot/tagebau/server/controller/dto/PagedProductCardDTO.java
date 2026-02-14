package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

import java.util.List;

/**
 * OpenAPI: openapi-catalog.yaml#/components/schemas/PagedProductCard
 * (Auch in openapi-landing.yaml verwendet.)
 */
public class PagedProductCardDTO {
    private List<ProductCardDTO> items;
    private int page;
    private int size;
    private long totalItems;

    public List<ProductCardDTO> getItems() { return items; }
    public void setItems(List<ProductCardDTO> items) { this.items = items; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }
}
