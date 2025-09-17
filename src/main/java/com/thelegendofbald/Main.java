package com.thelegendofbald;

import javax.swing.SwingUtilities;

import com.thelegendofbald.model.sounds.SoundManager;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.main.GameWindow;

public final class Main {

    private Main() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void main(String[] args) {
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
