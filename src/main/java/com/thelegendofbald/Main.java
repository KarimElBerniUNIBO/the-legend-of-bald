package com.thelegendofbald;

import javax.swing.SwingUtilities;
import com.thelegendofbald.model.sounds.SoundManager;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.main.GameWindow;

/**
 * Entry point of the Legend of Bald game.
 * <p>
 * This class initializes the game window and registers shutdown hooks
 * to properly release resources such as sounds and loggers.
 * </p>
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Main entry point of the application.
     *
     * @param args the command line arguments (not used, but required by the JVM)
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow().display());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerUtils.info("Closing all sounds...");
            SoundManager.closeAll();
            LoggerUtils.closeLogger();
        }));
    }
}
