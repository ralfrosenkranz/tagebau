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
        Product product = productService.getProductByTolerantProductId(productId);

        if (product != null) {
            ProductCardDTO resp = new ProductCardDTO();

            // Set basic product information
            resp.setId(product.getId());
            resp.setTechnicalName(product.getTechnicalName());
            resp.setNickname(product.getNickname());
            resp.setCondition(product.getCondition());
            //resp.setDescription(product.getDescription());
            resp.setShortDescription(product.getShortDescription());
            resp.setLongDescriptionMarkdown(product.getLongDescriptionMarkdown());
            //resp.setCategory(product.getCategory());
            //resp.setBrand(product.getBrand());
            //resp.setStatus(product.getStatus());
            //resp.setCreatedAt(product.getCreatedAt());
            //resp.setUpdatedAt(product.getUpdatedAt());

            // Set price information
            if (product.getPricing() != null) {
                ProductCardDTO.PricingDTO pricingDTO = new ProductCardDTO.PricingDTO();
                pricingDTO.setCurrency(product.getPricing().getCurrency());
                pricingDTO.setPriceExorbitant(product.getPricing().getPriceExorbitant().toString());
                resp.setPricing(pricingDTO);
            }

            // Set thumbnail URL
            resp.setThumbnailUrl(product.getMedia().getFirstThumbnailFile());

            // Set product specifications
            if (product.getSpecs() != null) {
                ProductCardDTO.ProductSpecsDTO specsDTO = new ProductCardDTO.ProductSpecsDTO();
                //specsDTO.setWeight(product.getSpecs().getWeight());
                //specsDTO.setDimensions(product.getSpecs().getDimensions());
                //specsDTO.setMaterial(product.getSpecs().getMaterial());
                //specsDTO.setColor(product.getSpecs().getColor());
                resp.setProductSpecs(specsDTO);
            }

            // Set inventory information
            if (product.getInventory() != null) {
                ProductCardDTO.InventoryDTO inventoryDTO = new ProductCardDTO.InventoryDTO();
                //inventoryDTO.setStock(product.getInventory().getStock());
                inventoryDTO.setAvailability(product.getInventory().getAvailability());
                resp.setInventory(inventoryDTO);
            }

            // Set shipping information
            if (product.getShipping() != null) {
                ProductCardDTO.ShippingDTO shippingDTO = new ProductCardDTO.ShippingDTO();
                //shippingDTO.setShippingTime(product.getShipping().getShippingTime());
                //shippingDTO.setShippingCost(product.getShipping().getShippingCost());
                //shippingDTO.setFreeShipping(product.getShipping().getFreeShipping());
                resp.setShipping(shippingDTO);
            }

            // Set media images
            if (product.getMedia() != null && product.getMedia().getImages() != null) {
                List<ProductCardDTO.MediaImageDTO> mediaImageDTOList = product.getMedia().getImages().stream()
                        .map(mediaImage -> {
                            ProductCardDTO.MediaImageDTO dto = new ProductCardDTO.MediaImageDTO();
                            //dto.setId(mediaImage.getId());
                            dto.setUrl(mediaImage.getFile());
                            dto.setRole(mediaImage.getRole());
                            //dto.setAltText(mediaImage.getAltText());
                            return dto;
                        })
                        .collect(Collectors.toList());
                resp.setMediaImages(mediaImageDTOList);
            }

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
