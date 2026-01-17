package de.ralfrosenkranz.springboot.tagebau.server.controller;

import de.ralfrosenkranz.springboot.tagebau.server.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@SessionScope
public class CategoryController {

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> listCategories() {
        // TODO: Categories aus DB/Service laden
        return ResponseEntity.ok(Collections.emptyList());
    }
}
