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

import com.thelegendofbald.api.panels.LifePanel;
import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.api.settingsmenu.KeybindsSettings;
import com.thelegendofbald.api.views.View;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.characters.Entity;

public class GamePanel extends MenuPanel {

    private static final long ATTACK_COOLDOWN = 1000; // 1 second cooldown for attack
    private final Bald bald = new Bald(60, 60, 100, "Bald", 50);
    private final DummyEnemy dummyenemy = new DummyEnemy(500, 200, 50, "ZioBilly", 60);
    private final GridPanel gridPanel;
    private final TileMap tileMap;
    private final LifePanel lifePanel;

    Timer timer = new Timer(16, e -> update());
    private final Set<Integer> pressedKeys = new HashSet<>();
    private long lastTimeAttack = 0;


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

        this.lifePanel = new LifePanel(new Dimension(200,20), bald.getLifeComponent());
        this.lifePanel.setBounds(100, 800, 200,20);
        this.add(lifePanel);

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

    boolean isNear(Entity target) {
        int distanceX = Math.abs(bald.getX() - target.getX());
        int distanceY = Math.abs(bald.getY() - target.getY());
        return distanceX < 50 && distanceY < 50; // Adjust the threshold as needed
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
        this.bald.takeDamage(10);
        this.lifePanel.paint(g);    
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

