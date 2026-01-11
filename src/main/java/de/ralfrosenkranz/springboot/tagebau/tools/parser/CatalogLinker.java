package de.ralfrosenkranz.springboot.tagebau.tools.parser;

import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.server.model.Category;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;

import java.util.HashMap;
import java.util.Map;

public class CatalogLinker {

    public void link(Catalog catalog) {
        Map<String, Category> byId = new HashMap<>();
        for (Category c : catalog.getCategories()) {
            byId.put(c.getId(), c);
        }
        for (Product p : catalog.getProducts()) {
            Category c = byId.get(p.getCategoryId());
            if (c == null) {
                throw new IllegalStateException("Product " + p.getId() + " references missing category_id=" + p.getCategoryId());
            }
            p.setCategory(c);
            c.getProducts().add(p);
        }
    }
}
