package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
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
import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.combat.projectile.Projectile;

import java.util.concurrent.ThreadLocalRandom;

public class GamePanel extends MenuPanel {

    private static final long ATTACK_COOLDOWN = 700; // 1 second cooldown for attack
    private final Bald bald = new Bald(60, 60, 100, "Bald", 10);
    
    private final GridPanel gridPanel;
    private final TileMap tileMap;
    private final LifePanel lifePanel;
    private List<DummyEnemy> enemies = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();

    Timer timer = new Timer(16, e -> update());
    private final Set<Integer> pressedKeys = new HashSet<>();
    private long lastTimeAttack = 0;
    private int num_enemies = 3;

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

        for (int i = 0 ; i < num_enemies ; i++) {
            enemies.add(new DummyEnemy(ThreadLocalRandom.current().nextInt(300, 1000), // x
                                       ThreadLocalRandom.current().nextInt(300, 600),  // y
                                       50, "ZioBilly", 10));
        }

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
        if (pressedKeys.contains(KeybindsSettings.SPACE.getKey())) {
            tryToShoot();
        }
    }

    public void tryToShoot() {
        long now = System.currentTimeMillis(); // tempo attuale

        if (now - lastTimeAttack >= ATTACK_COOLDOWN) {
            System.out.println("Attacco effettuato!");
            lastTimeAttack = now;

            projectiles.add(new Projectile(bald.getX() + 16, bald.getY() + 16, bald.isFacingRight() ? 1 : 0, 10));


        } else {
            System.out.println("Cooldown in corso...");
        }
}

    boolean intersects(Combatant e1, Combatant e2) {
        return e1.getBounds().intersects(e2.getBounds());
    }

    private void update() {

        List<Projectile> toRemoveProjectiles = new ArrayList<>();
        List<Entity> toRemoveEnemies = new ArrayList<>();

        handleInput();
        bald.move();

        for (DummyEnemy enemy : enemies){
            enemy.followPlayer(bald);
            enemy.updateAnimation();
            if (intersects(enemy, bald)){
                    bald.takeDamage(enemy.getAttackPower());
     
            }
        }

        for (Projectile projectile : projectiles) {
            projectile.move(); // Assicurati che il proiettile si muova

            for (DummyEnemy e : enemies) {
                // Usa la posizione del proiettile e del nemico per il controllo collisione
                if (intersects(projectile,e)) {
                    e.takeDamage(projectile.getAttackPower());
                    toRemoveProjectiles.add(projectile); // Rimuovi il proiettile dopo il colpo
                    if (e.getLifeComponent().isDead()) {
                        toRemoveEnemies.add(e);
                    }
                    break; // Un proiettile colpisce solo un nemico
                }  
            }
        }
        projectiles.removeAll(toRemoveProjectiles);
        enemies.removeAll(toRemoveEnemies);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.scaleGraphics(g);

        tileMap.render(g); 
        gridPanel.paintComponent(g);          
        bald.render(g);              
        for (DummyEnemy dummyenemy : enemies) {
            dummyenemy.render(g);
        }  
        for (Projectile p : projectiles) {
            p.render(g);
        }    
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

