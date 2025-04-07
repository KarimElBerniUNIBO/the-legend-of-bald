package com.thelegendofbald;



import javax.swing.*;
// Ensure the correct package path for GameWindow
import com.thelegendofbald.ui.GameWindow;

public class Main {
    public static void main(String[] args) {
        // Assicurati di eseguire Swing in modo thread-safe
        SwingUtilities.invokeLater(() -> {
            // Crea e mostra la finestra del gioco
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}

