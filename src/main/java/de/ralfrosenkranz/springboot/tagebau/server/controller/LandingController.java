package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.LandingResponseDTO;
import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.PagedProductCardDTO;
import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.ProductCardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LandingController {

    @GetMapping("/landing")
    public ResponseEntity<LandingResponseDTO> getLanding(
            @RequestParam(name = "topLimit", defaultValue = "8") int topLimit
    ) {
        // TODO: mit Services/Repositories verbinden (Hero/Highlights/Topseller/Kategorien dynamisch liefern)
        LandingResponseDTO resp = new LandingResponseDTO();

        LandingResponseDTO.Hero hero = new LandingResponseDTO.Hero();
        hero.setKicker("Schwere Maschinen • Tagebau • 24/7 Ersatzteile");
        hero.setTitle("Bagger, Muldenkipper & Bohrgeräte für den Tagebau");
        hero.setText("Robuste Technik. Hohe Nutzlast. Einsatzbereit für extreme Bedingungen.");
        hero.setImage(null);

        resp.setHero(hero);
        resp.setHighlights(Collections.emptyList());
        resp.setTopProducts(Collections.emptyList());
        resp.setCategories(Collections.emptyList());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/products/top")
    public ResponseEntity<List<ProductCardDTO>> listTopProducts(
            @RequestParam(name = "limit", defaultValue = "8") int limit
    ) {
        // TODO: Top-Produkte ermitteln (z.B. nach Verkauf/Ranking)
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/products/search")
    public ResponseEntity<PagedProductCardDTO> searchProducts(
            @RequestParam(name = "q") String q,
            @RequestParam(name = "categoryId", required = false) String categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sort", required = false) String sort
    ) {
        // TODO: Suche implementieren (q/categoryId/sort) + Paging (page/size)
        PagedProductCardDTO resp = new PagedProductCardDTO();
        resp.setItems(Collections.emptyList());
        resp.setPage(page);
        resp.setSize(size);
        resp.setTotalItems(0);
        return ResponseEntity.ok(resp);
    }
}
