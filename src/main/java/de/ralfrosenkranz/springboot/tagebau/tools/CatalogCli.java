package de.ralfrosenkranz.springboot.tagebau.tools;

import de.ralfrosenkranz.springboot.tagebau.tools.image.*;
import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.tools.catalog.CatalogLinker;
import de.ralfrosenkranz.springboot.tagebau.tools.catalog.CatalogParser;
import de.ralfrosenkranz.springboot.tagebau.tools.catalog.CatalogValidator;

import java.io.File;
import java.nio.file.Path;
import java.net.URI;

public class CatalogCli {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            usage();
            System.exit(2);
        }

        String mode = args[0];
        if ("--parse".equals(mode)) {
            Path json = Path.of(args[1]);
            Catalog catalog = loadLinkedValidated(json);
            System.out.println("OK: parsed products=" + catalog.getProducts().size() + ", categories=" + catalog.getCategories().size());
            return;
        }

        if ("--generate-images".equals(mode)) {
            if (args.length < 3) {
                usage();
                System.exit(2);
            }
            Path json = Path.of(args[1]);
            Path outRoot = Path.of(args[2]);

            ImageJobRunner.Settings s = new ImageJobRunner.Settings();
            // parse optional flags
            for (int i = 3; i < args.length; i++) {
                switch (args[i]) {
                    case "--sd-url" -> s.sdUrl = URI.create(args[++i]);
                    case "--size" -> {
                        String[] wh = args[++i].toLowerCase().split("x");
                        s.width = Integer.parseInt(wh[0]);
                        s.height = Integer.parseInt(wh[1]);
                    }
                    case "--steps" -> s.steps = Integer.parseInt(args[++i]);
                    case "--cfg" -> s.cfg = Double.parseDouble(args[++i]);
                    case "--sampler" -> s.sampler = args[++i];
                    case "--concurrency" -> s.concurrency = Integer.parseInt(args[++i]);
                    case "--thumb" -> s.thumbSize = Integer.parseInt(args[++i]);
                    case "--attempts" -> s.maxAttempts = Integer.parseInt(args[++i]);
                    case "--seed-base" -> { s.seedBase = Long.parseLong(args[++i]); s.seedFromHash = false; }
                    case "--seed-hash" -> s.seedFromHash = true;
                    case "--no-skip-existing" -> s.skipExisting = false;
                    default -> throw new IllegalArgumentException("Unknown arg: " + args[i]);
                }
            }

            Catalog catalog = loadLinkedValidated(json);
            ImageJobPlan plan = new ImageJobPlanner().plan(catalog, outRoot);

            System.out.println("Planned image jobs: " + plan.jobs().size());
            ImageJobRunner runner = new ImageJobRunner(s);
            runner.run(plan);

            long ok = plan.jobs().stream().filter(j -> j.status == ImageJob.Status.DONE).count();
            long fail = plan.jobs().stream().filter(j -> j.status == ImageJob.Status.FAILED).count();
            System.out.println("DONE=" + ok + " FAILED=" + fail);

            if (fail > 0) {
                System.out.println("Failed jobs:");
                for (ImageJob j : plan.jobs()) {
                    if (j.status == ImageJob.Status.FAILED) {
                        System.out.println(" - " + j.jobId + " -> " + j.lastError);
                    }
                }
                System.exit(1);
            }
            return;
        }

        usage();
        System.exit(2);
    }

    private static Catalog loadLinkedValidated(Path json) throws Exception {
        System.out.println (new File(".").getAbsolutePath ());
        System.out.println (json.toFile().getAbsolutePath ());
        Catalog catalog = new CatalogParser().parse(json);
        new CatalogLinker().link(catalog);
        new CatalogValidator().validate(catalog);
        return catalog;
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("  --parse <katalog.json>");
        System.out.println("  --generate-images <katalog.json> <out-root> [options]");
        System.out.println("Options (generate-images):");
        System.out.println("  --sd-url <http://127.0.0.1:7860>");
        System.out.println("  --size <WxH> (default 1024x1024)");
        System.out.println("  --steps <n> (default 30)");
        System.out.println("  --cfg <float> (default 7.0)");
        System.out.println("  --sampler <name> (default DPM++ 2M Karras)");
        System.out.println("  --concurrency <n> (default 4)");
        System.out.println("  --thumb <px> (default 256)");
        System.out.println("  --attempts <n> (default 3)");
        System.out.println("  --seed-base <n> (fixed seed, disables hash mode)");
        System.out.println("  --seed-hash (default; seed derived from productId+role)");
        System.out.println("  --no-skip-existing (re-generate even if files exist)");
    }
}
