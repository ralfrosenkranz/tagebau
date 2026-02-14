package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

import java.util.List;

/**
 * OpenAPI: openapi-cart.yaml#/components/schemas/PagedOrder
 */
public class PagedOrderDTO {
    private List<OrderDTO> items;
    private int page;
    private int size;
    private long totalItems;

    public List<OrderDTO> getItems() { return items; }
    public void setItems(List<OrderDTO> items) { this.items = items; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }
}
