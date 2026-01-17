package de.ralfrosenkranz.springboot.tagebau.server.repository;

import de.ralfrosenkranz.springboot.tagebau.server.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Category findByName(String name);
}
