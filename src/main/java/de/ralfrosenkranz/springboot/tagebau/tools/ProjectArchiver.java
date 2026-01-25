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

public class ProjectArchiver {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ProjectArchiver <project-root> <output.zip>");
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
            Path srcPath = rootPath.resolve("src");

            // Add pom.xml from root directory
            Path pomPath = rootPath.resolve("pom.xml");
            if (Files.exists(pomPath)) {
                zos.putNextEntry(new ZipEntry("pom.xml"));
                Files.copy(pomPath, zos);
                zos.closeEntry();
            }

            if (Files.exists(srcPath) && Files.isDirectory(srcPath)) {
                Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        // Check if file matches any of the target extensions
                        String fileName = file.getFileName().toString();
                        if (isTargetFile(fileName)) {
                            Path relativePath = srcPath.relativize(file);
                            zos.putNextEntry(new ZipEntry("src/" + relativePath.toString().replace("\\", "/")));
                            Files.copy(file, zos);
                            zos.closeEntry();
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        // Skip hidden directories
                        if (dir.getFileName().toString().startsWith(".")) {
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        }
    }

    private static boolean isTargetFile(String fileName) {
        String[] targetExtensions = {".java", ".xhtml", ".js", ".css", "pom.xml", ".properties", ".yaml", ".yml"};
        for (String ext : targetExtensions) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
