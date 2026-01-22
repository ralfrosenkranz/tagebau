package de.ralfrosenkranz.springboot.tagebau;

import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.tools.catalog.CatalogLinker;
import de.ralfrosenkranz.springboot.tagebau.tools.catalog.CatalogParser;
import de.ralfrosenkranz.springboot.tagebau.tools.catalog.CatalogValidator;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

@SpringBootApplication
public class Tagebau extends SpringBootServletInitializer {

    private static Logger log = LoggerFactory.getLogger(Tagebau.class);

    @Override
    protected SpringApplicationBuilder
    configure(SpringApplicationBuilder application) {
        return application.sources(Tagebau.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Tagebau.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return super.configure(builder);
//    }

//    public static void main(String[] args) {
//        SpringApplication.run(Tagebau.class, args);
//    }


    private Catalog loadLinkedValidated(Path json) throws Exception {
        log.debug (new File(".").getAbsolutePath ());
        log.debug (json.toFile().getAbsolutePath ());
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


    @Component
    public class StartupListener{
        private static final Logger log = LoggerFactory.getLogger(StartupListener.class);

        @Autowired
        ApplicationArgumentsReader argsReader;

        @PostConstruct
        public void onStartup() {
            log.info("Application is starting now");
            initDemoCatalog (argsReader.getArgs());
        }

        private void initDemoCatalog (String[] args) {
            try {
                //Path json = Path.of(args[1]);
                Path json = Path.of("tagebau_demo_katalog.json");
                catalog = loadLinkedValidated(json);
                int i = 0;
            } catch (Exception e) {
                log.error ("ERROR loading Demo-Catalog", e);
            }
        }
    }

    private Catalog catalog;

    public Catalog getCatalog ()
    {
        return catalog;
    }
}
