package com.thelegendofbald.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;

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
import com.thelegendofbald.characters.FinalBoss;
import com.thelegendofbald.controller.common.SwitchToOtherPanel;
import com.thelegendofbald.life.LifeComponent;
import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.common.DataManager;
import com.thelegendofbald.model.common.GameRun;
import com.thelegendofbald.model.common.Timer;
import com.thelegendofbald.model.common.Timer.TimeData;
import com.thelegendofbald.model.item.ItemGenerator;
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

/**
 * Main game panel that manages the core game lifecycle, rendering and input.
 *
 * <p>
 * Responsibilities:
 * <ul>
 * <li>Initialize and lay out game UI components (tile map, player, enemies,
 * HUD, shop, inventory, options).</li>
 * <li>Drive the game loop on a dedicated thread (implements {@link Runnable}),
 * updating game state
 * and triggering repaints at a configurable maximum frame rate.</li>
 * <li>Handle player input via key bindings, translate input into movement,
 * attacks and interactions.</li>
 * <li>Manage map transitions, actor spawning, item loading, combat coordination
 * and saving/loading game runs.</li>
 * <li>Render game visuals (tiles, actors, projectiles, HUD overlays) and debug
 * overlays (FPS, timer).</li>
 * </ul>
 * </p>
 *
 * <p>
 * Usage notes:
 * <ul>
 * <li>Call {@link #startGame()} to start the game loop and timer; this method
 * will also prompt for a player name.</li>
 * <li>Call {@link #pauseGame()} / {@link #resumeGame()} to pause or resume the
 * game; when paused the loop sleeps instead of updating.</li>
 * <li>Call {@link #stopGame()} to stop the loop and reset game state.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Threading and Swing:
 * <ul>
 * <li>The game loop runs off the Event Dispatch Thread (EDT) on a separate
 * {@link Thread}; UI-mutating operations
 * must be performed on the EDT (this class uses
 * {@link javax.swing.SwingUtilities#invokeLater} where appropriate).</li>
 * <li>Several fields that are accessed by both the game thread and the EDT are
 * declared {@code volatile} or {@code transient}
 * to reduce data-race risk, but callers should not assume full thread-safety
 * for complex interactions.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Serialization:
 * <ul>
 * <li>The class defines a custom {@code readObject} to restore transient
 * collections after deserialization.</li>
 * </ul>
 * </p>
 *
 * @see #startGame()
 * @see #stopGame()
 * @see #pauseGame()
 * @see #resumeGame()
 */
public final class GamePanel extends MenuPanel implements Runnable, Game {

    private static final int FINAL_GAME_SCREEN_TITLE_TRANSPARENCY = 150;

    private static final long serialVersionUID = 1L;

    private static final String MAP_1 = "map_1";
    private static final String MAP_2 = "map_2";
    private static final String MAP_3 = "map_3";
    private static final String MAP_4 = "map_4";

    private static final int DEFAULT_W = 1280;
    private static final int DEFAULT_H = 704;

    private static final int LIFE_W = 200;
    private static final int LIFE_H = 20;
    private static final int LIFE_Y = 800;

    private static final int TILE_SIZE = 32;
    private static final int DEFAULT_MAX_FPS = 60;

    private static final int BALD_W = 60;
    private static final int BALD_H = 60;

    private static final int ENEMY_W = 60;
    private static final int ENEMY_H = 60;

    private static final int BOSS_W = 96;
    private static final int BOSS_H = 96;

    private static final int ID_PORTAL = 4;
    private static final int ID_SPAWN = 5;
    private static final int ID_SHOP = 6;
    private static final int ID_ENEMY = 7;
    private static final int ID_PREV_PORTAL = 8;
    private static final int ID_BOSS = 9;
    private static final int ID_NEXT_MAP_TRIGGER = 10;

    private static final double OPTIONS_WIDTH_INSETS = 0.25;
    private static final double OPTIONS_HEIGHT_INSETS = 0.1;
    private static final double INVENTORY_WIDTH_INSETS = 0.25;
    private static final double INVENTORY_HEIGHT_INSETS = 0.25;

    private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);
    private static final Pair<Integer, Integer> FPS_POSITION = Pair.of(15, 25);
    private static final Pair<Integer, Integer> TIMER_POSITION = Pair.of(1085, 25);
    private static final Color ATTACK_AREA_COLOR = new Color(200, 200, 200, 100);

    private static final double MOVE_SPEED = 2.0;

    private static final long NANOS_IN_SECOND = 1_000_000_000L;
    private static final long MILLIS_IN_SECOND = 1000L;
    private static final long NANOS_IN_MILLI = 1_000_000L;
    private static final long SLEEP_INTERVAL_WHEN_PAUSED = 100L;
    private static final long LATE_FRAME_BACKOFF_NANOS = 250_000L;
    private static final long PORTAL_COOLDOWN_MS = 2000;

    private static final int WEAPON_ICON = 50;

    private static final double DX_STEP = 1.0;
    private static final double DY_STEP = 1.0;

    private static final int INVENTORY_COLS = 5;
    private static final int INVENTORY_ROWS = 3;

    private static final int LOOT_LVL_MIN = 7;
    private static final int LOOT_LVL_MAX = 9;

    private long portalCooldownUntil;

    private Integer pendingEntryTileId;
    private Integer pendingEntryIndex;

    private Boolean pendingFacingRight;

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints optionsGBC = gbcFactory.createBothGridBagConstraints();
    private final GridBagConstraints inventoryGBC = gbcFactory.createBothGridBagConstraints();

    private final transient Bald bald = new Bald(BALD_W, BALD_H, 100, "Bald", 50);
    private String currentMapName = MAP_1;

    private final Map<String, String> mapTransitions = Map.of(
            MAP_1, MAP_2,
            MAP_2, MAP_3,
            MAP_3, MAP_4);

    private final Map<String, String> reverseTransitions = Map.of(
            MAP_2, MAP_1,
            MAP_3, MAP_2);

    private final GridPanel gridPanel;
    private final transient TileMap tileMap;
    private final transient ItemManager itemManager;
    private final transient LootGenerator lootGenerator;

    private final LifePanel lifePanel;
    private transient List<DummyEnemy> enemies = new ArrayList<>();
    private final JPanel optionsPanel;
    private final transient Timer timer = new Timer();
    private transient GameRun gameRun;
    private final transient CombatManager combatManager;
    private final transient DataManager saveDataManager = new DataManager();

    private final JPanel inventoryPanel;
    private final transient Inventory inventory;

    private transient Thread gameThread;
    private volatile boolean running;
    private volatile boolean gameOver;
    private volatile boolean gameWon;
    private volatile boolean paused;
    private volatile int maxFPS = DEFAULT_MAX_FPS;
    private volatile boolean showingFPS = (boolean) VideoSettings.SHOW_FPS.getValue();
    private volatile int currentFPS;
    private volatile boolean showingTimer = (boolean) VideoSettings.SHOW_TIMER.getValue();

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final JButton shopButton = new JButton("Shop");
    private final JButton mainMenuButton = new JButton("Ritorna alla pagina principale");

    private FinalBoss boss;

    /**
     * Constructs the main game panel and initializes core game systems and UI.
     *
     * <p>
     * This constructor performs the following initializations:
     * <ul>
     * <li>Creates and configures the main grid and tile map used for rendering and
     * collision.</li>
     * <li>Initializes the player character ({@code Bald}) with default size, health
     * and a starting weapon.</li>
     * <li>Creates the item, loot and inventory managers and preloads items for the
     * first map.</li>
     * <li>Positions the player at the configured spawn point (if available) and
     * populates the inventory
     * with a set of starter weapons.</li>
     * <li>Sets up key bindings for input and schedules Swing-specific
     * initialization via
     * {@link SwingUtilities#invokeLater}.</li>
     * </ul>
     *
     * <p>
     * The constructor prepares the panel for use but does not start the game loop
     * or timer;
     * {@link #startGame()} must be invoked to begin gameplay.
     * </p>
     */
    public GamePanel() {
        super();
        final Dimension size = new Dimension(DEFAULT_W, DEFAULT_H);

        this.gridPanel = new GridPanel();
        this.gridPanel.setOpaque(false);
        this.gridPanel.setBounds(0, 0, size.width, size.height);

        this.lifePanel = new LifePanel(bald.getLifeComponent());
        this.lifePanel.setBounds(100, LIFE_Y, LIFE_W, LIFE_H);

        this.optionsPanel = new GameOptionsPanel();
        this.inventoryPanel = new InventoryPanel("INVENTORY", INVENTORY_COLS, INVENTORY_ROWS);
        this.inventory = ((InventoryPanel) this.inventoryPanel).getInventory();
        this.inventory.setBald(bald);

        this.tileMap = new TileMap(size.width, size.height, TILE_SIZE);
        this.combatManager = new CombatManager(bald, enemies);
        this.bald.setWeapon(new Sword(0, 0, WEAPON_ICON, WEAPON_ICON, combatManager));

        this.lootGenerator = new LootGenerator(new ItemGenerator(), List.of(LOOT_LVL_MIN, LOOT_LVL_MAX));
        this.itemManager = new ItemManager(tileMap, new ItemGenerator(), new MapItemLoader(), lootGenerator);
        this.itemManager.loadItemsForMap(MAP_1);

        tileMap.changeMap(MAP_1);
        bald.setTileMap(tileMap);
        bald.setSpawnPosition(ID_SPAWN, tileMap.getTileSize());

        final Point spawnPoint = tileMap.findSpawnPoint(ID_SPAWN);
        if (spawnPoint != null) {
            final int tileSize = tileMap.getTileSize();
            bald.setPosX(spawnPoint.x + (tileSize - bald.getWidth()) / 2);
            bald.setPosY(spawnPoint.y - bald.getHeight());
        }

        addWeaponsToInventory();
        setupKeyBindings();
        initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setBackground(Color.BLACK);
            this.setFocusable(true);
            this.setLayout(new GridBagLayout());
        });
    }

    private void readObject(final ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.enemies = new ArrayList<>();
    }

    private void addWeaponsToInventory() {
        final List<Weapon> weapons = List.of(
                new FireBall(0, 0, WEAPON_ICON, WEAPON_ICON, combatManager),
                new Sword(0, 0, WEAPON_ICON, WEAPON_ICON, combatManager),
                new Axe(0, 0, WEAPON_ICON, WEAPON_ICON, combatManager));
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

    /**
     * Refreshes the key bindings based on the current control settings.
     * This method should be called when control settings are changed.
     */
    public void refreshKeyBindings() {
        pressedKeys.clear();
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        final InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        final ActionMap am = this.getActionMap();
        im.clear();
        am.clear();

        // Pressed
        bindKey(im, am, "pressed UP", ControlsSettings.UP.getKey(), true,
                () -> pressedKeys.add(ControlsSettings.UP.getKey()));
        bindKey(im, am, "pressed DOWN", ControlsSettings.DOWN.getKey(), true,
                () -> pressedKeys.add(ControlsSettings.DOWN.getKey()));
        bindKey(im, am, "pressed LEFT", ControlsSettings.LEFT.getKey(), true,
                () -> pressedKeys.add(ControlsSettings.LEFT.getKey()));
        bindKey(im, am, "pressed RIGHT", ControlsSettings.RIGHT.getKey(), true,
                () -> pressedKeys.add(ControlsSettings.RIGHT.getKey()));
        bindKey(im, am, "pressed ESCAPE", KeyEvent.VK_ESCAPE, true, this::toggleOptionsPanel);
        bindKey(im, am, "pressed SPACE", ControlsSettings.ATTACK.getKey(), true,
                combatManager::tryToAttack);
        bindKey(im, am, "pressed I", ControlsSettings.INVENTORY.getKey(), true,
                this::toggleOpenInventory);
        bindKey(im, am, "interact", ControlsSettings.INTERACT.getKey(), true,
                this::interactWithItems);

        // Released
        bindKey(im, am, "released UP", ControlsSettings.UP.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.UP.getKey()));
        bindKey(im, am, "released DOWN", ControlsSettings.DOWN.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.DOWN.getKey()));
        bindKey(im, am, "released LEFT", ControlsSettings.LEFT.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.LEFT.getKey()));
        bindKey(im, am, "released RIGHT", ControlsSettings.RIGHT.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.RIGHT.getKey()));
    }

    private void bindKey(
            final InputMap im,
            final ActionMap am,
            final String name,
            final int key,
            final boolean pressed,
            final Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0, !pressed), name);
        am.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                action.run();
            }
        });
    }

    /**
     * Handles player input by updating the player's speed and direction based on
     * currently pressed keys.
     * If the player is immobilized, all movement is stopped.
     */
    private void handleInput() {
        if (bald.isImmobilized()) {
            bald.setSpeedX(0);
            bald.setSpeedY(0);
            return;
        }
        double dx = 0;
        double dy = 0;

        if (pressedKeys.contains(ControlsSettings.LEFT.getKey())) {
            dx -= DX_STEP;
        }
        if (pressedKeys.contains(ControlsSettings.RIGHT.getKey())) {
            dx += DX_STEP;
        }
        if (!MAP_1.equals(currentMapName)) {
            if (pressedKeys.contains(ControlsSettings.UP.getKey())) {
                dy -= DY_STEP;
            }
            if (pressedKeys.contains(ControlsSettings.DOWN.getKey())) {
                dy += DY_STEP;
            }
        }
        if (pressedKeys.contains(ControlsSettings.ATTACK.getKey())) {
            combatManager.tryToAttack();
        }

        final double magnitude = Math.hypot(dx, dy);
        if (magnitude > 0) {
            dx = dx / magnitude * MOVE_SPEED;
            dy = dy / magnitude * MOVE_SPEED;

        }

        bald.setFacingRight(dx > 0 || dx == 0 && bald.isFacingRight());
        bald.setSpeedX(dx);
        bald.setSpeedY(dy);
    }

    private void interactWithItems() {
        itemManager.getItems().stream()
                .filter(item -> bald.getBounds().intersects(item.getBounds()))
                .filter(item -> item instanceof Interactable)
                .map(item -> (Interactable) item)
                .findFirst()
                .ifPresent(interactableItem -> interactableItem.interact());
    }

    private void setFacingForTransition(final String from, final String to) {
        if (MAP_2.equals(from) && MAP_3.equals(to)) {
            pendingFacingRight = false;
        } else if (MAP_3.equals(from) && MAP_2.equals(to)) {
            pendingFacingRight = false;
        } else if (MAP_3.equals(from) && MAP_4.equals(to)) {
            pendingFacingRight = true;
        } else {
            pendingFacingRight = null;
        }
    }

    /**
     * Returns the current game run data, which includes the player's name and time.
     *
     * @return the current {@link GameRun} instance.
     */
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
                final GameWindow window = (GameWindow) SwingUtilities.getWindowAncestor(this);
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
        mainMenuButton.setVisible(false);
        gameThread = new Thread(this);
        gameThread.start();
        timer.start();
        this.setPlayerName();
    }

    @Override
    public void saveGame() {
        gameRun = new GameRun(gameRun.name(), timer.getFormattedTime());
        try {
            saveDataManager.saveGameRun(gameRun);
        } catch (final IOException e) {
            LoggerUtils.error("Error saving game run: " + e.getMessage());
        }
    }

    private void resetGame() {
        this.gameOver = false;
        this.gameWon = false;
        this.paused = false;
        this.pressedKeys.clear();

        this.timer.stop();

        this.currentMapName = MAP_1;
        this.tileMap.changeMap(MAP_1);
        this.itemManager.loadItemsForMap(MAP_1);
        this.spawnActorsFromMap();

        this.bald.setTileMap(tileMap);
        this.bald.setSpawnPosition(ID_SPAWN, tileMap.getTileSize());
        this.bald.getLifeComponent().setCurrentHealth(100);
        this.inventory.clear();
        this.addWeaponsToInventory();
        this.optionsPanel.setVisible(false);
        this.inventoryPanel.setVisible(false);
        this.mainMenuButton.setVisible(false);
        this.shopButton.setVisible(false);

        this.revalidate();
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

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        int frames = 0;
        long fpsTimer = System.currentTimeMillis();

        while (gameThread != null && running) {
            if (paused) {
                LockSupport.parkNanos(SLEEP_INTERVAL_WHEN_PAUSED * NANOS_IN_MILLI);
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

            final long frameTime = (System.nanoTime() - now) / NANOS_IN_MILLI;
            final long targetFrameTime = MILLIS_IN_SECOND / Math.max(1, maxFPS);
            final long sleepTime = targetFrameTime - frameTime;

            if (sleepTime > 0) {
                LockSupport.parkNanos(sleepTime * NANOS_IN_MILLI);
            } else {
                LockSupport.parkNanos(LATE_FRAME_BACKOFF_NANOS);
            }
        }
    }

    /**
     * Updates the game state for the current frame.
     *
     * @param deltatime the time elapsed (in seconds) since the previous frame.
     */
    public void update(final double deltatime) {
        if (gameOver || gameWon) {
            return;
        }
        handleInput();
        bald.updateAnimation();
        bald.move(tileMap, deltatime);
        bald.updateBuffs();

        if (System.currentTimeMillis() >= portalCooldownUntil) {
            if (isTouchingOrAdjacentToTileId(ID_NEXT_MAP_TRIGGER)) {
                switchToNextMap();
                return;
            }
            if (isTouchingOrAdjacentToTileId(ID_PREV_PORTAL)) {
                switchToPreviousMap();
                return;
            }
        }

        combatManager.checkEnemyAttacks();
        enemies.removeIf(DummyEnemy::isRemovable);
        enemies.forEach(enemy -> {
            if (enemy.isCloseTo(bald)) {
                enemy.followPlayer(bald);
                enemy.updateAnimation();
            }
        });

        if (boss != null && boss.isAlive()) {
            boss.followPlayer(bald);
            boss.updateAnimation();
        }
        combatManager.getProjectiles().forEach(p -> p.move(tileMap));
        combatManager.checkProjectiles();

        itemManager.updateAll();
        itemManager.handleItemCollection(bald);
        checkIfNearShopTile();
        if (!bald.isAlive() && !gameOver) {
            handleGameOver();
        }
        if (boss != null && !boss.isAlive() && !gameWon && !gameOver) {
            handleGameWon();
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * Handles the game won logic: stops the game, saves the run, and sets the win
     * flag.
     */
    private void handleGameWon() {
        this.gameWon = true;
        this.pauseGame();
        this.saveGame();
        this.pressedKeys.clear();
        this.mainMenuButton.setVisible(true);
    }

    /**
     * Handles the game over logic: stops the game and sets the game over flag.
     */
    private void handleGameOver() {
        this.gameOver = true;
        this.pauseGame();
        this.pressedKeys.clear();
        this.mainMenuButton.setVisible(true);
    }

    /**
     * Checks if the player character is touching or adjacent to a tile with the
     * specified ID.
     *
     * @param tileId the ID of the tile to check for.
     * @return {@code true} if touching or adjacent, otherwise {@code false}.
     */
    private boolean isTouchingOrAdjacentToTileId(final int tileId) {
        final int ts = tileMap.getTileSize();

        final int x1 = bald.getX();
        final int y1 = bald.getY();
        final int x2 = x1 + bald.getWidth() - 1;
        final int y2 = y1 + bald.getHeight() - 1;

        final int leftIn = Math.max(0, x1 / ts);
        final int rightIn = Math.max(0, x2 / ts);
        final int topIn = Math.max(0, y1 / ts);
        final int bottomIn = Math.max(0, y2 / ts);

        for (int ty = topIn; ty <= bottomIn; ty++) {
            for (int tx = leftIn; tx <= rightIn; tx++) {
                if (tileHasId(tx, ty, tileId)) {
                    return true;
                }
            }
        }

        final int leftEdgeCol = Math.max(0, (x1 - 1) / ts);
        final int rightEdgeCol = Math.max(0, (x2 + 1) / ts);
        final int topEdgeRow = Math.max(0, (y1 - 1) / ts);
        final int bottomEdgeRow = Math.max(0, (y2 + 1) / ts);

        for (int ty = topIn; ty <= bottomIn; ty++) {
            if (tileHasId(leftEdgeCol, ty, tileId)) {
                return true;
            }
            if (tileHasId(rightEdgeCol, ty, tileId)) {
                return true;
            }
        }
        for (int tx = leftIn; tx <= rightIn; tx++) {
            if (tileHasId(tx, topEdgeRow, tileId)) {
                return true;
            }
            if (tileHasId(tx, bottomEdgeRow, tileId)) {
                return true;
            }
        }

        return false;
    }

    private boolean tileHasId(final int tx, final int ty, final int id) {
        final Tile t = tileMap.getTileAt(tx, ty);
        return t != null && t.getId() == id;
    }

    private void switchToNextMap() {
        final String nextMapName = mapTransitions.get(currentMapName);
        if (nextMapName != null) {
            pendingEntryTileId = ID_SPAWN;
            pendingEntryIndex = 0;
            setFacingForTransition(currentMapName, nextMapName);
            changeAndLoadMap(nextMapName);
        } else {
            LoggerUtils.error("No next map defined.");
        }
    }

    private void switchToPreviousMap() {
        final String prevMapName = reverseTransitions.get(currentMapName);
        if (prevMapName != null) {
            pendingEntryTileId = ID_PORTAL;
            pendingEntryIndex = 0;
            setFacingForTransition(currentMapName, prevMapName);
            changeAndLoadMap(prevMapName);
        } else {
            LoggerUtils.error("No previous map defined.");
        }
    }

    private void changeAndLoadMap(final String mapName) {
        boss = null;
        currentMapName = mapName;

        tileMap.changeMap(mapName);
        bald.setTileMap(tileMap);

        boolean placed = false;

        if (pendingEntryTileId != null && pendingEntryIndex != null) {
            final List<Point> entries = tileMap.findAllWithId(pendingEntryTileId);
            if (!entries.isEmpty()) {
                final int idx = Math.max(0, Math.min(pendingEntryIndex, entries.size() - 1));
                final Point topLeft = entries.get(idx);
                final int ts = tileMap.getTileSize();
                bald.setPosX(topLeft.x + (ts - bald.getWidth()) / 2);
                bald.setPosY(topLeft.y - bald.getHeight());
                placed = true;
            }
        }

        if (!placed) {
            final List<Point> portals = tileMap.findAllWithId(ID_PORTAL);
            if (!portals.isEmpty()) {
                final Point topLeft = portals.get(0);
                final int ts = tileMap.getTileSize();
                bald.setPosX(topLeft.x + (ts - bald.getWidth()) / 2);
                bald.setPosY(topLeft.y - bald.getHeight());
                placed = true;
            }
        }

        pendingEntryTileId = null;
        pendingEntryIndex = null;

        if (!placed) {
            bald.setSpawnPosition(ID_SPAWN, tileMap.getTileSize());
        }

        portalCooldownUntil = System.currentTimeMillis() + PORTAL_COOLDOWN_MS;

        bald.setSpeedX(0);
        bald.setSpeedY(0);
        pressedKeys.clear();

        if (pendingFacingRight != null) {
            bald.setFacingRight(pendingFacingRight);
            pendingFacingRight = null;
        }

        spawnActorsFromMap();

        itemManager.loadItemsForMap(mapName);

        shopButton.setVisible(false);
    }

    private void spawnActorsFromMap() {
        enemies.clear();
        boss = null;

        final List<Point> enemyTiles = tileMap.findAllWithId(ID_ENEMY);
        final List<Point> bossTiles = tileMap.findAllWithId(ID_BOSS);

        enemyTiles.forEach(this::spawnEnemyAt);
        bossTiles.stream().findFirst().ifPresent(this::spawnBossAt);
    }

    private void spawnEnemyAt(final Point topLeft) {
        final int ts = tileMap.getTileSize();
        final int x = topLeft.x + (ts - ENEMY_W) / 2;
        final int y = topLeft.y + (ts - ENEMY_H) / 2;
        enemies.add(new DummyEnemy(x, y, ENEMY_W, "ZioBilly", 10, tileMap));
    }

    private void spawnBossAt(final Point topLeft) {
        if (boss != null) {
            return;
        }

        final int ts = tileMap.getTileSize();
        final int x = topLeft.x + (ts - BOSS_W) / 2;
        final int y = topLeft.y + (ts - BOSS_H) / 2;

        final int bossHp = 500;
        final int bossAtk = 1;
        final LifeComponent life = new LifeComponent(bossHp);

        boss = new FinalBoss(
                x, y,
                "Final Boss",
                bossHp,
                bossAtk,
                life,
                tileMap);

        combatManager.setBoss(boss);
    }

    @Override
    protected void paintComponent(final Graphics g) {

        final Graphics2D g2d = (Graphics2D) g.create();
        super.paintComponent(g2d);
        scaleGraphics(g2d);

        tileMap.paint(g2d);
        gridPanel.paintComponent(g2d);
        itemManager.renderAll(g2d);

        bald.render(g2d);
        enemies.forEach(enemy -> enemy.render(g2d));

        if (boss != null && boss.isAlive()) {
            boss.render(g2d);
        }

        combatManager.getProjectiles().forEach(p -> p.render(g2d));

        lifePanel.paintComponent(g2d);
        drawFPS(g2d);
        drawTimer(g2d);
        drawAttackArea(g2d);

        drawBossHP(g2d);
        if (gameOver) {
            drawGameOverScreen(g2d);
        } else if (gameWon) {
            drawGameWonScreen(g2d);
        }
        g2d.dispose();
    }

    private void drawGameWonScreen(final Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, FINAL_GAME_SCREEN_TITLE_TRANSPARENCY));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        final String msg = "YOU WON";
        final Font winFont = new Font("Arial", Font.BOLD, 72);
        g2d.setFont(winFont);
        g2d.setColor(Color.GREEN);

        final FontMetrics fm = g2d.getFontMetrics(winFont);
        final int msgWidth = fm.stringWidth(msg);
        final int msgHeight = fm.getAscent();

        final int x = (getWidth() - msgWidth) / 2;
        final int y = (getHeight() - msgHeight) / 2 + fm.getAscent();

        g2d.drawString(msg, x, y);
    }

    private void drawGameOverScreen(final Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, FINAL_GAME_SCREEN_TITLE_TRANSPARENCY));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        final String msg = "GAME OVER";
        final Font gameOverFont = new Font("Arial", Font.BOLD, 72);
        g2d.setFont(gameOverFont);
        g2d.setColor(Color.RED);

        final FontMetrics fm = g2d.getFontMetrics(gameOverFont);
        final int msgWidth = fm.stringWidth(msg);
        final int msgHeight = fm.getAscent();

        final int x = (getWidth() - msgWidth) / 2;
        final int y = (getHeight() - msgHeight) / 2 + fm.getAscent();

        g2d.drawString(msg, x, y);
    }

    private void drawAttackArea(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        bald.getWeapon().ifPresent(weapon -> {
            if (bald.isAttacking() && weapon instanceof MeleeWeapon) {
                final Arc2D attackArea = ((MeleeWeapon) weapon).getAttackArea();
                Optional.ofNullable(attackArea).ifPresent(atk -> {
                    g2d.setColor(ATTACK_AREA_COLOR);
                    g2d.fill(atk);
                });
            }
        });
    }

    private void drawBossHP(final Graphics2D g2d) {
        if (boss == null || !boss.isAlive()) {
            return;
        }

        final int w = 420, h = 18;
        final int x = (getWidth() - w) / 2;
        final int y = 12;

        final int hp = boss.getHealth();
        final int max = boss.getMaxHealth();
        final double ratio = Math.max(0.0, Math.min(1.0, hp / (double) max));
        final int fill = (int) (w * ratio);

        final int transparency = 140;
        final int xOffset = 6;
        final int yOffset = 6;
        final int widthOffset = 12;
        final int heightOffset = 18;
        final int arcWidth = 12;
        final int arcHeight = 12;
        final Color textColor = new Color(200, 40, 40);

        g2d.setColor(new Color(0, 0, 0, transparency));
        g2d.fillRoundRect(x - xOffset, y - yOffset, w + widthOffset, h + heightOffset, arcWidth, arcHeight);

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x, y, w, h);

        g2d.setColor(textColor);
        g2d.fillRect(x, y, fill, h);

        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, w, h);

        final float fontSize = 14f;
        g2d.setFont(DEFAULT_FONT.deriveFont(fontSize));

        final int xTextOffset = 6;
        final int yTextOffset = 14;
        g2d.drawString("FINAL BOSS  " + hp + "/" + max, x + xTextOffset, y + h + yTextOffset);
    }

    private void drawFPS(final Graphics g) {
        if (showingFPS) {
            g.setColor(Color.YELLOW);
            g.setFont(DEFAULT_FONT);
            g.drawString("FPS: " + currentFPS,
                    FPS_POSITION.getLeft(), FPS_POSITION.getRight());
        }
    }

    private void drawTimer(final Graphics g) {
        if (showingTimer) {
            final TimeData timeData = timer.getFormattedTime();
            g.setColor(Color.WHITE);
            g.setFont(DEFAULT_FONT);
            g.drawString(
                    String.format("Timer: %02d:%02d:%02d",
                            timeData.hours(), timeData.minutes(), timeData.seconds()),
                    TIMER_POSITION.getLeft(), TIMER_POSITION.getRight());
        }
    }

    private void scaleGraphics(final Graphics g) {
        Optional.ofNullable(SwingUtilities.getWindowAncestor(this))
                .filter(window -> window instanceof GameWindow)
                .map(window -> (GameWindow) window)
                .ifPresent(window -> {
                    final double scaleX = this.getWidth() / window.getInternalSize().getWidth();
                    final double scaleY = this.getHeight() / window.getInternalSize().getHeight();
                    ((Graphics2D) g).scale(scaleX, scaleY);
                });
    }

    /* ===================== Layout secondario/UI ===================== */

    @Override
    public void updateComponentsSize() {
        optionsGBC.insets.set(
                (int) (this.getHeight() * OPTIONS_HEIGHT_INSETS),
                (int) (this.getWidth() * OPTIONS_WIDTH_INSETS),
                (int) (this.getHeight() * OPTIONS_HEIGHT_INSETS),
                (int) (this.getWidth() * OPTIONS_WIDTH_INSETS));
        inventoryGBC.insets.set(
                (int) (this.getHeight() * INVENTORY_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_WIDTH_INSETS),
                (int) (this.getHeight() * INVENTORY_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_WIDTH_INSETS));
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        this.add(optionsPanel, optionsGBC);
        this.add(inventoryPanel, inventoryGBC);

        shopButton.setText("Shop");
        shopButton.setBackground(Color.YELLOW);
        shopButton.setOpaque(true);
        shopButton.setFocusable(false);
        shopButton.setVisible(false);
        shopButton.addActionListener(this::onShopButtonClicked);

        mainMenuButton.setVisible(false);
        mainMenuButton.setFocusable(false);
        mainMenuButton.addActionListener(e -> {
            stopGame();
            final GameWindow window = (GameWindow) SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                new SwitchToOtherPanel(window, Panels.MAIN_MENU).actionPerformed(e);
            }
        });

        final GridBagConstraints fillerGBC = new GridBagConstraints();
        fillerGBC.gridx = 0;
        fillerGBC.gridy = 0;
        fillerGBC.weightx = 1;
        fillerGBC.weighty = 1;
        fillerGBC.fill = GridBagConstraints.BOTH;
        this.add(Box.createGlue(), fillerGBC);

        final GridBagConstraints shopButtonGBC = new GridBagConstraints();
        shopButtonGBC.gridx = 0;
        shopButtonGBC.gridy = 0;
        shopButtonGBC.insets = new Insets(10, 10, 10, 10);
        shopButtonGBC.anchor = GridBagConstraints.SOUTH;
        shopButtonGBC.fill = GridBagConstraints.NONE;
        shopButtonGBC.weightx = 0;
        shopButtonGBC.weighty = 0;
        this.add(shopButton, shopButtonGBC);

        final GridBagConstraints mainMenuButtonGBC = new GridBagConstraints();
        final Insets mainMenuInsets = new Insets(150, 0, 0, 0);
        mainMenuButtonGBC.gridx = 0;
        mainMenuButtonGBC.gridy = 0;
        mainMenuButtonGBC.anchor = GridBagConstraints.CENTER;
        mainMenuButtonGBC.insets = mainMenuInsets;
        this.add(mainMenuButton, mainMenuButtonGBC);
    }

    private void onShopButtonClicked(final java.awt.event.ActionEvent event) {
        Objects.requireNonNull(event, "event");
        final ShopPanel shopPanel = new ShopPanel(this.combatManager, bald.getWallet());
        JOptionPane.showMessageDialog(this, shopPanel, "SHOP", JOptionPane.PLAIN_MESSAGE);
    }

    /** Shows the shop button when Bald is over a {@link #ID_SHOP} tile. */
    private void checkIfNearShopTile() {
        final int tileSize = tileMap.getTileSize();

        final int baldX = bald.getX();
        final int baldY = bald.getY();
        final int baldW = bald.getWidth();
        final int baldH = bald.getHeight();

        final int feetY = baldY + baldH;
        final int feetYInside = Math.max(0, feetY - 1);

        final int tileFeetY = feetYInside / tileSize;
        final int tileCenterX = (baldX + baldW / 2) / tileSize;

        final Tile tileUnderFeet = tileMap.getTileAt(tileCenterX, tileFeetY);
        final boolean onShopTile = tileUnderFeet != null && tileUnderFeet.getId() == ID_SHOP;

        if (shopButton.isVisible() != onShopTile) {
            shopButton.setVisible(onShopTile);
        }
    }

    /*
     * ===================== Getters/Setters richiesti dall'interfaccia
     * =====================
     */

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void stopGame() {
        this.running = false;
        this.timer.stop();
        this.resetGame();
    }

    @Override
    public void setFPS(final int fps) {
        this.maxFPS = fps;
    }

    @Override
    public void setShowingFPS(final boolean value) {
        this.showingFPS = value;
    }

    /**
     * @return {@code true} if the FPS counter is being shown in the UI.
     */
    public boolean isShowingFPS() {
        return showingFPS;
    }

    /**
     * Enables or disables the display of the game timer.
     *
     * @param showingTimer if {@code true}, the timer is shown as an overlay.
     */
    public void setShowingTimer(final boolean showingTimer) {
        this.showingTimer = showingTimer;
    }

    /**
     * @return {@code true} if the timer is being shown in the UI.
     */
    public boolean isShowingTimer() {
        return showingTimer;
    }

}
