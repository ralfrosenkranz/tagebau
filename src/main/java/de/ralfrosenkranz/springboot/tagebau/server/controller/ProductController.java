package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.InquiryRequestDTO;
import de.ralfrosenkranz.springboot.tagebau.server.model.MediaImage;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@SessionScope
public class ProductController {

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable("productId") String productId) {
        // TODO: Produkt inkl. OneToOne Relationen laden (Specs/Pricing/Inventory/Shipping/Media)
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/{productId}/media/images")
    public ResponseEntity<List<MediaImage>> listProductImages(@PathVariable("productId") String productId) {
        // TODO: MediaImages aus ProductMedia laden
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/products/{productId}/related")
    public ResponseEntity<List<Product>> listRelatedProducts(
            @PathVariable("productId") String productId,
            @RequestParam(name = "limit", defaultValue = "6") int limit
    ) {
        // TODO: Related-Logik (gleiche Kategorie / ähnliche Specs)
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping("/inquiries")
    public ResponseEntity<Void> createInquiry(@RequestBody InquiryRequestDTO body) {
        // TODO: Anfrage speichern / Mail schicken
        return ResponseEntity.accepted().build();
    }
}
