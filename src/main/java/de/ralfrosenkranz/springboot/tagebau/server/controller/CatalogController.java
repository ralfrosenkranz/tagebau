package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.*;
import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import de.ralfrosenkranz.springboot.tagebau.server.service.CatalogService;
import de.ralfrosenkranz.springboot.tagebau.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @Autowired
    ProductService productService;

    @GetMapping("/catalog")
    public ResponseEntity<CatalogInfoDTO> getCatalog() {
        Catalog catalog = catalogService.getCatalog();

        CatalogInfoDTO info = new CatalogInfoDTO();
        info.setId(catalog.getId());
        info.setSchemaVersion(catalog.getSchemaVersion());
        info.setGeneratedAt(catalog.getGeneratedAt() != null
                ? OffsetDateTime.ofInstant(catalog.getGeneratedAt(), ZoneOffset.UTC)
                : OffsetDateTime.now(ZoneOffset.UTC));
        info.setNote(catalog.getNote());
        return ResponseEntity.ok(info);
    }

    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<PagedProductCardDTO> listProductsByCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(name = "condition", required = false) String condition,
            @RequestParam(name = "availability", required = false) String availability,
            @RequestParam(name = "minPrice", required = false) Long minPrice,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "24") int size,
            @RequestParam(name = "sort", required = false) String sort
    ) {
        // TODO: echte DB-Query (Filter/Paging/Sort) statt In-Memory
        List<Product> all = productService.getProductsByCategoryId(categoryId);
        if (all == null) {
            return ResponseEntity.notFound().build();
        }

        List<Product> filtered = all.stream()
                .filter(p -> condition == null || condition.isBlank() || condition.equalsIgnoreCase(p.getCondition()))
                .filter(p -> availability == null || availability.isBlank() ||
                        (p.getInventory() != null && availability.equalsIgnoreCase(p.getInventory().getAvailability())))
                .filter(p -> minPrice == null || (p.getPricing() != null && p.getPricing().getPriceExorbitant() != null && p.getPricing().getPriceExorbitant() >= minPrice))
                .filter(p -> maxPrice == null || (p.getPricing() != null && p.getPricing().getPriceExorbitant() != null && p.getPricing().getPriceExorbitant() <= maxPrice))
                .collect(Collectors.toList());

        if (sort != null && !sort.isBlank()) {
            String s = sort.trim().toLowerCase(Locale.ROOT);
            if (s.contains("pricing.priceexorbitant")) {
                filtered.sort(Comparator.comparingLong(p -> p.getPricing() != null && p.getPricing().getPriceExorbitant() != null ? p.getPricing().getPriceExorbitant() : Long.MAX_VALUE));
            } else if (s.contains("nickname")) {
                filtered.sort(Comparator.comparing(p -> p.getNickname() != null ? p.getNickname() : ""));
            }
            // TODO: weitere Sortierungen und Sort-Richtung abbilden
        }

        long total = filtered.size();
        int from = Math.max(0, page * size);
        int to = Math.min(filtered.size(), from + Math.max(1, size));
        List<Product> slice = (from >= to) ? List.of() : filtered.subList(from, to);

        List<ProductCardDTO> items = slice.stream().map(CatalogController::toProductCard).collect(Collectors.toList());

        PagedProductCardDTO resp = new PagedProductCardDTO();
        resp.setItems(items);
        resp.setPage(page);
        resp.setSize(size);
        resp.setTotalItems(total);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/products/{productId}/thumbnail")
    public ResponseEntity<MediaThumbDTO> getProductThumbnail(@PathVariable("productId") String productId) {
        Product product = productService.getProductByTolerantProductId(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        String thumb = (product.getMedia() != null) ? product.getMedia().getFirstThumbnailFile() : null;
        MediaThumbDTO dto = new MediaThumbDTO(thumb);
        return ResponseEntity.ok(dto);
    }

    static ProductCardDTO toProductCard(Product product) {
        ProductCardDTO dto = new ProductCardDTO();
        dto.setId(product.getId());
        dto.setSku(product.getSku());
        dto.setCategoryId(product.getCategoryId());
        dto.setCategoryName(product.getCategoryName());
        dto.setTechnicalName(product.getTechnicalName());
        dto.setNickname(product.getNickname());
        dto.setCondition(product.getCondition());
        dto.setShortDescription(product.getShortDescription());

        if (product.getPricing() != null) {
            ProductCardDTO.PricingSummary p = new ProductCardDTO.PricingSummary();
            p.setCurrency(product.getPricing().getCurrency());
            p.setPriceExorbitant(product.getPricing().getPriceExorbitant());
            p.setListPriceEvenMoreExorbitant(product.getPricing().getListPriceEvenMoreExorbitant());
            p.setVatNote(product.getPricing().getVatNote());
            dto.setPricing(p);
        }

        if (product.getInventory() != null) {
            ProductCardDTO.InventorySummary inv = new ProductCardDTO.InventorySummary();
            inv.setStockQty(product.getInventory().getStockQty());
            inv.setAvailability(product.getInventory().getAvailability());
            dto.setInventory(inv);
        }

        String thumb = (product.getMedia() != null) ? product.getMedia().getFirstThumbnailFile() : null;
        dto.setThumbnail(new MediaThumbDTO(thumb));

        return dto;
    }
}
