package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.lang.Math;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.thelegendofbald.api.views.MainView;
import com.thelegendofbald.api.views.View;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;

public class GamePanel extends JPanel implements Runnable {

    private final Bald bald = new Bald(60, 60, 100, "Bald", 50);
    private final DummyEnemy dummyenemy = new DummyEnemy(500, 200, 50, "ZioBilly", 50);
    private final GridPanel gridPanel;
    private final TileMap tileMap;

    private Thread gameThread;

    private final Set<Integer> pressedKeys = new HashSet<>();

    private final MainView window;

    public GamePanel(Dimension size, MainView window) {
        this.window = window;
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);

        this.gridPanel = new GridPanel();
        this.gridPanel.setOpaque(false);
        this.gridPanel.setBounds(0, 0, size.width, size.height);
        this.add(gridPanel);

        this.tileMap = new TileMap(size.width, size.height);
        this.requestFocusInWindow();

        setupKeyBindings();
        this.startGame();
        
        SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
    }

    private void setupKeyBindings() {
    InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = this.getActionMap();

    // Tasti premuti
    bindKey(im, am, "pressed UP", KeyEvent.VK_UP, true, () -> pressedKeys.add(KeyEvent.VK_UP));
    bindKey(im, am, "pressed DOWN", KeyEvent.VK_DOWN, true, () -> pressedKeys.add(KeyEvent.VK_DOWN));
    bindKey(im, am, "pressed LEFT", KeyEvent.VK_LEFT, true, () -> pressedKeys.add(KeyEvent.VK_LEFT));
    bindKey(im, am, "pressed RIGHT", KeyEvent.VK_RIGHT, true, () -> pressedKeys.add(KeyEvent.VK_RIGHT));

    // Tasti rilasciati
    bindKey(im, am, "released UP", KeyEvent.VK_UP, false, () -> pressedKeys.remove(KeyEvent.VK_UP));
    bindKey(im, am, "released DOWN", KeyEvent.VK_DOWN, false, () -> pressedKeys.remove(KeyEvent.VK_DOWN));
    bindKey(im, am, "released LEFT", KeyEvent.VK_LEFT, false, () -> pressedKeys.remove(KeyEvent.VK_LEFT));
    bindKey(im, am, "released RIGHT", KeyEvent.VK_RIGHT, false, () -> pressedKeys.remove(KeyEvent.VK_RIGHT));
    }

    private void bindKey(InputMap im, ActionMap am, String name, int key, boolean pressed, Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0, !pressed), name);
        am.put(name, new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                action.run();
            }
        });
    }

    private static final double MOVE_SPEED = 2.0;

    private void handleInput() {
        double dx = 0;
        double dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_LEFT))  dx -= 1;
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) dx += 1;
        if (pressedKeys.contains(KeyEvent.VK_UP))    dy -= 1;
        if (pressedKeys.contains(KeyEvent.VK_DOWN))  dy += 1;

        // Normalizza il vettore per garantire velocità costante
        double magnitude = Math.hypot(dx, dy); // meglio di sqrt(x^2 + y^2)
        if (magnitude > 0) {
            dx = (dx / magnitude) * MOVE_SPEED;
            dy = (dy / magnitude) * MOVE_SPEED;
        }

        bald.setSpeedX(dx);
        bald.setSpeedY(dy);
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        System.out.println("Game loop started!");

        long lastTime = System.nanoTime();
        double interval = 1000000000/60;
        int drawCount = 0;
        long timer = 0;
        double delta = 0;

        while (true) {

            long now = System.nanoTime();
            timer += (now - lastTime);
            delta += (now - lastTime) / interval;
            lastTime = now;

            if(delta >= 1) {

                update();
                repaint();
                delta--;
                drawCount++; //check FPS
            }

            if(timer >= 1000000000) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }   
  
        }
    }
    
    private void update() {

        handleInput();
        bald.move();
        dummyenemy.followPlayer(bald);
        dummyenemy.updateAnimation();
        repaint();
        System.out.printf("dx: %.3f dy: %.3f%n", bald.getSpeedX(), bald.getSpeedY());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.scaleGraphics(g);

        tileMap.render(g);           
        gridPanel.paintComponent(g); 
        bald.render(g);              
        dummyenemy.render(g);        
    }

    private void scaleGraphics(Graphics g) {
        double scaleX = this.getWidth() / ((View) window).getInternalSize().getWidth();
        double scaleY = this.getHeight() / ((View) window).getInternalSize().getHeight();
        ((Graphics2D) g).scale(scaleX, scaleY);
    }
}

