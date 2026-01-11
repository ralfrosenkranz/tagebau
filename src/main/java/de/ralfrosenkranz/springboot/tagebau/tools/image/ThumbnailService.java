package de.ralfrosenkranz.springboot.tagebau.tools.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ThumbnailService {

    public void writeThumbnail(Path inputPng, Path outputPng, int size) throws IOException {
        BufferedImage src = ImageIO.read(inputPng.toFile());
        if (src == null) throw new IOException("Could not read image: " + inputPng);

        int w = src.getWidth();
        int h = src.getHeight();
        int side = Math.min(w, h);
        int x = (w - side) / 2;
        int y = (h - side) / 2;

        BufferedImage cropped = src.getSubimage(x, y, side, side);
        BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(cropped, 0, 0, size, size, null);
        g.dispose();

        Files.createDirectories(outputPng.getParent());
        ImageIO.write(scaled, "png", outputPng.toFile());
    }
}
