package de.ralfrosenkranz.springboot.tagebau.server.controller.dto;

public class MediaImageDTO {
    private String file;
    private String thumbnailFile;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getThumbnailFile() {
        return thumbnailFile;
    }

    public void setThumbnailFile(String thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }
}
