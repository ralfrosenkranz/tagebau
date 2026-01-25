package de.ralfrosenkranz.springboot.tagebau.server.repository;

import de.ralfrosenkranz.springboot.tagebau.server.model.Category;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    Optional<Product> findBySku(String sku);
    
    /**
     * Convenience lookup by the denormalized category name column on Product.
     * (Product.category is a Category entity; comparing it to a String is invalid.)
     */
    List<Product> findByCategoryName(String categoryName);

    /**
     * Lookup by Category entity relationship.
     */
    List<Product> findByCategory(Category category);

    /**
     * Lookup by Category.id via property traversal.
     */
    List<Product> findByCategory_Id(String categoryId);


//    @Query("SELECT p FROM Product p WHERE " +
//            "p.technicalName LIKE %:keyword% OR " +
//            "p.nickname LIKE %:keyword% OR " +
//            "CHARINDEX(:keyword, p.shortDescription) > 0 OR "  +
//            "CHARINDEX(:keyword, p.longDescriptionMarkdown) > 0")
//    List<Product> findByKeyword(@Param("keyword") String keyword);

    //List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    //List<Product> findByBrand(String brand);
}
