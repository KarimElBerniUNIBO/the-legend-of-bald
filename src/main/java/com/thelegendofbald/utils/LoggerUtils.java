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
import java.util.Locale;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for logging messages to log files with automatic log rotation
 * and zipping of older logs.
 */
public final class LoggerUtils {

    private static final String LOG_DIRECTORY_NAME = "logs";
    private static final Logger LOGGER = Logger.getLogger(LoggerUtils.class.getName());
    private static FileHandler fileHandler;

    private LoggerUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    static {
        try {
            final Path logsDir = Paths.get(LOG_DIRECTORY_NAME);
            if (!Files.exists(logsDir)) {
                Files.createDirectories(logsDir);
            }

            zipOlderLogs(logsDir);
            createNewLogFile();

        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize logger", e);
        }
    }

    private static void zipOlderLogs(final Path logsDir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
            for (final Path logFile : stream) {
                zipLogFile(logFile);
            }

        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to zip older log files", e);
        }
    }

    private static void zipLogFile(final Path logFile) throws IOException {
        final Path logFilePath = Optional.ofNullable(logFile)
                .orElseThrow(() -> new IllegalArgumentException("logFile cannot be null"));
        final String zipFileName = logFilePath.toString().replaceAll("\\.log$", ".zip");
        final Path zipFilePath = Paths.get(zipFileName);
        if (Files.exists(zipFilePath)) {
            return;
        }

        try (
                FileInputStream fis = new FileInputStream(logFilePath.toFile());
                FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
                ZipOutputStream zos = new ZipOutputStream(fos);) {
            final Path logFileNamePath = Optional.ofNullable(logFilePath.getFileName())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Could not get file name from path: " + logFilePath));
            final ZipEntry zipEntry = new ZipEntry(logFileNamePath.toString());
            zos.putNextEntry(zipEntry);

            final int bufferSize = 1024;
            final byte[] buffer = new byte[bufferSize];
            int length = fis.read(buffer);

            while (length > 0) {
                zos.write(buffer, 0, length);
                length = fis.read(buffer);
            }

            zos.closeEntry();
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to zip log file: " + logFilePath, e);
        } catch (final IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        Files.delete(logFilePath);
    }

    private static void createNewLogFile() throws IOException {
        final String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.ROOT).format(new Date());
        final String logFileName = LOG_DIRECTORY_NAME + File.separator + timestamp + ".log";
        fileHandler = new FileHandler(logFileName, true);
        fileHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fileHandler);
        LOGGER.setUseParentHandlers(true);
    }

    /**
     * Logs an info message.
     * 
     * @param message The message to log.
     */
    public static void info(final String message) {
        LOGGER.log(Level.INFO, message);
    }

    /**
     * Logs a warning message.
     * 
     * @param message The message to log.
     */
    public static void warning(final String message) {
        LOGGER.log(Level.WARNING, message);
    }

    /**
     * Logs an error message.
     * 
     * @param message The message to log.
     */
    public static void error(final String message) {
        LOGGER.log(Level.SEVERE, message);
    }

    /**
     * Logs an info message with an associated throwable.
     * 
     * @param message The message to log.
     * @param t       The throwable to log.
     */
    public static void info(final String message, final Throwable t) {
        LOGGER.log(Level.INFO, message, t);
    }

    /**
     * Logs a warning message with an associated throwable.
     * 
     * @param message The message to log.
     * @param t       The throwable to log.
     */
    public static void warning(final String message, final Throwable t) {
        LOGGER.log(Level.WARNING, message, t);
    }

    /**
     * Logs an error message with an associated throwable.
     * 
     * @param message The message to log.
     * @param t       The throwable to log.
     */
    public static void error(final String message, final Throwable t) {
        LOGGER.log(Level.SEVERE, message, t);
    }

    /**
     * Closes the logger and releases associated resources.
     */
    public static void closeLogger() {
        Optional.ofNullable(fileHandler).ifPresent(FileHandler::close);
    }

}
