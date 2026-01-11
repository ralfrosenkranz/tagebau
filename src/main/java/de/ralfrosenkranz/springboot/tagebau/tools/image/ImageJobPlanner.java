package de.ralfrosenkranz.springboot.tagebau.tools.image;

import de.ralfrosenkranz.springboot.tagebau.server.model.Catalog;
import de.ralfrosenkranz.springboot.tagebau.server.model.MediaImage;
import de.ralfrosenkranz.springboot.tagebau.server.model.Product;

import java.nio.file.Path;
import java.util.Locale;

public class ImageJobPlanner {

    private static final String STYLE_SUFFIX = "\nStilattribute: Dramatisch, Heroisch.";

    public ImageJobPlan plan(Catalog catalog, Path outRoot) {
        ImageJobPlan plan = new ImageJobPlan();
        for (Product p : catalog.getProducts()) {
            if (p.getMedia() == null) continue;
            for (MediaImage img : p.getMedia().getImages()) {
                String basePrompt = img.getGenerationPrompt();
                if (basePrompt == null || basePrompt.isBlank()) {
                    // fallback: construct minimal prompt
                    basePrompt = "Photorealistic studio product photo of " + p.getTechnicalName() + ", industrial machinery.";
                }
                String prompt = basePrompt + STYLE_SUFFIX;

                Path outFile = outRoot.resolve(normalizeRel(img.getFile()));
                Path thumbFile = outRoot.resolve(normalizeRel(img.getThumbnailFile()));

                String jobId = p.getId() + ":" + img.getRole();
                plan.add(new ImageJob(jobId, p.getId(), img.getRole(), img.getLabel(), prompt, outFile, thumbFile));
            }
        }
        return plan;
    }

    private static Path normalizeRel(String rel) {
        // avoid accidental absolute paths; keep as relative
        String r = rel.replace('\\', '/');
        while (r.startsWith("/")) r = r.substring(1);
        return Path.of(r);
    }
}
