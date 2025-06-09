package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.api.settingsmenu.KeybindsSettings;
import com.thelegendofbald.api.views.View;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.item.Chest;
import com.thelegendofbald.item.Potion;

public class GamePanel extends MenuPanel {

    private final Bald bald = new Bald(60, 60, 100, "Bald", 50);
    private final DummyEnemy dummyenemy = new DummyEnemy(500, 200, 50, "ZioBilly", 50);
    private final Chest chest = new Chest(60,180);
    private final Potion potion = new Potion(100,100);
    private final GridPanel gridPanel;
    private final TileMap tileMap;

    Timer timer = new Timer(16, e -> update());
    private final Set<Integer> pressedKeys = new HashSet<>();


    public GamePanel() {
        super();
        Dimension size = new Dimension(1280, 704);
        //this.setPreferredSize(size);
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
        bald.setSpeedX(pressedKeys.contains(KeybindsSettings.RIGHT.getKey()) ? 5 :
                       pressedKeys.contains(KeybindsSettings.LEFT.getKey()) ? -5 : 0);
        bald.setSpeedY(pressedKeys.contains(KeybindsSettings.DOWN.getKey()) ? 5 :
                       pressedKeys.contains(KeybindsSettings.UP.getKey()) ? -5 : 0);
     
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
        double scaleX = this.getWidth() / ((View) SwingUtilities.getWindowAncestor(this)).getInternalSize().getWidth();
        double scaleY = this.getHeight() / ((View) SwingUtilities.getWindowAncestor(this)).getInternalSize().getHeight();
        ((Graphics2D) g).scale(scaleX, scaleY);
    }

    @Override
    public void updateComponentsSize() {
    }

    @Override
    public void addComponentsToPanel() {
    }
}

