package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.thelegendofbald.api.views.MainView;
import com.thelegendofbald.api.views.View;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.item.Chest;
import com.thelegendofbald.item.Potion;

public class GamePanel extends JPanel {

    private final Bald bald = new Bald(60, 60, 100, "Bald", 50);
    private final DummyEnemy dummyenemy = new DummyEnemy(500, 200, 50, "ZioBilly", 50);
    private final Chest chest = new Chest(60,180);
    private final Potion potion = new Potion(100,100);
    private final GridPanel gridPanel;
    private final TileMap tileMap;

    Timer timer = new Timer(16, e -> update());
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

        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                handleInput();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                handleInput();
            }
        });
    }

    private void handleInput() {
        bald.updateAnimation();
        bald.setSpeedX(pressedKeys.contains(KeyEvent.VK_RIGHT) ? 5 :
                       pressedKeys.contains(KeyEvent.VK_LEFT) ? -5 : 0);
        bald.setSpeedY(pressedKeys.contains(KeyEvent.VK_DOWN) ? 5 :
                       pressedKeys.contains(KeyEvent.VK_UP) ? -5 : 0);
        if(pressedKeys.contains(KeyEvent.VK_E)){
            this.chest.setChestOpen();
        }
    }

    

    private void update() {

        handleInput();
        bald.move();
        dummyenemy.followPlayer(bald);
        dummyenemy.updateAnimation();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.scaleGraphics(g);

        tileMap.render(g);           
        gridPanel.paintComponent(g); 
        bald.render(g);              
        dummyenemy.render(g);    
        chest.render(g);   
        potion.render(g); 
    }

    private void scaleGraphics(Graphics g) {
        double scaleX = this.getWidth() / ((View) window).getInternalSize().getWidth();
        double scaleY = this.getHeight() / ((View) window).getInternalSize().getHeight();
        ((Graphics2D) g).scale(scaleX, scaleY);
    }
}

