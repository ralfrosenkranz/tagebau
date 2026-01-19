package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.CatalogInfoDTO;
import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.PagedProductCardDTO;
import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.ProductCardDTO;
import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.server.model.MediaImage;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import de.ralfrosenkranz.springboot.tagebau.server.service.CatalogService;
import de.ralfrosenkranz.springboot.tagebau.server.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@SessionScope
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @Autowired
    ImageService imageService;

    @GetMapping("/catalog")
    public ResponseEntity<CatalogInfoDTO> getCatalog() {
        // TODO: z.B. aktuellen Catalog bestimmen (Default/Active)

        Catalog catalog = catalogService.getCatalog();

        CatalogInfoDTO info = new CatalogInfoDTO();
        info.setId("default");
        info.setName("Tagebau Heavy Machines");
        info.setDescription("Demo-Katalog mit " + catalog.getCategories().size() + " Kategorien und " + catalog.getProducts().size() + " Produkten.");
        return ResponseEntity.ok(info);
    }

    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<PagedProductCardDTO> listProductsByCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "sort", defaultValue = "popularity") String sort,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        // TODO: Filter/Sort/Pagination implementieren

        Catalog catalog = catalogService.getCatalog();
        List<Product> productList = catalog.getProductsByCategoryId (categoryId);

        List<ProductCardDTO> contentList = productList.stream()
                .map(product -> {
                    ProductCardDTO dto = new ProductCardDTO();
                    dto.setId(product.getId());
                    dto.setTechnicalName(product.getTechnicalName());
                    dto.setNickname(product.getNickname());
                    ProductCardDTO.PricingDTO pricingDTO = new ProductCardDTO.PricingDTO();
                    pricingDTO.setCurrency("EUR");
                    pricingDTO.setPriceExorbitant(product.getPricing().getPriceExorbitant().toString());
                    dto.setPricing(pricingDTO);
                    dto.setThumbnailUrl(product.getMedia().getFirstThumbnailFile());
                    // Set other properties as needed
                    return dto;
                })
                .collect(Collectors.toList());

        PagedProductCardDTO resp = new PagedProductCardDTO();
        resp.setContent(contentList);
        resp.setPage(page);
        resp.setSize(size);
        resp.setTotalElements(productList.size ());
        resp.setTotalPages(productList.size () > 0 ? 1 : 0);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/products/{productId}/thumbnail")
    public ResponseEntity<ByteArrayResource> getProductThumbnail(
            @PathVariable("productId") String productId
    ) {
        Catalog catalog = catalogService.getCatalog();
        Product product = catalog.getProductByProductId (productId);
        ByteArrayResource bytearrayJpegResource = null;

        if (product != null) {
            Optional<MediaImage> imageOptional = product.getMedia().getImages().stream().findFirst();
            if (imageOptional.isPresent()) {
                String thumbnailFile = imageOptional.get().getThumbnailFile();
                bytearrayJpegResource = imageService.getImageResourceAsJpegByteArray(thumbnailFile);
            }
        }

        if (bytearrayJpegResource == null) {
           bytearrayJpegResource = imageService.getMissingImageResourceAsJpegByteArray();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .cacheControl(CacheControl.noCache())
                .body(bytearrayJpegResource);
    }
}
