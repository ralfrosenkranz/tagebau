package de.ralfrosenkranz.springboot.tagebau.server.repository;

import de.ralfrosenkranz.springboot.tagebau.server.model.DataCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataCatalogRepository extends JpaRepository<DataCatalog, Long> {
    
    List<DataCatalog> findByParent(DataCatalog parent);
    
    List<DataCatalog> findByLevel(Integer level);
    
    List<DataCatalog> findByPathContaining(String path);
}
