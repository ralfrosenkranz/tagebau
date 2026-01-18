package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.LandingResponseDTO;
import de.ralfrosenkranz.springboot.tagebau.server.controller.dto.ProductCardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@SessionScope
public class LandingController {

    @GetMapping("/landing")
    public ResponseEntity<LandingResponseDTO> getLanding(
            @RequestParam(name = "topLimit", defaultValue = "8") int topLimit
    ) {
        // TODO: mit Services/Repositories verbinden
        LandingResponseDTO resp = new LandingResponseDTO();
        LandingResponseDTO.Hero hero = new LandingResponseDTO.Hero();
        hero.setKicker("HURTZ Schwere Maschinen • Tagebau • 24/7 Ersatzteile");
        hero.setTitle("WURTZ Bagger, Muldenkipper & Bohrgeräte für den Tagebau");
        hero.setText("ZUTZEL Robuste Technik. Hohe Nutzlast. Einsatzbereit für extreme Bedingungen.");
        resp.setHero(hero);
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
    public ResponseEntity<List<ProductCardDTO>> searchProducts(
            @RequestParam(name = "q") String q,
            @RequestParam(name = "limit", defaultValue = "20") int limit
    ) {
        // TODO: Suche implementieren
        return ResponseEntity.ok(Collections.emptyList());
    }
}
