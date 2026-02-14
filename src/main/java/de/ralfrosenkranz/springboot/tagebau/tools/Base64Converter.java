package de.ralfrosenkranz.springboot.tagebau.tools;

/*
        Erstelle bitte ein Kommandozeilenprogramm in Java, das eine beliebige Binär-Datei in ein Base64 Encodiertes Äquvalent verwandelt. und umgekegrt.  Es gibt also zwei Modi, (1) bin to base64, und (2) base64 to bin. Das Ausgegebene File soll im Fall (1) den ursprünglichen Dateinamen ohne Extension tragen, gefolgt von einem Timestamp mit folgendem Format "String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("_yyyy_MM_dd_HH_mm"));
        "  und darauf folgend die ursprünglich Extension und danach .base64.txt tragen. Im Fall (2) soll  der Timstamp aktuell erneuert werden, und .base64.txt soll wieder entfernt werden. Es dürfen keine Dateien überschrieben werden.

        Bitte ändere das programm so, dass es den anzuwendenden Modis anhand der Extension der Datei selbst erkennt. Dadurch erwartet das Programm nann nur noch einen einzigen Parameter.
 */

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class Base64Converter {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Base64Converter <file>");
            System.exit(1);
        }

        String inputFile = args[0];

        try {
            Path inputPath = Paths.get(inputFile);
            if (!Files.exists(inputPath)) {
                throw new IOException("Input file does not exist: " + inputFile);
            }

            // Detect mode based on file extension
            String fileName = inputPath.getFileName().toString();
            if (fileName.endsWith(".base64.txt")) {
                convertBase64ToBinary(inputFile);
            } else {
                convertBinaryToBase64(inputFile);
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void convertBinaryToBase64(String inputFile) throws IOException {
        Path inputPath = Paths.get(inputFile);

        // Read binary data
        byte[] fileData = Files.readAllBytes(inputPath);

        // Encode to Base64
        String encodedData = Base64.getEncoder().encodeToString(fileData);

        // Generate output filename
        String fileName = inputPath.getFileName().toString();
        String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("_yyyy_MM_dd_HH_mm"));
        String outputFileName = nameWithoutExt + timestamp + extension + ".base64.txt";

        // Write to output file
        Path outputPath = inputPath.getParent().resolve(outputFileName);
        if (Files.exists(outputPath)) {
            throw new IOException("Output file already exists: " + outputFileName);
        }
        Files.write(outputPath, encodedData.getBytes());

        System.out.println("Base64 encoded file created: " + outputPath.toAbsolutePath());
    }

    private static void convertBase64ToBinary(String inputFile) throws IOException {
        Path inputPath = Paths.get(inputFile);

        // Read Base64 data
        String encodedData = new String(Files.readAllBytes(inputPath));

        // Decode Base64
        byte[] decodedData = Base64.getDecoder().decode(encodedData);

        // Generate output filename
        String fileName = inputPath.getFileName().toString();
        String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        String nameWithoutTimestamp = nameWithoutExt.substring(0, nameWithoutExt.lastIndexOf('_'));
        String timestamp = nameWithoutExt.substring(nameWithoutExt.lastIndexOf('_'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        String outputFileName = nameWithoutTimestamp + timestamp + extension;

        // Write to output file
        Path outputPath = inputPath.getParent().resolve(outputFileName);
        if (Files.exists(outputPath)) {
            throw new IOException("Output file already exists: " + outputFileName);
        }
        Files.write(outputPath, decodedData);

        System.out.println("Binary file created: " + outputPath.toAbsolutePath());
    }
}
