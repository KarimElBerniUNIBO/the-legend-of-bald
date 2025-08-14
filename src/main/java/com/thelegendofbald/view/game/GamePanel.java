package com.thelegendofbald.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
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
import com.thelegendofbald.item.weapons.FireBall;
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

    private static final long serialVersionUID = 1L;

    private static final double OPTIONS_WIDTH_INSETS = 0.25;
    private static final double OPTIONS_HEIGHT_INSETS = 0.1;

    private static final double INVENTORY_WIDTH_INSETS = 0.25;
    private static final double INVENTORY_HEIGHT_INSETS = 0.25;

    private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);

    private static final Pair<Integer, Integer> FPS_POSITION = Pair.of(15, 25);
    private static final Pair<Integer, Integer> TIMER_POSITION = Pair.of(1085, 25);

    private static final Color ATTACK_AREA_COLOR = new Color(200, 200, 200, 100);

    private transient final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints optionsGBC = gbcFactory.createBothGridBagConstraints();
    private final GridBagConstraints inventoryGBC = gbcFactory.createBothGridBagConstraints();

    private transient final Bald bald = new Bald(60, 60, 100, "Bald", 50);

    private String currentMapName = "map_1";
    private final GridPanel gridPanel;
    private transient final TileMap tileMap;
    private final LifePanel lifePanel;
    private transient List<DummyEnemy> enemies = new ArrayList<>();
    private final JPanel optionsPanel;
    private transient final Timer timer = new Timer();
    private transient GameRun gameRun;
    private transient final CombatManager combatManager;
    private transient final DataManager saveDataManager = new DataManager();

    private final JPanel inventoryPanel;
    private transient final Inventory inventory;

    private int num_enemies = 3;

    private transient Thread gameThread;
    private volatile boolean running = false;

    private volatile int maxFPS = (int) VideoSettings.FPS.getValue();
    private volatile boolean showingFPS = (boolean) VideoSettings.SHOW_FPS.getValue();
    private volatile int currentFPS = 0;
    private volatile boolean showingTimer = (boolean) VideoSettings.SHOW_TIMER.getValue();

    private final Set<Integer> pressedKeys = new HashSet<>();

    boolean paused;

    public GamePanel() {
        super();
        Dimension size = new Dimension(1280, 704);

        this.gridPanel = new GridPanel();
        this.gridPanel.setOpaque(false);
        this.gridPanel.setBounds(0, 0, size.width, size.height);

        this.lifePanel = new LifePanel(new Dimension(200,20), bald.getLifeComponent());
        this.lifePanel.setBounds(100, 800, 200,20);

        this.optionsPanel = new GameOptionsPanel();
        this.inventoryPanel = new InventoryPanel("INVENTORY", 5, 3);
        this.inventory = ((InventoryPanel) this.inventoryPanel).getInventory();
        this.inventory.setBald(bald);

        this.tileMap = new TileMap(size.width, size.height, 32);

        this.combatManager = new CombatManager(bald, enemies);
        this.bald.setWeapon(new FireBall(0, 0, 50, 50, combatManager));

        /*JButton shopButton = new JButton("Shop");
        shopButton.setBounds(100, 100, 120, 40);
        shopButton.setVisible(true);
        shopButton.setBackground(Color.YELLOW);
        shopButton.setOpaque(true);
        shopButton.addActionListener(e -> {
            ShopPanel shopPanel = new ShopPanel();
            JOptionPane.showMessageDialog(this, shopPanel, "Negozio", JOptionPane.PLAIN_MESSAGE);
        });
        this.add(shopButton);*/

        tileMap.changeMap("map_1");
        bald.setTileMap(tileMap);

        Point spawnPoint = tileMap.findSpawnPoint(5);
        if (spawnPoint != null) {
            int tileSize = tileMap.TILE_SIZE;
            bald.setX(spawnPoint.x + (tileSize - bald.getWidth()) / 2);
            bald.setY(spawnPoint.y - bald.getHeight());
        }

        this.addWeaponsToInventory();

        for (int i = 0 ; i < num_enemies ; i++) {
            enemies.add(new DummyEnemy(ThreadLocalRandom.current().nextInt(300, 1000), // x
                                       ThreadLocalRandom.current().nextInt(300, 600),  // y
                                       60, "ZioBilly", 10));
        }

        setupKeyBindings();
        this.initialize();


    }


    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setBackground(Color.BLACK);
            this.setFocusable(true);
            this.setLayout(new GridBagLayout());



        });
    }


    private void addWeaponsToInventory() {
        List<Weapon> weapons = List.of(new FireBall(0, 0, 50, 50, combatManager),
                                       new Sword(0, 0, 50, 50, combatManager),
                                       new Axe(0, 0, 50, 50, combatManager));

        weapons.forEach(inventory::add);
    }

    private void setupKeyBindings() {
        final InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        final ActionMap am = this.getActionMap();

        // Tasti premuti
        bindKey(im, am, "pressed UP", ControlsSettings.UP.getKey(), true, () -> pressedKeys.add(KeyEvent.VK_UP));
        bindKey(im, am, "pressed DOWN", ControlsSettings.DOWN.getKey(), true, () -> pressedKeys.add(KeyEvent.VK_DOWN));
        bindKey(im, am, "pressed LEFT", ControlsSettings.LEFT.getKey(), true, () -> pressedKeys.add(KeyEvent.VK_LEFT));
        bindKey(im, am, "pressed RIGHT", ControlsSettings.RIGHT.getKey(), true, () -> pressedKeys.add(KeyEvent.VK_RIGHT));
        bindKey(im, am, "pressed ESCAPE", KeyEvent.VK_ESCAPE, true, () -> {
            if (paused && !inventoryPanel.isVisible()) {
                closeOptionsPanel();
            } else if (!inventoryPanel.isVisible()) {
                openOptionsPanel();
            } else {
                inventoryPanel.setVisible(false);
                openOptionsPanel();
            }
        });
        bindKey(im, am, "pressed SPACE", ControlsSettings.ATTACK.getKey(), true, combatManager::tryToAttack);
        bindKey(im, am, "pressed E", ControlsSettings.INVENTORY.getKey(), true, () -> {
            inventoryPanel.setVisible(!inventoryPanel.isVisible());
        });

        // Tasti rilasciati
        bindKey(im, am, "released UP", ControlsSettings.UP.getKey(), false, () -> pressedKeys.remove(KeyEvent.VK_UP));
        bindKey(im, am, "released DOWN", ControlsSettings.DOWN.getKey(), false, () -> pressedKeys.remove(KeyEvent.VK_DOWN));
        bindKey(im, am, "released LEFT", ControlsSettings.LEFT.getKey(), false, () -> pressedKeys.remove(KeyEvent.VK_LEFT));
        bindKey(im, am, "released RIGHT", ControlsSettings.RIGHT.getKey(), false, () -> pressedKeys.remove(KeyEvent.VK_RIGHT));
    }

    private void bindKey(final InputMap im, final ActionMap am, final String name, final int key,
                         final boolean pressed, final Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0, !pressed), name);
        am.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                action.run();
            }
        });
    }

    private void handleInput() {
        double dx = 0;
        double dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_LEFT)) { dx -= 1; }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) { dx += 1; }
        if (pressedKeys.contains(KeyEvent.VK_UP)) { dy -= 1; }
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) { dy += 1; }

        if (pressedKeys.contains(ControlsSettings.ATTACK.getKey())) { combatManager.tryToAttack(); }

        final double magnitude = Math.hypot(dx, dy);
        if (magnitude > 0) {
            dx /= magnitude;
            dy /= magnitude;
        }

        if (dx > 0) {
            bald.setFacingRight(true);
        } else if (dx < 0) {
            bald.setFacingRight(false);
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
        try {
            saveDataManager.saveGameRun(gameRun);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stopGame();
    }

    /**
     * Esegue il ciclo principale del gioco.
     * Gestisce il calcolo del delta time, aggiornamento dello stato e FPS.
     */
    @Override
    public void run() {
        final long NANOS_IN_SECOND = 1_000_000_000L;
        final long MILLIS_IN_SECOND = 1000L;
        final long SLEEP_INTERVAL_WHEN_PAUSED = 100L;
        final int DEFAULT_MAX_FPS = 60;

        long lastTime = System.nanoTime();
        int frames = 0;
        long fpsTimer = System.currentTimeMillis();

        while (gameThread != null) {
            if (paused) {
                try {
                    Thread.sleep(SLEEP_INTERVAL_WHEN_PAUSED);
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                lastTime = System.nanoTime();
                continue;
            }

            final long now = System.nanoTime();
            final double deltaTime = (now - lastTime) / (double) NANOS_IN_SECOND;
            lastTime = now;

            update(deltaTime);
            repaint();

            frames++;
            if (System.currentTimeMillis() - fpsTimer >= MILLIS_IN_SECOND) {
                currentFPS = frames;
                frames = 0;
                fpsTimer += MILLIS_IN_SECOND;
            }

            final long frameTime = (System.nanoTime() - now) / 1_000_000; // in ms
            final long targetFrameTime = MILLIS_IN_SECOND / DEFAULT_MAX_FPS;
            final long sleepTime = targetFrameTime - frameTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void update(double deltatime) {
        handleInput();
        bald.updateAnimation();
        bald.move(tileMap, deltatime);

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
        if (tileUnderFeet != null && tileUnderFeet.getId() == 4) {
            if (feetY % tileSize == 0) {
                switchToNextMap();
                return;
            }
        }
        combatManager.checkEnemyAttacks();

        enemies.removeIf(enemy -> !enemy.isAlive());
        enemies.forEach(enemy -> {
            if(enemy.isCloseTo(bald)){
                enemy.followPlayer(bald, tileMap, deltatime);
                enemy.updateAnimation();
            }

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
            int x = spawnPoint.x + (tileSize - bald.getWidth()) / 2;
            int y = spawnPoint.y + tileSize - bald.getHeight();
            bald.setX(x);
            bald.setY(y);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            spawnPoint = null;
        } else {
            System.out.println("Spawn point not found!");
        }

        enemies.clear();

        for (int i = 0 ; i < num_enemies ; i++) {
            enemies.add(new DummyEnemy(ThreadLocalRandom.current().nextInt(300, 1000), // x
                                       ThreadLocalRandom.current().nextInt(300, 600),  // y
                                       60, "ZioBilly", 10));
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
                Optional.ofNullable(attackArea).ifPresent(atk -> {
                    g2d.setColor(ATTACK_AREA_COLOR);
                    g2d.fill(atk);
                });
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


        // ➤ CREA QUI IL BOTTONE
        JButton shopButton = new JButton("Shop");
        shopButton.setBackground(Color.YELLOW);
        shopButton.setOpaque(true);
        shopButton.setFocusable(false);
        shopButton.addActionListener(e -> {
            ShopPanel shopPanel = new ShopPanel(this.combatManager, bald.getWallet());
            JOptionPane.showMessageDialog(this, shopPanel, "Negozio", JOptionPane.PLAIN_MESSAGE);
        });

        // componente “colla” che assorbe lo spazio in più
        GridBagConstraints fillerGBC = new GridBagConstraints();
        fillerGBC.gridx   = 0;            // prima colonna
        fillerGBC.gridy   = 0;            // prima riga
        fillerGBC.weightx = 1;            // si espande in larghezza
        fillerGBC.weighty = 1;            // si espande in altezza
        fillerGBC.fill    = GridBagConstraints.BOTH;   // riempie la cella
        this.add(Box.createGlue(), fillerGBC);         // puoi usare anche new JPanel()


        // ➤ AGGIUNGI IL BOTTONE CON LE GBC CORRETTE
        GridBagConstraints shopButtonGBC = new GridBagConstraints();
        shopButtonGBC.gridx = 0;
        shopButtonGBC.gridy = 0;
        shopButtonGBC.insets = new Insets(10, 10, 10, 10);
        shopButtonGBC.anchor = GridBagConstraints.SOUTH;
        shopButtonGBC.fill = GridBagConstraints.NONE;
        shopButtonGBC.weightx = 0;
        shopButtonGBC.weighty = 0;

        this.add(shopButton, shopButtonGBC);
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

    public void pauseGame() {
        this.paused = true;
    }

    public void resumeGame() {
        this.paused = false;
    }

    private void openOptionsPanel() {
        pauseGame();
        pressedKeys.clear();
        this.optionsPanel.setVisible(true);
        this.optionsPanel.requestFocusInWindow();
        this.repaint();
    }

    public void closeOptionsPanel() {
        this.optionsPanel.setVisible(false);
        pressedKeys.clear();
        resumeGame();
        this.requestFocusInWindow();
        this.repaint();
    }
}
