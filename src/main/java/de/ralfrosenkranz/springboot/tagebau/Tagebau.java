package de.ralfrosenkranz.springboot.tagebau;

import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
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

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;

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

                // Optional: Wenn DB erreichbar ist, Demo-Daten persistieren, damit beim nächsten Start nicht erneut initialisiert wird.
                tryPersistDemoCatalog(demo);

            } catch (Exception e) {
                log.error("ERROR loading Demo-Catalog", e);
            }
        }

        @Transactional
        protected void tryPersistDemoCatalog(Catalog demo) {
            try (Connection ignored = dataSource.getConnection()) {
                // save() cascaded Categories/Products, sofern der Parser/Linker die Relationen korrekt setzt
                catalogRepository.save(demo);
            } catch (Exception e) {
                log.warn("Demo-Katalog konnte nicht in die DB geschrieben werden (weiter mit In-Memory).", e);
            }
        }
    }

    private Catalog catalog;

    public Catalog getCatalog() {
        return catalog;
    }
}
