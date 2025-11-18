package com.thelegendofbald;

import javax.swing.SwingUtilities;

import com.thelegendofbald.model.sounds.SoundManager;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.main.GameWindow;

/**
 * The Main class is the primary entry point for the "The Legend of Bald" application.
 * It is responsible for launching the game's graphical user interface and handling cleanup
 * operations upon application shutdown.
 * This is a utility class and cannot be instantiated.
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * The main method which serves as the application's entry point.
     * <p>
     * It launches the game window (`GameWindow`) on the Swing Event Dispatch Thread to ensure
     * UI thread-safety.
     * It also registers a shutdown hook to ensure that resources, such as sounds and the logger,
     * are properly closed upon JVM termination.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameWindow().display();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerUtils.info("Closing all sounds...");
            SoundManager.closeAll();
            LoggerUtils.closeLogger();
        }));
    }
}
