package de.ralfrosenkranz.springboot.tagebau.server.service;

import de.ralfrosenkranz.springboot.tagebau.Tagebau;
import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private Tagebau tagebau;

    public ProductService(Tagebau tagebau) {
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

    private final static String PRODUCT_ID_PATTERN = "prod-0000";

    private String getValidProductId(String tolerantProductId) {

        StringBuilder sb = new StringBuilder();
        sb.append(PRODUCT_ID_PATTERN);

        if (tolerantProductId != null) {

            int iPat = PRODUCT_ID_PATTERN.length() - 1;
            int iProd = tolerantProductId.length() - 1;

            for (int n = 0; (n < PRODUCT_ID_PATTERN.length()) && (iProd >= 0); n++, iPat--, iProd--) {
                char c = tolerantProductId.charAt(iProd);
                sb.setCharAt(iPat, c);
            }
        }

        return sb.toString();
    }

    public Product getProductByTolerantProductId(String tolerantProductId) {
        String productId = getValidProductId(tolerantProductId);
        Product product = getCatalog().getProductByProductId(productId);
        return product;
    }
}
