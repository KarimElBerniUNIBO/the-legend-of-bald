package com.thelegendofbald.ui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

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
    private final Set<Integer> pressedKeys = new HashSet<>();
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
          pressedKeys.add(e.getKeyCode());
          updateSpeed();
     }

       @Override
       public void keyReleased(KeyEvent e) {
           pressedKeys.remove(e.getKeyCode());
          updateSpeed();
       }
    });


    }

    private void updateSpeed() {
        bald.updateAnimation();
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            bald.setSpeedX(5);
        } else if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            bald.setSpeedX(-5);
        } else {
            bald.setSpeedX(0);
        }
    
        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            bald.setSpeedY(-5);
        } else if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            bald.setSpeedY(5);
        } else {
            bald.setSpeedY(0);
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
        //bald.updateAnimation();
        dummyenemy.followPlayer(bald);
        dummyenemy.updateAnimation();
        
        repaint(); // Repaint the panel to reflect changes
    }
    
    
}

