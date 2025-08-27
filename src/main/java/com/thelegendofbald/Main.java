package com.thelegendofbald;

import javax.swing.SwingUtilities;

import com.thelegendofbald.model.sounds.SoundManager;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.main.GameWindow;

public final class Main {

    private Main() {
        // Prevent instantiation
    }

    public static void main(String[] args) {
        // Assicurati di eseguire Swing in modo thread-safe
        SwingUtilities.invokeLater(() -> {
            // Crea e mostra la finestra del gioco
            new GameWindow().display();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LoggerUtils.info("Closing all sounds...");
            LoggerUtils.closeLogger();
            SoundManager.closeAll();
        }));
    }
}
