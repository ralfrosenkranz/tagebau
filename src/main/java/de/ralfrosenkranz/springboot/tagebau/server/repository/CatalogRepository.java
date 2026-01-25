package de.ralfrosenkranz.springboot.tagebau.server.repository;

import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
}
