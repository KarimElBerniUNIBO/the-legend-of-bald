package com.thelegendofbald.ui.model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    
    private BufferedImage image;

    public GamePanel(Dimension size) {
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        loadImage();
    }

    private void loadImage() {
        try {
            // Load image from resources folder (adjust path as needed)
            InputStream is = getClass().getResourceAsStream("/images/character.png");
            if (is != null) {
                image = ImageIO.read(is);
            } else {
                System.err.println("Image file not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Logica per disegnare il gioco (esempio: disegnare il personaggio)
        g.setColor(Color.RED);
        g.fillRect(100, 100, 50, 50);  // Disegna un quadrato rosso come esempio
    }
}

