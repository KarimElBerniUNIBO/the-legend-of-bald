package com.thelegendofbald;

import javax.swing.*;

import com.thelegendofbald.ui.view.GameWindow;

public class Main {
    public static void main(String[] args) {
        // Assicurati di eseguire Swing in modo thread-safe
        SwingUtilities.invokeLater(() -> {
            // Crea e mostra la finestra del gioco
            new GameWindow().display();
        });
    }
}

