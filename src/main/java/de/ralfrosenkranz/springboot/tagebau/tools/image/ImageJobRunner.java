package de.ralfrosenkranz.springboot.tagebau.tools.image;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.nio.file.Path;

public class ImageJobRunner {

    public static class Settings {
        public URI sdUrl = URI.create("http://127.0.0.1:7860");
        public int width = 1024;
        public int height = 1024;
        public int steps = 30;
        public double cfg = 7.0;
        public String sampler = "DPM++ 2M Karras";
        public int concurrency = 4;
        public int thumbSize = 256;
        public int maxAttempts = 3;
        public long seedBase = 0L;
        public boolean seedFromHash = true;
        public boolean skipExisting = true;
    }

    private final StableDiffusionWebUiClient client;
    private final ThumbnailService thumbnailService = new ThumbnailService();
    private final Settings settings;

    public ImageJobRunner(Settings settings) {
        this.settings = settings;
        this.client = new StableDiffusionWebUiClient(settings.sdUrl);
    }

    public void run(ImageJobPlan plan) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(settings.concurrency);
        List<Future<?>> futures = new ArrayList<>();

        for (ImageJob job : plan.jobs()) {
            futures.add(pool.submit(() -> runOne(job)));
        }

        pool.shutdown();
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (ExecutionException e) {
                // errors are recorded per job; continue
            }
        }
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    private void runOne(ImageJob job) {
        if (settings.skipExisting && Files.exists(job.outputFile) && Files.exists(job.thumbnailFile)) {
            job.status = ImageJob.Status.DONE;
            return;
        }

        for (int attempt = 1; attempt <= settings.maxAttempts; attempt++) {
            job.attempts = attempt;
            job.status = ImageJob.Status.RUNNING;
            try {
                Files.createDirectories(job.outputFile.getParent());

                long seed = computeSeed(job);
                byte[] png = client.txt2img(job.prompt, settings.width, settings.height, settings.steps, settings.cfg, settings.sampler, seed);
                Files.write(job.outputFile, png);

                thumbnailService.writeThumbnail(job.outputFile, job.thumbnailFile, settings.thumbSize);

                job.status = ImageJob.Status.DONE;
                job.lastError = null;
                return;
            } catch (Exception ex) {
                job.lastError = ex.getMessage();
                job.status = ImageJob.Status.FAILED;
                // simple backoff
                try { Thread.sleep(800L * attempt); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
    }

    private long computeSeed(ImageJob job) {
        if (!settings.seedFromHash) return settings.seedBase;
        long h = 1125899906842597L; // prime
        String s = job.productId + "|" + job.role + "|" + settings.seedBase;
        for (int i=0;i<s.length();i++) h = 31*h + s.charAt(i);
        return Math.abs(h);
    }
}
