package com.thelegendofbald.ui.model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
        
        // Draw the image instead of the red square
        if (image != null) {
            g.drawImage(image, 100, 100, 50, 50, null);
        } else {
            // Fallback to the square if image loading failed
            g.setColor(Color.RED);
            g.fillRect(100, 100, 50, 50);
        }
    }
    
}

