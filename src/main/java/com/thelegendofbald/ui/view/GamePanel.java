package com.thelegendofbald.ui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;

public class GamePanel extends JPanel {
    
    private BufferedImage image;
    private static String path;
    private final Bald bald = new Bald(60, 60, 100, "Bald", 50);
    private final DummyEnemy dummyenemy = new DummyEnemy(500, 200, 50, "ZioBilly", 50);// Create an instance of Bald
    Timer timer = new Timer(16, e -> update()); // 60 FPS (1000ms / 60 ≈ 16ms)
    public GamePanel(Dimension size, String path) {
        timer.start();
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.path = path;
        loadImage();
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
    
            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });
    }
        

    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: // Freccia su
                bald.setSpeedY(-5); // Muovi Bald verso l'alto
             break;
            case KeyEvent.VK_DOWN: // Freccia giù
                bald.setSpeedY(5); // Muovi Bald verso il basso
                break;
            case KeyEvent.VK_LEFT: // Freccia sinistra
                bald.setSpeedX(-5); // Muovi Bald verso sinistra
                break;
            case KeyEvent.VK_RIGHT: // Freccia destra
                bald.setSpeedX(5); // Muovi Bald verso destra
                break;
        }
    }
    private void handleKeyRelease(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                bald.setSpeedY(0); // Ferma il movimento verticale
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                bald.setSpeedX(0); // Ferma il movimento orizzontale
                break;
        }
    }

    private void loadImage() {
        try {
            // Load image from resources folder (adjust path as needed)
            InputStream is = getClass().getResourceAsStream(this.path);
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


        if (image != null) {
            g.drawImage(image, 0, 0, 900, 600, null);
        } else {
            // Fallback to the square if image loading failed
            g.setColor(Color.RED);
            g.fillRect(100, 100, 50, 50);
        }
        bald.render(g);
        dummyenemy.render(g);

    }

    public void update() {
        bald.move();
        bald.updateAnimation();
        dummyenemy.followPlayer(bald);
        dummyenemy.updateAnimation();
        
        repaint(); // Repaint the panel to reflect changes
    }
    
    
}

