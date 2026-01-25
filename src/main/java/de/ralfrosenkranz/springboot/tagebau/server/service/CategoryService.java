package de.ralfrosenkranz.springboot.tagebau.server.service;

import de.ralfrosenkranz.springboot.tagebau.Tagebau;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private Tagebau tagebau;

    public CategoryService(Tagebau tagebau) {
        this.tagebau = tagebau;
    }
}
