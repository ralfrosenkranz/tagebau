package de.ralfrosenkranz.springboot.tagebau.tools.catalog;

import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.server.model.MediaImage;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;

public class CatalogValidator {

    public void validate(Catalog catalog) {
        if (catalog.getCategories() == null || catalog.getCategories().isEmpty()) {
            throw new IllegalStateException("catalog.categories is empty");
        }
        if (catalog.getProducts() == null || catalog.getProducts().isEmpty()) {
            throw new IllegalStateException("catalog.products is empty");
        }
        for (Product p : catalog.getProducts()) {
            if (p.getMedia() != null) {
                for (MediaImage img : p.getMedia().getImages()) {
                    if (img.getFile() == null || img.getFile().isBlank()) {
                        throw new IllegalStateException("Product " + p.getId() + " has media image without file");
                    }
                    if (img.getThumbnailFile() == null || img.getThumbnailFile().isBlank()) {
                        throw new IllegalStateException("Product " + p.getId() + " has media image without thumbnail_file");
                    }
                }
            }
        }
    }
}
