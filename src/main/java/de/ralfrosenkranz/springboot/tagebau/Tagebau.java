package de.ralfrosenkranz.springboot.tagebau;

import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.server.model.Category;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;
import de.ralfrosenkranz.springboot.tagebau.server.repository.CatalogRepository;
import de.ralfrosenkranz.springboot.tagebau.tools.catalog.CatalogLinker;
import de.ralfrosenkranz.springboot.tagebau.tools.catalog.CatalogParser;
import de.ralfrosenkranz.springboot.tagebau.tools.catalog.CatalogValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Tagebau extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(Tagebau.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Tagebau.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Tagebau.class, args);
    }

    private Catalog loadLinkedValidated(Path json) throws Exception {
        log.debug(new File(".").getAbsolutePath());
        log.debug(json.toFile().getAbsolutePath());
        Catalog catalog = new CatalogParser().parse(json);
        new CatalogLinker().link(catalog);
        new CatalogValidator().validate(catalog);
        return catalog;
    }

    @Component
    public class ApplicationArgumentsReader {
        private final ApplicationArguments args;

        public ApplicationArgumentsReader(ApplicationArguments args) {
            this.args = args;
        }

        public String[] getArgs() {
            return args.getSourceArgs();
        }
    }

    /**
     * Initialisiert den Demo-Katalog nur dann, wenn die Datenbank nicht erreichbar ist,
     * noch keine passenden Daten enthält oder noch leer ist. Wenn die DB in Ordnung ist,
     * wird der Katalog aus der DB geladen.
     */
    @Component
    public class StartupListener implements ApplicationRunner {

        private static final Logger log = LoggerFactory.getLogger(StartupListener.class);

        @Autowired
        ApplicationArgumentsReader argsReader;

        @Autowired
        DataSource dataSource;

        @Autowired
        CatalogRepository catalogRepository;

        @PersistenceContext
        EntityManager entityManager;

        private final PlatformTransactionManager transactionManager;
        private final TransactionTemplate txTemplate;

        public StartupListener(
                CatalogRepository catalogRepository,
                EntityManager entityManager,
                PlatformTransactionManager transactionManager
        ) {
            this.catalogRepository = catalogRepository;
            this.entityManager = entityManager;
            this.transactionManager = transactionManager;
            this.txTemplate = new TransactionTemplate(transactionManager);
        }

        @Override
        public void run(ApplicationArguments args) {
            boolean initNeeded = shouldInitializeDemoCatalog();

            if (initNeeded) {
                log.warn("DB nicht verfügbar/leer/nicht passend – Demo-Katalog wird initialisiert.");
                initDemoCatalog(argsReader.getArgs());
            } else {
                log.info("DB verfügbar und befüllt – Katalog wird aus der Datenbank geladen.");
                try {
                    catalog = catalogRepository.findById(1L).orElse(null);
                } catch (Exception e) {
                    // Sicherheitsnetz: falls trotz Check beim Laden etwas schiefgeht
                    log.warn("Konnte Katalog nicht aus DB laden – Demo-Katalog wird initialisiert.", e);
                    initDemoCatalog(argsReader.getArgs());
                }
            }
        }

        private boolean shouldInitializeDemoCatalog() {
            // 1) DB-Verbindung testen
            try (Connection c = dataSource.getConnection()) {

                // 2) Sicherstellen, dass wir überhaupt in einer DB/Schema sind
                String dbName = c.getCatalog();
                if (dbName == null || dbName.isBlank()) {
                    return true;
                }

                // 3) Prüfen, ob die (erwarteten) Tabellen vorhanden und befüllt sind
                //    (bei fehlenden Tabellen/Schema wirft SQL eine Exception)
                if (countTableRows(c, "catalogs") == 0) {
                    return true;
                }
                if (countTableRows(c, "categories") == 0) {
                    return true;
                }
                if (countTableRows(c, "products") == 0) {
                    return true;
                }

                // Optional: Root-Catalog (id=1) sollte existieren
                if (!catalogRepository.existsById(1L)) {
                    return true;
                }

                return false;

            } catch (Exception e) {
                log.warn("DB ist nicht zugreifbar – Demo-Katalog wird verwendet.", e);
                return true;
            }
        }


        private long countTableRows(Connection c, String tableName) throws Exception {
            try (var stmt = c.createStatement();
                 var rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
                rs.next();
                return rs.getLong(1);
            }
        }

        private void initDemoCatalog(String[] args) {
            try {
                // Path json = Path.of(args[1]);
                Path json = Path.of("tagebau_demo_katalog.json");
                Catalog demo = loadLinkedValidated(json);

                // In-Memory für die aktuelle Laufzeit verfügbar machen
                catalog = demo;

                // Wenn DB erreichbar ist: Demo-Daten persistieren (inkl. aller durch Parser/Linker gemachten Beziehungen)
                // und als "Single Root" (id=1) in der DB upserten.
                // Damit werden die Änderungen nicht nur in-memory gehalten, sondern landen auch dauerhaft in der DB.
                tryPersistDemoCatalogGraph(demo);

            } catch (Exception e) {
                log.error("ERROR loading Demo-Catalog", e);
            }
        }

        /**
         * Persistiert den Demo-Katalog in die DB.
         *
         * Hintergrund: Das Parsen/Linken erzeugt in-memory Relationen (z.B. Product.category, Category.catalog, Product.specs.product …).
         * Damit diese Änderungen auch in der DB ankommen, wird hier ein "Upsert" des gesamten Katalog-Objektgraphen durchgeführt.
         *
         * Vorgehen:
         * - Wenn kein Root-Catalog existiert: demo direkt speichern (Cascade persist).
         * - Wenn Root-Catalog existiert: bestehenden Katalog laden, Collections leeren (orphanRemoval), neu befüllen, speichern.
         */
        protected void tryPersistDemoCatalogGraph(Catalog demo) {
            try (Connection ignored = dataSource.getConnection()) {

                Catalog saved = txTemplate.execute(status -> upsertSingleRootCatalog(demo));

                if (saved != null) {
                    // Für die laufende App den persistierten Stand verwenden (inkl. IDs/managed graph)
                    catalog = saved;
                }

            } catch (Exception e) {
                log.warn("Demo-Katalog konnte nicht in die DB geschrieben werden (weiter mit In-Memory).", e);
            }
}

        private Catalog upsertSingleRootCatalog(Catalog demo) {
            final Long ROOT_ID = 1L;

            // Defensive: Relationen vor Persistenz sicher setzen
            enforceBackReferences(demo);

            // WICHTIG: Spring Data "save" würde bei gesetzter ID immer "merge" nutzen.
            // Bei assigned IDs (z.B. prod-0001) führt merge dazu, dass Hibernate versucht,
            // die Entitäten zuerst zu laden. Wenn sie nicht existieren, kann das in
            // EntityNotFoundException/JpaObjectRetrievalFailureException enden.
            //
            // Deshalb: echter Upsert über "delete + persist".

            if (catalogRepository.existsById(ROOT_ID)) {
                catalogRepository.deleteById(ROOT_ID);
                catalogRepository.flush();
                // Sicherstellen, dass der Persistence Context keine Reste des gelöschten Graphen hält
                entityManager.clear();
            }

            entityManager.persist(demo);
            entityManager.flush();
            return demo;
        }

        private void enforceBackReferences(Catalog c) {
            if (c == null) return;
            if (c.getCategories() != null) {
                for (Category cat : c.getCategories()) {
                    cat.setCatalog(c);
                }
            }
            Map<String, Category> byId = new HashMap<>();
            if (c.getCategories() != null) {
                for (Category cat : c.getCategories()) {
                    byId.put(cat.getId(), cat);
                }
            }
            if (c.getProducts() != null) {
                for (Product p : c.getProducts()) {
                    p.setCatalog(c);
                    if (p.getCategory() == null) {
                        Category cat = byId.get(p.getCategoryId());
                        if (cat != null) p.setCategory(cat);
                    }
                    if (p.getSpecs() != null) p.getSpecs().setProduct(p);
                    if (p.getPricing() != null) p.getPricing().setProduct(p);
                    if (p.getInventory() != null) p.getInventory().setProduct(p);
                    if (p.getShipping() != null) p.getShipping().setProduct(p);
                    if (p.getMedia() != null) {
                        p.getMedia().setProduct(p);
                        if (p.getMedia().getImages() != null) {
                            p.getMedia().getImages().forEach(img -> img.setMedia(p.getMedia()));
                        }
                    }
                }
            }
        }
    }

    private Catalog catalog;

    public Catalog getCatalog() {
        return catalog;
    }
}