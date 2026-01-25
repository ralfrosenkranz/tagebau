package de.ralfrosenkranz.springboot.tagebau.server.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageService {

    //Beispiel: http://localhost:8080/api/products/prod-0002/thumbnail
    public ByteArrayResource getImageResourceAsJpegByteArray(String imageFilePath) {
        try {
            // Determine the resource path - assuming it's relative to classpath:/static/
            String resourcePath = "static/tagebau/" + imageFilePath;

            // Get the resource from classpath
            org.springframework.core.io.Resource resource = new org.springframework.core.io.ClassPathResource(resourcePath);

            // Read the resource into byte array
            java.io.InputStream inputStream = resource.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();
            outputStream.close();
            inputStream.close();

            return new ByteArrayResource(imageBytes);
        } catch (IOException e) {
            // Return broken image resource on error
            return getMissingImageResourceAsJpegByteArray();
        }
    }

    public ByteArrayResource getMissingImageResourceAsJpegByteArray() {
        try {
            // Create a 200x200 pixel image
            BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Fill background with light gray
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, 200, 200);

            // Draw a red X (missing resource indicator)
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(10));
            g2d.drawLine(40, 40, 160, 160);
            g2d.drawLine(160, 40, 40, 160);

            // Draw "MISSING" text
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics fm = g2d.getFontMetrics();
            String text = "MISSING";
            int textWidth = fm.stringWidth(text);
            g2d.drawString(text, (200 - textWidth) / 2, 100);

            // Convert to JPEG
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "JPEG", jpegOutputStream);
            jpegOutputStream.close();

            g2d.dispose();

            return new ByteArrayResource(jpegOutputStream.toByteArray());
        } catch (IOException e) {
            // Fallback to empty resource if something fails
            return new ByteArrayResource(new byte[0]);
        }
    }
}
