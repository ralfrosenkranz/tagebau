package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.InquiryRequestDTO;
import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.MediaImageDTO;
import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.ProductCardDTO;
import de.ralfrosenkranz.springboot.tagebau.server.model.MediaImage;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import de.ralfrosenkranz.springboot.tagebau.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@SessionScope
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductCardDTO> getProduct(@PathVariable("productId") String productId) {
        // TODO: Produkt inkl. OneToOne Relationen laden (Specs/Pricing/Inventory/Shipping/Media)

        Product product = productService.getProductByTolerantProductId (productId);

        if (product != null) {
            ProductCardDTO resp = new ProductCardDTO();

            resp.setId(product.getId());
            resp.setTechnicalName(product.getTechnicalName());
            resp.setNickname(product.getNickname());

            // Set price information
            ProductCardDTO.PriceView priceView = new ProductCardDTO.PriceView();
            priceView.setCurrency("EUR");
            priceView.setAmount(product.getPricing().getPriceExorbitant().toString());
            resp.setPriceView(priceView);

            // Set thumbnail URL
            resp.setThumbnailUrl(product.getMedia().getFirstThumbnailFile());

            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products/{productId}/media/images")
    public ResponseEntity<List<MediaImageDTO>> listProductImages(@PathVariable("productId") String productId) {
        // TODO: MediaImages aus ProductMedia laden

        Product product = productService.getProductByTolerantProductId (productId);
        if (product != null) {
            List<MediaImage> mediaImageList = product.getMedia().getImages();

            List<MediaImageDTO> mediaImageDTOList = mediaImageList.stream().map(mediaImage -> {
                MediaImageDTO dto = new MediaImageDTO();
                dto.setFile(mediaImage.getFile());
                dto.setThumbnailFile(mediaImage.getThumbnailFile());
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(mediaImageDTOList);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
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
