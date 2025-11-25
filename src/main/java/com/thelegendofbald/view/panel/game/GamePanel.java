package com.thelegendofbald.view.panel.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.geom.Arc2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.controller.GameEngine;
import com.thelegendofbald.controller.input.InputController;
import com.thelegendofbald.controller.level.LevelManager;
import com.thelegendofbald.controller.navigation.SwitchToOtherPanel;
import com.thelegendofbald.model.config.VideoSettings;
import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.model.entity.DummyEnemy;
import com.thelegendofbald.model.inventory.Inventory;
import com.thelegendofbald.model.item.weapons.MeleeWeapon;
import com.thelegendofbald.model.item.weapons.Sword;
import com.thelegendofbald.model.item.weapons.Weapon;
import com.thelegendofbald.model.system.CombatManager;
import com.thelegendofbald.model.system.DataManager;
import com.thelegendofbald.model.system.Game;
import com.thelegendofbald.model.system.GameRun;
import com.thelegendofbald.model.system.Timer;
import com.thelegendofbald.model.system.Timer.TimeData;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.layout.GridBagConstraintsFactory;
import com.thelegendofbald.view.layout.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.view.panel.base.MenuPanel;
import com.thelegendofbald.view.panel.base.Panels;
import com.thelegendofbald.view.panel.hud.LifePanel;
import com.thelegendofbald.view.panel.inventory.InventoryPanel;
import com.thelegendofbald.view.panel.shop.ShopPanel;
import com.thelegendofbald.view.render.GridPanel;
import com.thelegendofbald.view.render.Tile;
import com.thelegendofbald.view.render.TileMap;
import com.thelegendofbald.view.window.GameWindow;

/**
 * Main game panel that manages the core game lifecycle, rendering and input.
 */
public final class GamePanel extends MenuPanel implements Game {

    private static final int FINAL_GAME_SCREEN_TITLE_TRANSPARENCY = 150;
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_W = 1280;
    private static final int DEFAULT_H = 704;

    private static final int LIFE_W = 200;
    private static final int LIFE_H = 20;
    private static final int LIFE_Y = 800;

    private static final int TILE_SIZE = 32;

    private static final int BALD_W = 60;
    private static final int BALD_H = 60;

    private static final int ID_SHOP = 6;

    private static final double OPTIONS_WIDTH_INSETS = 0.25;
    private static final double OPTIONS_HEIGHT_INSETS = 0.1;
    private static final double INVENTORY_WIDTH_INSETS = 0.25;
    private static final double INVENTORY_HEIGHT_INSETS = 0.25;

    private static final java.awt.Font DEFAULT_FONT = new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.BOLD, 20);
    private static final Pair<Integer, Integer> FPS_POSITION = Pair.of(15, 25);
    private static final Pair<Integer, Integer> TIMER_POSITION = Pair.of(1085, 25);
    private static final Color ATTACK_AREA_COLOR = new Color(200, 200, 200, 100);

    private static final int WEAPON_ICON = 50;
    private static final int INVENTORY_COLS = 5;
    private static final int INVENTORY_ROWS = 3;

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints optionsGBC = gbcFactory.createBothGridBagConstraints();
    private final GridBagConstraints inventoryGBC = gbcFactory.createBothGridBagConstraints();

    private final transient Bald bald = new Bald(BALD_W, BALD_H, 100, "Bald", 50);

    private final GridPanel gridPanel;
    private final LifePanel lifePanel;
    private final JPanel optionsPanel;
    private final JPanel inventoryPanel;
    private final transient Inventory inventory;
    private final JButton shopButton = new JButton("Shop");
    private final JButton mainMenuButton = new JButton("Ritorna alla pagina principale");
    private final ShopPanel shopPanel;

    private final transient Timer timer = new Timer();
    private transient GameRun gameRun;
    private final transient CombatManager combatManager;
    private final transient DataManager saveDataManager = new DataManager();

    private final transient GameEngine gameEngine;
    private final transient LevelManager levelManager;
    private final transient InputController inputController;

    private volatile boolean showingFPS = (boolean) VideoSettings.SHOW_FPS.getValue();
    private volatile boolean showingTimer = (boolean) VideoSettings.SHOW_TIMER.getValue();

    /**
     * Constructs a new GamePanel.
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

        final TileMap tileMap = new TileMap(size.width, size.height, TILE_SIZE);
        final List<DummyEnemy> enemies = new ArrayList<>();
        this.combatManager = new CombatManager(bald, enemies);
        this.bald.setWeapon(new Sword(0, 0, WEAPON_ICON, WEAPON_ICON, combatManager));

        this.levelManager = new LevelManager(tileMap, bald, combatManager, enemies);
        this.levelManager.loadInitialMap();

        shopButton.setText("Shop");
        shopButton.setBackground(Color.YELLOW);
        shopButton.setOpaque(true);
        shopButton.setFocusable(false);
        shopButton.setVisible(false);
        shopButton.addActionListener(this::onShopButtonClicked);

        shopPanel = new ShopPanel(this.combatManager, bald.getWallet(), this.inventory);

        addWeaponsToInventory();

        this.inputController = new InputController(this, bald, combatManager, levelManager,
                this::toggleOptionsPanel, this::toggleOpenInventory);
        this.gameEngine = new GameEngine(this, bald, levelManager, combatManager, inputController, timer);

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
    }

    private void addWeaponsToInventory() {
        final Weapon sword = new Sword(0, 0, WEAPON_ICON, WEAPON_ICON, combatManager);
        inventory.add(sword);
    }

    private void toggleOptionsPanel() {
        if (gameEngine.isPaused() && !inventoryPanel.isVisible()) {
            closeOptionsPanel();
        } else if (!inventoryPanel.isVisible()) {
            openOptionsPanel();
        } else {
            inventoryPanel.setVisible(false);
            openOptionsPanel();
        }
    }

    private void toggleOpenInventory() {
        if (!gameEngine.isPaused() && !optionsPanel.isVisible() && !inventoryPanel.isVisible()) {
            inventoryPanel.setVisible(true);
        } else if (inventoryPanel.isVisible()) {
            inventoryPanel.setVisible(false);
        }
    }

    private void openOptionsPanel() {
        pauseGame();
        inputController.clearPressedKeys();
        this.optionsPanel.setVisible(true);
        this.optionsPanel.requestFocusInWindow();
        this.repaint();
    }

    private void closeOptionsPanel() {
        this.optionsPanel.setVisible(false);
        inputController.clearPressedKeys();
        resumeGame();
        this.requestFocusInWindow();
        this.repaint();
    }

    /**
     * Refreshes the key bindings.
     */
    public void refreshKeyBindings() {
        inputController.refreshKeyBindings(this);
    }

    /**
     * Returns the current game run.
     *
     * @return the current game run.
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
        mainMenuButton.setVisible(false);
        gameEngine.start();
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
        gameEngine.reset();

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
        gameEngine.pause();
    }

    @Override
    public void resumeGame() {
        gameEngine.resume();
    }

    @Override
    protected void paintComponent(final Graphics g) {

        final Graphics2D g2d = (Graphics2D) g.create();
        super.paintComponent(g2d);
        scaleGraphics(g2d);

        levelManager.getTileMap().paint(g2d);
        gridPanel.paintComponent(g2d);
        levelManager.getItemManager().renderAll(g2d);

        bald.render(g2d);
        levelManager.getEnemies().forEach(enemy -> enemy.render(g2d));

        if (levelManager.getBoss() != null && levelManager.getBoss().isAlive()) {
            levelManager.getBoss().render(g2d);
        }

        combatManager.getProjectiles().forEach(p -> p.render(g2d));

        lifePanel.paintComponent(g2d);
        drawFPS(g2d);
        drawTimer(g2d);
        drawAttackArea(g2d);

        drawBossHP(g2d);
        if (gameEngine.isGameOver()) {
            drawGameOverScreen(g2d);
        } else if (gameEngine.isGameWon()) {
            drawGameWonScreen(g2d);
        }
        g2d.dispose();
    }

    private void drawGameWonScreen(final Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, FINAL_GAME_SCREEN_TITLE_TRANSPARENCY));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        final String msg = "YOU WON";
        final java.awt.Font winFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 72);
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
        final java.awt.Font gameOverFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 72);
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
        if (levelManager.getBoss() == null || !levelManager.getBoss().isAlive()) {
            return;
        }

        final int w = 420, h = 18;
        final int x = (getWidth() - w) / 2;
        final int y = 12;

        final int hp = levelManager.getBoss().getHealth();
        final int max = levelManager.getBoss().getMaxHealth();
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
            g.drawString("FPS: " + gameEngine.getCurrentFPS(),
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
        JOptionPane.showMessageDialog(this, shopPanel, "SHOP", JOptionPane.PLAIN_MESSAGE);
    }

    /** Shows the shop button when Bald is over a {@link #ID_SHOP} tile. */
    public void checkIfNearShopTile() {
        final int tileSize = levelManager.getTileMap().getTileSize();

        final int baldX = bald.getX();
        final int baldY = bald.getY();
        final int baldW = bald.getWidth();
        final int baldH = bald.getHeight();

        final int feetY = baldY + baldH;
        final int feetYInside = Math.max(0, feetY - 1);

        final int tileFeetY = feetYInside / tileSize;
        final int tileCenterX = (baldX + baldW / 2) / tileSize;

        final Tile tileUnderFeet = levelManager.getTileMap().getTileAt(tileCenterX, tileFeetY);
        final boolean onShopTile = tileUnderFeet != null && tileUnderFeet.getId() == ID_SHOP;

        if (shopButton.isVisible() != onShopTile) {
            shopButton.setVisible(onShopTile);
        }
    }

    /**
     * Shows the main menu button.
     */
    public void showMainMenuButton() {
        mainMenuButton.setVisible(true);
    }

    @Override
    public boolean isRunning() {
        return gameEngine.isRunning();
    }

    @Override
    public void stopGame() {
        gameEngine.stop();
        this.resetGame();
    }

    @Override
    public void setFPS(final int fps) {
        gameEngine.setMaxFPS(fps);
    }

    @Override
    public void setShowingFPS(final boolean value) {
        this.showingFPS = value;
    }

    /**
     * Checks if FPS are being shown.
     *
     * @return true if FPS are shown, false otherwise.
     */
    public boolean isShowingFPS() {
        return showingFPS;
    }

    /**
     * Sets whether to show the timer.
     *
     * @param showingTimer true to show the timer, false otherwise.
     */
    public void setShowingTimer(final boolean showingTimer) {
        this.showingTimer = showingTimer;
    }

    /**
     * Checks if the timer is being shown.
     *
     * @return true if timer is shown, false otherwise.
     */
    public boolean isShowingTimer() {
        return showingTimer;
    }

}
