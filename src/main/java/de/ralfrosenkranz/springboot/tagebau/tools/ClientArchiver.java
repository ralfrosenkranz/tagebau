package de.ralfrosenkranz.springboot.tagebau.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ClientArchiver {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ClientArchiver <project-root> <output.zip>");
            System.exit(1);
        }

        String projectRoot = args[0];
        String outputZip = args[1];

        // Add timestamp to filename
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm"));
        String stampedOutputZip = outputZip.replace(".zip", "_" + timestamp + ".zip");

        try {
            createZipArchive(projectRoot, stampedOutputZip);
            File zipFile = new File(stampedOutputZip).getAbsoluteFile();
            System.out.println("Project archived successfully to " + zipFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error creating archive: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void createZipArchive(String projectRoot, String outputZip) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputZip);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            Path rootPath = Paths.get(projectRoot);
            Path staticPath = rootPath.resolve("src").resolve("main").resolve("resources").resolve("static");

            if (Files.exists(staticPath) && Files.isDirectory(staticPath)) {
                Files.walkFileTree(staticPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        // Check if file matches any of the target extensions
                        String fileName = file.getFileName().toString();
                        if (isTargetFile(fileName)) {
                            Path relativePath = staticPath.relativize(file);
                            zos.putNextEntry(new ZipEntry("src/main/resources/static/" + relativePath.toString().replace("\\", "/")));
                            Files.copy(file, zos);
                            zos.closeEntry();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        }
    }

    private static boolean isTargetFile(String fileName) {
        return  fileName.endsWith(".xml") ||
                fileName.endsWith(".yml") ||
                fileName.endsWith(".yaml") ||
                fileName.endsWith(".json") ||
                fileName.endsWith(".txt") ||
                fileName.endsWith(".html") ||
                fileName.endsWith(".xhtml") ||
                fileName.endsWith(".css") ||
                fileName.endsWith(".js");
    }
}
