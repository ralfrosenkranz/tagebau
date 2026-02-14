package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.CategoryViewDTO;
import de.ralfrosenkranz.springboot.tagebau.server.model.Category;
import de.ralfrosenkranz.springboot.tagebau.server.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryViewDTO>> listCategories() {
        // TODO: Categories aus DB/Service laden (aktuell: aus Catalog)
        List<Category> categories = catalogService.getCatalog().getCategories();
        List<CategoryViewDTO> dto = categories.stream().map(c -> {
            CategoryViewDTO d = new CategoryViewDTO();
            d.setId(c.getId());
            d.setName(c.getName());
            return d;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}
