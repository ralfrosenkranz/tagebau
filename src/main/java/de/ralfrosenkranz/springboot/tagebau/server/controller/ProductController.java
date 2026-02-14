package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.*;
import de.ralfrosenkranz.springboot.tagebau.server.model.MediaImage;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import de.ralfrosenkranz.springboot.tagebau.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailDTO> getProduct(@PathVariable("productId") String productId) {
        Product product = productService.getProductByTolerantProductId(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(product.getId());
        dto.setSku(product.getSku());
        dto.setCategoryId(product.getCategoryId());
        dto.setCategoryName(product.getCategoryName());
        dto.setTechnicalName(product.getTechnicalName());
        dto.setNickname(product.getNickname());
        dto.setCondition(product.getCondition());
        dto.setShortDescription(product.getShortDescription());
        dto.setLongDescriptionMarkdown(product.getLongDescriptionMarkdown());

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

        if (product.getShipping() != null) {
            ProductDetailDTO.Shipping s = new ProductDetailDTO.Shipping();
            s.setShippingCostEur(product.getShipping().getShippingCostEur());
            s.setLeadTimeDays(product.getShipping().getLeadTimeDays());
            s.setIncotermsSuggestion(product.getShipping().getIncotermsSuggestion());
            s.setNotes(product.getShipping().getNotes());
            dto.setShipping(s);
        }

        if (product.getSpecs() != null) {
            ProductDetailDTO.ProductSpecs sp = new ProductDetailDTO.ProductSpecs();
            sp.setMachineType(product.getSpecs().getMachineType());
            sp.setOperatingWeightT(product.getSpecs().getOperatingWeightT());
            sp.setBucketCapacityM3(product.getSpecs().getBucketCapacityM3());
            sp.setEnginePowerKw(product.getSpecs().getEnginePowerKw());
            sp.setHoursUsed(product.getSpecs().getHoursUsed());
            sp.setBoomLengthM(product.getSpecs().getBoomLengthM());
            sp.setPayloadT(product.getSpecs().getPayloadT());
            sp.setTireSize(product.getSpecs().getTireSize());
            sp.setThroughputTph(product.getSpecs().getThroughputTph());
            sp.setBeltWidthMm(product.getSpecs().getBeltWidthMm());
            sp.setWheelDiameterM(product.getSpecs().getWheelDiameterM());
            sp.setBucketCount(product.getSpecs().getBucketCount());
            sp.setBladeCapacityM3(product.getSpecs().getBladeCapacityM3());
            sp.setHoleDiameterMm(product.getSpecs().getHoleDiameterMm());
            sp.setMaxHoleDepthM(product.getSpecs().getMaxHoleDepthM());
            dto.setSpecs(sp);
        }

        ProductDetailDTO.Media media = new ProductDetailDTO.Media();
        List<MediaImageDTO> images = (product.getMedia() != null && product.getMedia().getImages() != null)
                ? product.getMedia().getImages().stream().map(ProductController::toMediaImageDTO).collect(Collectors.toList())
                : List.of();
        media.setImages(images);
        dto.setMedia(media);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/products/{productId}/media/images")
    public ResponseEntity<List<MediaImageDTO>> listProductImages(
            @PathVariable("productId") String productId,
            @RequestParam(name = "role", required = false) String role
    ) {
        Product product = productService.getProductByTolerantProductId(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        List<MediaImage> imgs = (product.getMedia() != null && product.getMedia().getImages() != null)
                ? product.getMedia().getImages()
                : List.of();

        List<MediaImageDTO> dto = imgs.stream()
                .filter(mi -> role == null || role.isBlank() || (mi.getRole() != null && role.equalsIgnoreCase(mi.getRole())))
                .map(ProductController::toMediaImageDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/products/{productId}/related")
    public ResponseEntity<List<ProductCardDTO>> listRelatedProducts(
            @PathVariable("productId") String productId,
            @RequestParam(name = "limit", defaultValue = "6") int limit
    ) {
        // TODO: Ähnlichkeitslogik (z.B. gleiche Kategorie, ähnliche Specs) – aktuell über Service
        List<Product> related = productService.getRelatedProductsByProductId(productId, limit);
        List<ProductCardDTO> dto = related.stream().map(CatalogController::toProductCard).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/inquiries")
    public ResponseEntity<InquiryResponseDTO> createInquiry(@RequestBody InquiryRequestDTO body) {
        // TODO: Anfrage speichern / Mail schicken / Workflow starten
        InquiryResponseDTO resp = new InquiryResponseDTO();
        resp.setId(UUID.randomUUID().toString());
        resp.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.status(201).body(resp);
    }

    private static MediaImageDTO toMediaImageDTO(MediaImage mi) {
        MediaImageDTO dto = new MediaImageDTO();
        dto.setId(mi.getId());
        dto.setRole(mi.getRole());
        dto.setLabel(mi.getLabel());
        dto.setFile(mi.getFile());
        dto.setThumbnailFile(mi.getThumbnailFile());
        dto.setGenerationPrompt(mi.getGenerationPrompt());
        return dto;
    }
}
