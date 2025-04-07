package com.thelegendofbald.ui;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        // Imposta il titolo della finestra
        super("The Legend of Bald");

        // Imposta la dimensione della finestra
        setSize(800, 600);

        // Chiusura della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Aggiungi un pannello per il rendering del gioco (o altro)
        add(new GamePanel());
    }
}
