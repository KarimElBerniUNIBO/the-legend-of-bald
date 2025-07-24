package com.thelegendofbald.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.game.Game;
import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.api.panels.LifePanel;
import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.api.settingsmenu.ControlsSettings;
import com.thelegendofbald.api.settingsmenu.VideoSettings;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.combat.projectile.Projectile;
import com.thelegendofbald.item.weapons.Axe;
import com.thelegendofbald.item.weapons.Magic;
import com.thelegendofbald.item.weapons.Sword;
import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.common.DataManager;
import com.thelegendofbald.model.common.GameRun;
import com.thelegendofbald.model.common.Timer;
import com.thelegendofbald.model.common.Timer.TimeData;
import com.thelegendofbald.model.weapons.MeleeWeapon;
import com.thelegendofbald.model.weapons.Weapon;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.view.inventory.InventoryPanel;
import com.thelegendofbald.view.main.GameWindow;
import com.thelegendofbald.view.main.GridPanel;
import com.thelegendofbald.view.main.ShopPanel;
import com.thelegendofbald.view.main.Tile;
import com.thelegendofbald.view.main.TileMap;

public class GamePanel extends MenuPanel implements Runnable, Game {

    private static final double OPTIONS_WIDTH_INSETS = 0.25;
    private static final double OPTIONS_HEIGHT_INSETS = 0.1;

    private static final double INVENTORY_WIDTH_INSETS = 0.25;
    private static final double INVENTORY_HEIGHT_INSETS = 0.25;

    private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);

    private static final Pair<Integer, Integer> FPS_POSITION = Pair.of(15, 25);
    private static final Pair<Integer, Integer> TIMER_POSITION = Pair.of(1085, 25);

    private static final Color ATTACK_AREA_COLOR = new Color(200, 200, 200, 100);

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints optionsGBC = gbcFactory.createBothGridBagConstraints();
    private final GridBagConstraints inventoryGBC = gbcFactory.createBothGridBagConstraints();

    private final Bald bald = new Bald(60, 60, 100, "Bald", 50);
    private final DummyEnemy dummyenemy = new DummyEnemy(500, 200, 50, "ZioBilly", 50);

    private String currentMapName = "map_1";
    private final GridPanel gridPanel;
    private final TileMap tileMap;
    private final LifePanel lifePanel;
    private List<DummyEnemy> enemies = new ArrayList<>();
    private final JPanel optionsPanel;
    private final Timer timer = new Timer();
    private GameRun gameRun;
    private final CombatManager combatManager;
    private final DataManager saveDataManager = new DataManager();

    private final JPanel inventoryPanel;
    private final Inventory inventory;

    private int num_enemies = 3;

    private Thread gameThread;
    private boolean running = false;

    private int maxFPS = (int) VideoSettings.FPS.getValue();
    private boolean showingFPS = (boolean) VideoSettings.SHOW_FPS.getValue();
    private int currentFPS = 0;
    private boolean showingTimer = (boolean) VideoSettings.SHOW_TIMER.getValue();

    private final Set<Integer> pressedKeys = new HashSet<>();

    public GamePanel() {
        super();
        Dimension size = new Dimension(1280, 704);
        //this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(new GridBagLayout());

        this.gridPanel = new GridPanel();
        this.gridPanel.setOpaque(false);
        this.gridPanel.setBounds(0, 0, size.width, size.height);

        this.lifePanel = new LifePanel(new Dimension(200,20), bald.getLifeComponent());
        this.lifePanel.setBounds(100, 800, 200,20);

        this.optionsPanel = new GameOptionsPanel(size);
        this.inventoryPanel = new InventoryPanel("INVENTORY", size, 5, 3);
        this.inventory = ((InventoryPanel) this.inventoryPanel).getInventory();
        this.inventory.setBald(bald);

        this.tileMap = new TileMap(size.width, size.height, 32);

        this.combatManager = new CombatManager(bald, enemies);
        this.bald.setWeapon(new Magic(0, 0, 50, 50, combatManager));

        JButton shopButton = new JButton("Shop");
        shopButton.setBounds(100, 100, 120, 40);
        shopButton.setVisible(true);
        shopButton.setBackground(Color.YELLOW);
        shopButton.setOpaque(true);
        shopButton.addActionListener(e -> {
            ShopPanel shopPanel = new ShopPanel();
            JOptionPane.showMessageDialog(this, shopPanel, "Negozio", JOptionPane.PLAIN_MESSAGE);
        });
        this.add(shopButton);

        tileMap.changeMap("map_1");
        bald.setTileMap(tileMap);

        Point spawnPoint = tileMap.findSpawnPoint(5);
        if (spawnPoint != null) {
            int tileSize = tileMap.TILE_SIZE;
            bald.setX(spawnPoint.x + (tileSize - bald.getWidth()) / 2);
            bald.setY(spawnPoint.y - bald.getHeight());
        }

        this.addWeaponsToInventory();

        this.requestFocusInWindow();

        for (int i = 0 ; i < num_enemies ; i++) {
            enemies.add(new DummyEnemy(ThreadLocalRandom.current().nextInt(300, 1000), // x
                                       ThreadLocalRandom.current().nextInt(300, 600),  // y
                                       60, "ZioBilly", 10));
        }

        setupKeyBindings();
        // this.startGame();

        SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
    }

    private void addWeaponsToInventory() {
        List<Weapon> weapons = List.of(new Magic(0, 0, 50, 50, combatManager),
                                       new Sword(0, 0, 50, 50, combatManager),
                                       new Axe(0, 0, 50, 50, combatManager));

        weapons.forEach(inventory::add);
    }

    private void setupKeyBindings() {
        InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        // Tasti premuti
        bindKey(im, am, "pressed UP", KeyEvent.VK_UP, true, () -> pressedKeys.add(KeyEvent.VK_UP));
        bindKey(im, am, "pressed DOWN", KeyEvent.VK_DOWN, true, () -> pressedKeys.add(KeyEvent.VK_DOWN));
        bindKey(im, am, "pressed LEFT", KeyEvent.VK_LEFT, true, () -> pressedKeys.add(KeyEvent.VK_LEFT));
        bindKey(im, am, "pressed RIGHT", KeyEvent.VK_RIGHT, true, () -> pressedKeys.add(KeyEvent.VK_RIGHT));
        bindKey(im, am, "pressed SPACE", ControlsSettings.ATTACK.getKey(), true, combatManager::tryToAttack);

        // Tasti rilasciati
        bindKey(im, am, "released UP", KeyEvent.VK_UP, false, () -> pressedKeys.remove(KeyEvent.VK_UP));
        bindKey(im, am, "released DOWN", KeyEvent.VK_DOWN, false, () -> pressedKeys.remove(KeyEvent.VK_DOWN));
        bindKey(im, am, "released LEFT", KeyEvent.VK_LEFT, false, () -> pressedKeys.remove(KeyEvent.VK_LEFT));
        bindKey(im, am, "released RIGHT", KeyEvent.VK_RIGHT, false, () -> pressedKeys.remove(KeyEvent.VK_RIGHT));
    }

    private void bindKey(InputMap im, ActionMap am, String name, int key, boolean pressed, Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0, !pressed), name);
        am.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                action.run();
            }
        });
    }

    private static final double MOVE_SPEED = 2.0;

    private void handleInput() {
        double dx = 0;
        double dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_LEFT))
            dx -= 1;
        if (pressedKeys.contains(KeyEvent.VK_RIGHT))
            dx += 1;
        if (pressedKeys.contains(KeyEvent.VK_UP))
            dy -= 1;
        if (pressedKeys.contains(KeyEvent.VK_DOWN))
            dy += 1;

        if (pressedKeys.contains(ControlsSettings.ATTACK.getKey())) {
            combatManager.tryToAttack();
        }

        // Normalizza il vettore per garantire velocità costante
        double magnitude = Math.hypot(dx, dy); // meglio di sqrt(x^2 + y^2)
        if (magnitude > 0) {
            dx = (dx / magnitude) * MOVE_SPEED;
            dy = (dy / magnitude) * MOVE_SPEED;
        }

        bald.setSpeedX(dx);
        bald.setSpeedY(dy);
    }

    boolean intersects(Combatant e1, Combatant e2) {
        return e1.getBounds().intersects(e2.getBounds());
    }

    public GameRun getGameRun() {
        return gameRun;
    }

    private void setPlayerName() {
        String nickname = "";

        // TODO: Mettere in pausa il gioco e mostrare un dialogo per inserire il nome

        while (Optional.ofNullable(nickname).isEmpty() || nickname.isBlank()) {
            nickname = javax.swing.JOptionPane.showInputDialog("Enter your nickname:");
        }
        gameRun = new GameRun(nickname, timer.getFormattedTime());

        System.out.println("Game started with player nickname: " + gameRun.name() + " at time: " + gameRun.timedata());
        // TODO: Continua il gioco
    }

    @Override
    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
        timer.start();
        this.setPlayerName();
    }

    @Override
    public void finishGame() {
        gameRun = new GameRun(gameRun.name(), timer.getFormattedTime());
        saveDataManager.saveGameRun(gameRun);
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Game loop started!");

        long lastTime = System.nanoTime();
        double interval = 0;
        int drawCount = 0;
        long updateInterval = 0;
        double delta = 0;

        while (true) {

            long now = System.nanoTime();
            updateInterval += (now - lastTime);
            interval = 1e9 / maxFPS;
            delta += (now - lastTime) / interval;
            lastTime = now;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++; // check FPS
            }

            if (updateInterval >= 1e9) {
                // System.out.println("FPS:" + drawCount);
                currentFPS = drawCount;
                drawCount = 0;
                updateInterval = 0;
            }

        }
    }

    private void update() {
        handleInput();
        bald.updateAnimation();
        bald.move();

        dummyenemy.followPlayer(bald);
        dummyenemy.updateAnimation();

        int baldX = bald.getX();
        int baldY = bald.getY();
        int baldW = bald.getWidth();
        int baldH = bald.getHeight();
        int tileSize = tileMap.TILE_SIZE;

        // Calcola la posizione dei piedi di Bald
        int feetY = baldY + baldH;
        int tileFeetY = feetY / tileSize;
        int tileCenterX = (baldX + baldW / 2) / tileSize;

        // --- LOGICA CAMBIO MAPPA ---
        Tile tileUnderFeet = tileMap.getTileAt(tileCenterX, tileFeetY);
        System.out.println("Tile sotto i piedi: " + (tileUnderFeet != null ? tileUnderFeet.getId() : "null") + " at (" + tileCenterX + "," + tileFeetY + ")");
        if (tileUnderFeet != null && tileUnderFeet.getId() == 4) {
            if (feetY % tileSize == 0) {
                switchToNextMap();
                return;
            }
        }
        combatManager.checkEnemyAttacks();

        enemies.removeIf(enemy -> !enemy.isAlive());
        enemies.forEach(enemy -> {
            enemy.followPlayer(bald);
            enemy.updateAnimation();
        });

        combatManager.getProjectiles().forEach(Projectile::move);
        combatManager.checkProjectiles();

        repaint();
    }

    public void changeMap(String mapName) {
        currentMapName = mapName;
        tileMap.changeMap(mapName);
        bald.setTileMap(tileMap);

        Point spawnPoint = tileMap.findSpawnPoint(5);
        if (spawnPoint != null) {
            int tileSize = tileMap.TILE_SIZE;
            bald.setX(spawnPoint.x + (tileSize - bald.getWidth()) / 2);
            bald.setY(spawnPoint.y + tileSize - bald.getHeight());
        } else {
            System.out.println("Spawn point not found!");
        }
    }

    private void switchToNextMap() {
        if (currentMapName.equals("map_1")) {
            changeMap("map_2");
        } else if (currentMapName.equals("map_2")) {
            changeMap("map_3");
        } else {
            System.out.println("Nessuna mappa successiva definita.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        super.paintComponent(g2d);

        this.scaleGraphics(g2d);
        tileMap.paint(g2d);
        gridPanel.paintComponent(g2d);
        bald.render(g2d);
        enemies.forEach(enemy -> enemy.render(g2d));
        this.combatManager.getProjectiles().forEach(p -> p.render(g2d));   
        this.lifePanel.paintComponent(g2d);
        this.drawFPS(g2d);
        this.drawTimer(g2d);
        this.drawAttackArea(g2d);

        g2d.dispose();
    }

    private void drawAttackArea(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        bald.getWeapon().ifPresent(weapon -> {
            if (bald.isAttacking() && weapon instanceof MeleeWeapon) {
                Arc2D attackArea = ((MeleeWeapon) weapon).getAttackArea();
                g2d.setColor(ATTACK_AREA_COLOR);
                Optional.ofNullable(attackArea).ifPresent(g2d::fill);
            }
        });
    }

    private void drawFPS(Graphics g) {
        if (showingFPS) {
            g.setColor(Color.YELLOW);
            g.setFont(DEFAULT_FONT);
            g.drawString("FPS: " + currentFPS, FPS_POSITION.getLeft(), FPS_POSITION.getRight());
        }
    }

    private void drawTimer(Graphics g) {
        if (showingTimer) {
            TimeData timeData = timer.getFormattedTime();

            g.setColor(Color.WHITE);
            g.setFont(DEFAULT_FONT);

            g.drawString(
                    String.format("Timer: %02d:%02d:%02d", timeData.hours(), timeData.minutes(), timeData.seconds()),
                    TIMER_POSITION.getLeft(), TIMER_POSITION.getRight());
        }
    }

    private void scaleGraphics(Graphics g) {
        Optional.ofNullable(SwingUtilities.getWindowAncestor(this))
                .filter(window -> window instanceof GameWindow)
                .map(window -> (GameWindow) window)
                .ifPresent(window -> {
                    double scaleX = this.getWidth() / window.getInternalSize().getWidth();
                    double scaleY = this.getHeight() / window.getInternalSize().getHeight();
                    ((Graphics2D) g).scale(scaleX, scaleY);
                });
    }

    @Override
    public void updateComponentsSize() {
        optionsGBC.insets.set((int) (this.getHeight() * OPTIONS_HEIGHT_INSETS),
                (int) (this.getWidth() * OPTIONS_WIDTH_INSETS), (int) (this.getHeight() * OPTIONS_HEIGHT_INSETS),
                (int) (this.getWidth() * OPTIONS_WIDTH_INSETS));
        inventoryGBC.insets.set((int) (this.getHeight() * INVENTORY_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_WIDTH_INSETS), (int) (this.getHeight() * INVENTORY_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_WIDTH_INSETS));
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        //this.add(gridPanel);
        this.add(optionsPanel, optionsGBC);
        this.add(inventoryPanel, inventoryGBC);
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void stopGame() {
        this.running = false;
    }

    @Override
    public void setFPS(int fps) {
        this.maxFPS = fps;
    }

    @Override
    public void setShowingFPS(boolean showingFPS) {
        this.showingFPS = showingFPS;
    }

    public boolean isShowingFPS() {
        return showingFPS;
    }

    public void setShowingTimer(boolean showingTimer) {
        this.showingTimer = showingTimer;
    }

    public boolean isShowingTimer() {
        return showingTimer;
    }

}
