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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
import com.thelegendofbald.api.interactable.Interactable;
import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.api.panels.LifePanel;
import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.api.panels.Panels;
import com.thelegendofbald.api.settingsmenu.ControlsSettings;
import com.thelegendofbald.api.settingsmenu.VideoSettings;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.controller.common.SwitchToOtherPanel;
import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.common.DataManager;
import com.thelegendofbald.model.common.GameRun;
import com.thelegendofbald.model.common.Timer;
import com.thelegendofbald.model.common.Timer.TimeData;
import com.thelegendofbald.model.item.ItemFactory;
import com.thelegendofbald.model.item.ItemManager;
import com.thelegendofbald.model.item.loot.LootGenerator;
import com.thelegendofbald.model.item.map.MapItemLoader;
import com.thelegendofbald.model.item.weapons.Axe;
import com.thelegendofbald.model.item.weapons.FireBall;
import com.thelegendofbald.model.item.weapons.Sword;
import com.thelegendofbald.model.weapons.MeleeWeapon;
import com.thelegendofbald.model.weapons.Weapon;
import com.thelegendofbald.utils.LoggerUtils;
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
    private final Map<String, String> mapTransitions = Map.of(
            "map_1", "map_2",
            "map_2", "map_3"
    );

    private final GridPanel gridPanel;
    private transient final TileMap tileMap;
    private transient ItemManager itemManager;
    private transient LootGenerator lootGenerator;

    private final LifePanel lifePanel;
    private transient List<DummyEnemy> enemies = new ArrayList<>();
    private final JPanel optionsPanel;
    private transient final Timer timer = new Timer();
    private transient GameRun gameRun;
    private transient final CombatManager combatManager;
    private transient final DataManager saveDataManager = new DataManager();

    private final JPanel inventoryPanel;
    private transient final Inventory inventory;

    private transient Thread gameThread;
    private volatile boolean running = false;

    private volatile int maxFPS = 60;
    private volatile boolean showingFPS = (boolean) VideoSettings.SHOW_FPS.getValue();
    private volatile int currentFPS = 0;
    private volatile boolean showingTimer = (boolean) VideoSettings.SHOW_TIMER.getValue();

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final JButton shopButton = new JButton("Shop");

    private volatile boolean paused;
    private static final long NANOS_IN_SECOND = 1_000_000_000L;
    private static final long MILLIS_IN_SECOND = 1000L;
    private static final long SLEEP_INTERVAL_WHEN_PAUSED = 100L;

    public GamePanel() {
        super();
        Dimension size = new Dimension(1280, 704);

        this.gridPanel = new GridPanel();
        this.gridPanel.setOpaque(false);
        this.gridPanel.setBounds(0, 0, size.width, size.height);

        this.lifePanel = new LifePanel(new Dimension(200, 20), bald.getLifeComponent());
        this.lifePanel.setBounds(100, 800, 200, 20);

        this.optionsPanel = new GameOptionsPanel();
        this.inventoryPanel = new InventoryPanel("INVENTORY", 5, 3);
        this.inventory = ((InventoryPanel) this.inventoryPanel).getInventory();
        this.inventory.setBald(bald);

        this.tileMap = new TileMap(size.width, size.height, 32);
        this.combatManager = new CombatManager(bald, enemies);
        this.bald.setWeapon(new FireBall(0, 0, 50, 50, combatManager));

        // Inizializzazione LootGenerator con pool di item possibili
        this.lootGenerator = new LootGenerator(
                new ItemFactory(),
                List.of(7, 8, 11, 12) // ID loot possibili
        );

        // Inizializzazione ItemManager con loot
        this.itemManager = new ItemManager(tileMap, new ItemFactory(), new MapItemLoader(), lootGenerator);
        this.itemManager.loadItemsForMap("map_1");

        tileMap.changeMap("map_1");
        bald.setTileMap(tileMap);

        Point spawnPoint = tileMap.findSpawnPoint(5);
        if (spawnPoint != null) {
            int tileSize = tileMap.TILE_SIZE;
            bald.setPosX(spawnPoint.x + (tileSize - bald.getWidth()) / 2);
            bald.setPosY(spawnPoint.y - bald.getHeight());
        }

        this.addWeaponsToInventory();


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
        List<Weapon> weapons = List.of(
                new FireBall(0, 0, 50, 50, combatManager),
                new Sword(0, 0, 50, 50, combatManager),
                new Axe(0, 0, 50, 50, combatManager)
        );
        weapons.forEach(inventory::add);
    }

    private void toggleOptionsPanel() {
        if (paused && !inventoryPanel.isVisible()) {
            closeOptionsPanel();
        } else if (!inventoryPanel.isVisible()) {
            openOptionsPanel();
        } else {
            inventoryPanel.setVisible(false);
            openOptionsPanel();
        }
    }

    private void toggleOpenInventory() {
        if (!paused && !optionsPanel.isVisible() && !inventoryPanel.isVisible()) {
            inventoryPanel.setVisible(true);
        } else if (inventoryPanel.isVisible()) {
            inventoryPanel.setVisible(false);
        }
    }

    private void openOptionsPanel() {
        pauseGame();
        pressedKeys.clear();
        this.optionsPanel.setVisible(true);
        this.optionsPanel.requestFocusInWindow();
        this.repaint();
    }

    private void closeOptionsPanel() {
        this.optionsPanel.setVisible(false);
        pressedKeys.clear();
        resumeGame();
        this.requestFocusInWindow();
        this.repaint();
    }

    @Override
    public void pauseGame() {
        this.paused = true;
        this.timer.stop();
    }

    @Override
    public void resumeGame() {
        this.paused = false;
        this.timer.resume();
    }

    public void refreshKeyBindings() {
        pressedKeys.clear();
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        final InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        final ActionMap am = this.getActionMap();

        // Tasti premuti
        bindKey(im, am, "pressed UP", ControlsSettings.UP.getKey(), true, () -> pressedKeys.add(ControlsSettings.UP.getKey()));
        bindKey(im, am, "pressed DOWN", ControlsSettings.DOWN.getKey(), true, () -> pressedKeys.add(ControlsSettings.DOWN.getKey()));
        bindKey(im, am, "pressed LEFT", ControlsSettings.LEFT.getKey(), true, () -> pressedKeys.add(ControlsSettings.LEFT.getKey()));
        bindKey(im, am, "pressed RIGHT", ControlsSettings.RIGHT.getKey(), true, () -> pressedKeys.add(ControlsSettings.RIGHT.getKey()));
        bindKey(im, am, "pressed ESCAPE", KeyEvent.VK_ESCAPE, true, this::toggleOptionsPanel);
        bindKey(im, am, "pressed SPACE", ControlsSettings.ATTACK.getKey(), true, combatManager::tryToAttack);
        bindKey(im, am, "pressed I", ControlsSettings.INVENTORY.getKey(), true, this::toggleOpenInventory);
        bindKey(im, am, "interact", ControlsSettings.INTERACT.getKey(), true, this::interactWithItems);

        // Tasti rilasciati
        bindKey(im, am, "released UP", ControlsSettings.UP.getKey(), false, () -> pressedKeys.remove(ControlsSettings.UP.getKey()));
        bindKey(im, am, "released DOWN", ControlsSettings.DOWN.getKey(), false, () -> pressedKeys.remove(ControlsSettings.DOWN.getKey()));
        bindKey(im, am, "released LEFT", ControlsSettings.LEFT.getKey(), false, () -> pressedKeys.remove(ControlsSettings.LEFT.getKey()));
        bindKey(im, am, "released RIGHT", ControlsSettings.RIGHT.getKey(), false, () -> pressedKeys.remove(ControlsSettings.RIGHT.getKey()));
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
        if (bald.isImmobilized()) {
            bald.setSpeedX(0);
            bald.setSpeedY(0);
            return;
        }
        double dx = 0, dy = 0;
        if (pressedKeys.contains(ControlsSettings.LEFT.getKey())) dx -= 1;
        if (pressedKeys.contains(ControlsSettings.RIGHT.getKey())) dx += 1;
        if (!"map_1".equals(currentMapName)) {
            if (pressedKeys.contains(ControlsSettings.UP.getKey())) dy -= 1;
            if (pressedKeys.contains(ControlsSettings.DOWN.getKey())) dy += 1;
        }
        if (pressedKeys.contains(ControlsSettings.ATTACK.getKey())) combatManager.tryToAttack();

        double magnitude = Math.hypot(dx, dy);
        if (magnitude > 0) {
            dx = (dx / magnitude) * MOVE_SPEED;
            dy = (dy / magnitude) * MOVE_SPEED;
        }
        bald.setFacingRight(dx > 0 || (dx == 0 && bald.isFacingRight()));
        bald.setSpeedX(dx);
        bald.setSpeedY(dy);
    }

    private void interactWithItems() {
        itemManager.getItems().stream()
                .filter(item -> bald.getBounds().intersects(item.getBounds()))
                .filter(item -> item instanceof Interactable)
                .map(item -> (Interactable) item)
                .findFirst()
                .ifPresent(interactableItem -> interactableItem.interact(bald));
    }

    public GameRun getGameRun() {
        return gameRun;
    }

    private void setPlayerName() {
        String nickname = "";

        this.pauseGame();
        while (nickname.isBlank()) {
            nickname = JOptionPane.showInputDialog("Enter your nickname:");
            if (Optional.ofNullable(nickname).isEmpty()) {
                this.stopGame();
                GameWindow window = (GameWindow) SwingUtilities.getWindowAncestor(this);
                new SwitchToOtherPanel(window, Panels.MAIN_MENU).actionPerformed(null);
                return;
            }
        }

        gameRun = new GameRun(nickname, timer.getFormattedTime());
        this.resumeGame();
    }

    @Override
    public void startGame() {
        this.running = true;
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
            LoggerUtils.error("Error saving game run: " + e.getMessage());
        }
        this.stopGame();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        int frames = 0;
        long fpsTimer = System.currentTimeMillis();

        while (gameThread != null && running) {
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
            final long targetFrameTime = MILLIS_IN_SECOND / maxFPS;
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

    public void update(double deltatime) {
        handleInput();
        bald.updateAnimation();
        bald.move(tileMap, deltatime);
        bald.updateBuffs();
        if (isAtMapTransitionPoint()) {
            switchToNextMap();
            return;
        }
        combatManager.checkEnemyAttacks();
        enemies.removeIf(enemy -> !enemy.isAlive());
        enemies.forEach(enemy -> {
            if (enemy.isCloseTo(bald)) {
                enemy.followPlayer(bald);
                enemy.updateAnimation();
            }
        });
        combatManager.getProjectiles().forEach(p -> p.move(tileMap));
        combatManager.checkProjectiles();
        itemManager.updateAll();
        itemManager.handleItemCollection(bald);
        checkIfNearShopTile();
        this.revalidate();
        this.repaint();
    }

    private boolean isAtMapTransitionPoint() {
        int baldX = bald.getX();
        int baldY = bald.getY();
        int baldH = bald.getHeight();
        int baldW = bald.getWidth();
        int tileSize = tileMap.TILE_SIZE;
        int feetY = baldY + baldH;
        int tileFeetY = feetY / tileSize;
        int tileCenterX = (baldX + baldW / 2) / tileSize;
        Tile tileUnderFeet = tileMap.getTileAt(tileCenterX, tileFeetY);
        return tileUnderFeet != null && tileUnderFeet.getId() == 4 && (feetY % tileSize == 0);
    }

    private void switchToNextMap() {
        String nextMapName = mapTransitions.get(currentMapName);
        if (nextMapName != null) {
            changeAndLoadMap(nextMapName);
        } else {
            LoggerUtils.error("Nessuna mappa successiva definita.");
        }
    }
    
    
    private void changeAndLoadMap(String mapName) {
    currentMapName = mapName;

    // cambia mappa e aggiorna riferimento nel player
    tileMap.changeMap(mapName);
    bald.setTileMap(tileMap);

    // ► POSIZIONA BALD SUL TILE id == 5 (centrato e "a terra")
    Point spawnPoint = tileMap.findSpawnPoint(5);
    if (spawnPoint != null) {
        int tileSize = tileMap.TILE_SIZE;
        bald.setX(spawnPoint.x + (tileSize - bald.getWidth()) / 2);
        bald.setY(spawnPoint.y + tileSize - bald.getHeight());
    } else {
        System.out.println("⚠️ Nessun tile id==5 trovato in " + mapName);
    }

    spawnEnemiesFromMap(); 

    itemManager.loadItemsForMap(mapName);

    shopButton.setVisible(false);
}

    private void spawnEnemiesFromMap() {
        enemies.clear();
        List<Point> spawns = tileMap.findAllWithId(7);
        int tileSize = tileMap.TILE_SIZE;
        for (Point topLeft : spawns) {
            int enemyW = 60, enemyH = 60;
            int x = topLeft.x + (tileSize - enemyW) / 2;
            int y = topLeft.y + (tileSize - enemyH) / 2;
            enemies.add(new DummyEnemy(x, y, enemyW, "ZioBilly", 10));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        super.paintComponent(g2d);
        this.scaleGraphics(g2d);
        tileMap.paint(g2d);
        gridPanel.paintComponent(g2d);
        itemManager.renderAll(g2d);
        bald.render(g2d);
        enemies.forEach(enemy -> enemy.render(g2d));
        combatManager.getProjectiles().forEach(p -> p.render(g2d));
        lifePanel.paintComponent(g2d);
        drawFPS(g2d);
        drawTimer(g2d);
        drawAttackArea(g2d);
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
                    String.format("Timer: %02d:%02d:%02d",
                            timeData.hours(), timeData.minutes(), timeData.seconds()),
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
                (int) (this.getWidth() * OPTIONS_WIDTH_INSETS),
                (int) (this.getHeight() * OPTIONS_HEIGHT_INSETS),
                (int) (this.getWidth() * OPTIONS_WIDTH_INSETS));
        inventoryGBC.insets.set((int) (this.getHeight() * INVENTORY_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_WIDTH_INSETS),
                (int) (this.getHeight() * INVENTORY_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_WIDTH_INSETS));
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        this.add(optionsPanel, optionsGBC);
        this.add(inventoryPanel, inventoryGBC);

        // ▼ usa il CAMPO 'shopButton' (non crearne uno nuovo)
        shopButton.setText("Shop");
        shopButton.setBackground(Color.YELLOW);
        shopButton.setOpaque(true);
        shopButton.setFocusable(false);
        shopButton.setVisible(false);
        shopButton.addActionListener(e -> {
            ShopPanel shopPanel = new ShopPanel(this.combatManager, bald.getWallet());
            JOptionPane.showMessageDialog(this, shopPanel, "SHOP", JOptionPane.PLAIN_MESSAGE);
        });

        // filler per layout
        GridBagConstraints fillerGBC = new GridBagConstraints();
        fillerGBC.gridx = 0;
        fillerGBC.gridy = 0;
        fillerGBC.weightx = 1;
        fillerGBC.weighty = 1;
        fillerGBC.fill = GridBagConstraints.BOTH;
        this.add(Box.createGlue(), fillerGBC);

        // posizionamento bottone
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

    private void checkIfNearShopTile() {
        final int tileSize = tileMap.TILE_SIZE;

        final int baldX = bald.getX();
        final int baldY = bald.getY();
        final int baldW = bald.getWidth();
        final int baldH = bald.getHeight();

        final int feetY = baldY + baldH;

        // 🔸 campiona 1 px "dentro" il tile dei piedi (evita il bordo superiore)
        final int feetYInside = Math.max(0, feetY - 1);

        final int tileFeetY = feetYInside / tileSize;               // riga corretta del tile sotto i piedi
        final int tileCenterX = (baldX + baldW / 2) / tileSize;     // colonna al centro del personaggio

        final Tile tileUnderFeet = tileMap.getTileAt(tileCenterX, tileFeetY);

        // Mostra se davvero SEI sopra un tile id==6 (niente vincolo di allineamento)
        final boolean onShopTile = tileUnderFeet != null && tileUnderFeet.getId() == 6;

        if (shopButton.isVisible() != onShopTile) {
            shopButton.setVisible(onShopTile);
        }
}



    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void stopGame() {
        this.running = false;
        this.timer.stop();
        
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
