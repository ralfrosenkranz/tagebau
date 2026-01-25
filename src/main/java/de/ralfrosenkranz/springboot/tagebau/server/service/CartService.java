package de.ralfrosenkranz.springboot.tagebau.server.service;

import de.ralfrosenkranz.springboot.tagebau.Tagebau;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private Tagebau tagebau;

    public CartService(Tagebau tagebau) {
        this.tagebau = tagebau;
    }

}
