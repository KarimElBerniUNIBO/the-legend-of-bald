package com.thelegendofbald.ui.model;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel(Dimension size) {
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Logica per disegnare il gioco (esempio: disegnare il personaggio)
        g.setColor(Color.RED);
        g.fillRect(100, 100, 50, 50);  // Disegna un quadrato rosso come esempio
    }
}

