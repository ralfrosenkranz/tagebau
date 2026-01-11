package de.ralfrosenkranz.springboot.tagebau.tools.image;

import java.util.*;

public class ImageJobPlan {
    private final Map<String, ImageJob> byId = new LinkedHashMap<>();
    private final List<ImageJob> ordered = new ArrayList<>();

    public void add(ImageJob job) {
        if (byId.containsKey(job.jobId)) {
            throw new IllegalArgumentException("Duplicate jobId: " + job.jobId);
        }
        byId.put(job.jobId, job);
        ordered.add(job);
    }

    public List<ImageJob> jobs() {
        return Collections.unmodifiableList(ordered);
    }

    public Map<String, ImageJob> jobMap() {
        return Collections.unmodifiableMap(byId);
    }
}
