package de.ralfrosenkranz.springboot.tagebau.tools.image;

import java.nio.file.Path;
import java.util.Objects;

public class ImageJob {
    public enum Status { PENDING, RUNNING, DONE, FAILED }

    public final String jobId;
    public final String productId;
    public final String role;
    public final String label;
    public final String prompt;
    public final Path outputFile;
    public final Path thumbnailFile;

    public volatile Status status = Status.PENDING;
    public volatile int attempts = 0;
    public volatile String lastError = null;

    public ImageJob(String jobId, String productId, String role, String label, String prompt, Path outputFile, Path thumbnailFile) {
        this.jobId = Objects.requireNonNull(jobId);
        this.productId = Objects.requireNonNull(productId);
        this.role = Objects.requireNonNull(role);
        this.label = Objects.requireNonNull(label);
        this.prompt = Objects.requireNonNull(prompt);
        this.outputFile = Objects.requireNonNull(outputFile);
        this.thumbnailFile = Objects.requireNonNull(thumbnailFile);
    }
}
