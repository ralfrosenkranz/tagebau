package de.ralfrosenkranz.springboot.tagebau.server.service;

import de.ralfrosenkranz.springboot.tagebau.Tagebau;
import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

    private Tagebau tagebau;

    public CatalogService(Tagebau tagebau) {
        this.tagebau = tagebau;
    }

    public Catalog getCatalog() {

        synchronized (tagebau) {
            Catalog c = tagebau.getCatalog();

            if (c != null) {
                return c;
            } else {
                return new Catalog();
            }
        }
    }
}
