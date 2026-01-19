package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.InquiryRequestDTO;
import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.MediaImageDTO;
import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.ProductCardDTO;
import de.ralfrosenkranz.springboot.tagebau.server.model.MediaImage;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import de.ralfrosenkranz.springboot.tagebau.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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
            ProductCardDTO dto = getProductCardDTO(product);

            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @NonNull
    private static ProductCardDTO getProductCardDTO(Product product) {
        ProductCardDTO dto = new ProductCardDTO();

        // Set basic product information
        dto.setId(product.getId());
        dto.setTechnicalName(product.getTechnicalName());
        dto.setNickname(product.getNickname());
        dto.setCondition(product.getCondition());
        dto.setShortDescription(product.getShortDescription());
        dto.setLongDescriptionMarkdown(product.getLongDescriptionMarkdown());
        dto.setCategory(product.getCategoryName());

        // Set price information
        if (product.getPricing() != null) {
            ProductCardDTO.PricingDTO pricingDTO = new ProductCardDTO.PricingDTO();
            pricingDTO.setCurrency(product.getPricing().getCurrency());
            pricingDTO.setPriceExorbitant(product.getPricing().getPriceExorbitant().toString());
            dto.setPricing(pricingDTO);
        }

        // Set thumbnail URL
        dto.setThumbnailUrl(product.getMedia().getFirstThumbnailFile());

        // Set product specifications
        if (product.getSpecs() != null) {
            ProductCardDTO.ProductSpecsDTO specsDTO = new ProductCardDTO.ProductSpecsDTO();
            specsDTO.setMachineType(product.getSpecs().getMachineType());
            specsDTO.setOperatingWeightT(product.getSpecs().getOperatingWeightT());
            specsDTO.setBucketCapacityM3(product.getSpecs().getBucketCapacityM3());
            specsDTO.setEnginePowerKw(product.getSpecs().getEnginePowerKw());
            specsDTO.setHoursUsed(product.getSpecs().getHoursUsed());
            specsDTO.setBoomLengthM(product.getSpecs().getBoomLengthM());
            specsDTO.setPayloadT(product.getSpecs().getPayloadT());
            specsDTO.setTireSize(product.getSpecs().getTireSize());
            specsDTO.setThroughputTph(product.getSpecs().getThroughputTph());
            specsDTO.setBeltWidthMm(product.getSpecs().getBeltWidthMm());
            specsDTO.setWheelDiameterM(product.getSpecs().getWheelDiameterM());
            specsDTO.setBucketCount(product.getSpecs().getBucketCount());
            specsDTO.setBladeCapacityM3(product.getSpecs().getBladeCapacityM3());
            specsDTO.setHoleDiameterMm(product.getSpecs().getHoleDiameterMm());
            specsDTO.setMaxHoleDepthM(product.getSpecs().getMaxHoleDepthM());
            dto.setSpecs(specsDTO);
        }

        // Set inventory information
        if (product.getInventory() != null) {
            ProductCardDTO.InventoryDTO inventoryDTO = new ProductCardDTO.InventoryDTO();
            //inventoryDTO.setStock(product.getInventory().getStock());
            inventoryDTO.setAvailability(product.getInventory().getAvailability());
            dto.setInventory(inventoryDTO);
        }

        // Set shipping information
        if (product.getShipping() != null) {
            ProductCardDTO.ShippingDTO shippingDTO = new ProductCardDTO.ShippingDTO();
            //shippingDTO.setShippingTime(product.getShipping().getShippingTime());
            //shippingDTO.setShippingCost(product.getShipping().getShippingCost());
            //shippingDTO.setFreeShipping(product.getShipping().getFreeShipping());
            dto.setShipping(shippingDTO);
        }

        // Set media images
        if (product.getMedia() != null && product.getMedia().getImages() != null) {
            List<ProductCardDTO.MediaImageDTO> mediaImageDTOList = product.getMedia().getImages().stream()
                    .map(mediaImage -> {
                        ProductCardDTO.MediaImageDTO mediaImageDTO = new ProductCardDTO.MediaImageDTO();
                        //productCardDTO.setId(mediaImage.getId());
                        mediaImageDTO.setUrl(mediaImage.getFile());
                        mediaImageDTO.setRole(mediaImage.getRole());
                        //productCardDTO.setAltText(mediaImage.getAltText());
                        return mediaImageDTO;
                    })
                    .collect(Collectors.toList());
            dto.setMediaImages(mediaImageDTOList);
        }
        return dto;
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
    public ResponseEntity<List<ProductCardDTO>> listRelatedProducts(
            @PathVariable("productId") String productId,
            @RequestParam(name = "limit", defaultValue = "6") int limit
    ) {
        // TODO: Related-Logik (gleiche Kategorie / ähnliche Specs)

        List <Product> relatedProductList = productService.getRelatedProductsByProductId (productId, limit);

        List<ProductCardDTO> relatedProductDTOList = relatedProductList.stream().
                map(product -> {
            ProductCardDTO dto = getProductCardDTO(product);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(relatedProductDTOList);
    }

    @PostMapping("/inquiries")
    public ResponseEntity<Void> createInquiry(@RequestBody InquiryRequestDTO body) {
        // TODO: Anfrage speichern / Mail schicken
        return ResponseEntity.accepted().build();
    }
}
