package de.ralfrosenkranz.springboot.tagebau.server.service;

import de.ralfrosenkranz.springboot.tagebau.Tagebau;
import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import de.ralfrosenkranz.springboot.tagebau.server.repository.CategoryRepository;
import de.ralfrosenkranz.springboot.tagebau.server.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final Tagebau tagebau;
   private final ProductRepository productRepository;

    public ProductService(Tagebau tagebau,
                          ProductRepository productRepository) {

        this.tagebau = tagebau;
        this.productRepository = productRepository;
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

    @Transactional(readOnly = true)
    public Product getProductByTolerantProductId(String tolerantProductId) {
        String productId = getValidProductId(tolerantProductId);
        return getProductByProductId (productId);
    }

    @Transactional(readOnly = true)
    public Product getProductByProductId(String productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()){
            return productOptional.get();
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryId(String categoryId) {
        List<Product> resultList = productRepository.findByCategory_Id(categoryId);
        return resultList;
    }

    @Transactional(readOnly = true)
    public List<Product> getRelatedProductsByProductId(String productId, int limit) {
        List<Product> resultProductList = new ArrayList<>();
        Product product = getProductByTolerantProductId(productId);
        String categoryId = product.getCategoryId();

        //TODO: Eine mehr datenbankbasierte Lösung implementieren ---
        List<Product> productList = getProductsByCategoryId (categoryId);
        if (product != null) {
            productList.stream()
                    .filter(anotherProduct -> !(anotherProduct.getId().equals(product.getId())))
                    .limit(limit)
                    .forEach(resultProduct -> resultProductList.add(resultProduct));
        }

        return resultProductList;
    }



}
