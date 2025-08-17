package com.thelegendofbald.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LoggerUtils {

    private static final String LOG_DIRECTORY_NAME = "logs";
    private static final Logger LOGGER = Logger.getLogger(LoggerUtils.class.getName());
    private static FileHandler fileHandler;

    static {
        try {
            Path logsDir = Paths.get(LOG_DIRECTORY_NAME);
            if (!Files.exists(logsDir)) {
                Files.createDirectories(logsDir);
            }

            zipOlderLogs(logsDir);
            createNewLogFile();
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize logger", e);
        }
    }

    private static void zipOlderLogs(Path logsDir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
            for (Path logFile : stream) {
                zipLogFile(logFile);
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to zip older log files", e);
        }
    }

    private static void zipLogFile(Path logFile) throws IOException {
        String zipFileName = logFile.toString().replaceAll("\\.log$", ".zip");
        Path zipFilePath = Paths.get(zipFileName);
        if (Files.exists(zipFilePath)) {
            return;
        }

        try (
            FileInputStream fis = new FileInputStream(logFile.toFile());
            FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
            ZipOutputStream zos = new ZipOutputStream(fos);   
        ) {
            ZipEntry zipEntry = new ZipEntry(logFile.getFileName().toString());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to zip log file: " + logFile, e);
        }

        Files.delete(logFile);
    }

    private static void createNewLogFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String logFileName = LOG_DIRECTORY_NAME + File.separator + timestamp + ".log";
        fileHandler = new FileHandler(logFileName, true);
        fileHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fileHandler);
        LOGGER.setUseParentHandlers(true);
    }

    public static void info(String message) {
        LOGGER.log(Level.INFO, message);
    }

    public static void warning(String message) {
        LOGGER.log(Level.WARNING, message);
    }

    public static void error(String message) {
        LOGGER.log(Level.SEVERE, message);
    }

    public static void closeLogger() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }

}
