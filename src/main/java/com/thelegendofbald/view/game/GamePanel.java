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
import com.thelegendofbald.api.interactable.Interactable;
import com.thelegendofbald.api.panels.LifePanel;
import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.api.settingsmenu.ControlsSettings;
import com.thelegendofbald.api.settingsmenu.VideoSettings;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.combat.projectile.Projectile;
import com.thelegendofbald.item.Chest;
import com.thelegendofbald.item.GameItem;
import com.thelegendofbald.item.InventoryItem;
import com.thelegendofbald.item.ItemFactory;
import com.thelegendofbald.item.MapItemSpawner;
import com.thelegendofbald.item.Trap;
import com.thelegendofbald.item.UsableItem;
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
    private final Map<String, String> mapTransitions = Map.of(
        "map_1", "map_2",
        "map_2", "map_3"
    );
    private final GridPanel gridPanel;
    private transient final TileMap tileMap;

    private transient ItemFactory itemFactory;
    private MapItemSpawner itemSpawner;
    private transient List<GameItem> gameItems = new ArrayList<>();

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

        this.itemFactory = new ItemFactory();
        this.gameItems = new ArrayList<>();

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
            enemies.add(new DummyEnemy(ThreadLocalRandom.current().nextInt(300, 1000), 
                                       ThreadLocalRandom.current().nextInt(300, 600),
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
        InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        bindKey(im, am, "pressed UP", KeyEvent.VK_UP, true, () -> pressedKeys.add(KeyEvent.VK_UP));
        bindKey(im, am, "pressed DOWN", KeyEvent.VK_DOWN, true, () -> pressedKeys.add(KeyEvent.VK_DOWN));
        bindKey(im, am, "pressed LEFT", KeyEvent.VK_LEFT, true, () -> pressedKeys.add(KeyEvent.VK_LEFT));
        bindKey(im, am, "pressed RIGHT", KeyEvent.VK_RIGHT, true, () -> pressedKeys.add(KeyEvent.VK_RIGHT));
        bindKey(im, am, "pressed SPACE", ControlsSettings.ATTACK.getKey(), true, combatManager::tryToAttack);

        bindKey(im, am, "released UP", KeyEvent.VK_UP, false, () -> pressedKeys.remove(KeyEvent.VK_UP));
        bindKey(im, am, "released DOWN", KeyEvent.VK_DOWN, false, () -> pressedKeys.remove(KeyEvent.VK_DOWN));
        bindKey(im, am, "released LEFT", KeyEvent.VK_LEFT, false, () -> pressedKeys.remove(KeyEvent.VK_LEFT));
        bindKey(im, am, "released RIGHT", KeyEvent.VK_RIGHT, false, () -> pressedKeys.remove(KeyEvent.VK_RIGHT));

        bindKey(im, am, "interact", KeyEvent.VK_E, true, this::interactWithItems);
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

        double magnitude = Math.hypot(dx, dy);
        if (magnitude > 0) {
            dx = (dx / magnitude) * MOVE_SPEED;
            dy = (dy / magnitude) * MOVE_SPEED;
        }

        if (dx > 0) {
            bald.setFacingRight(true);
        } else if (dx < 0) {
            bald.setFacingRight(false);
        }

        bald.setSpeedX(dx);
        bald.setSpeedY(dy);
    }

    private void interactWithItems() {
        gameItems.stream()
                .filter(item -> bald.getBounds().intersects(item.getBounds()))
                .filter(item -> item instanceof Interactable)
                .map(item -> (Interactable) item)
                .findFirst()
                .ifPresent(interactableItem -> interactableItem.interact(bald, inventory));
    }

    boolean intersects(Combatant e1, Combatant e2) {
        return e1.getBounds().intersects(e2.getBounds());
    }

    public GameRun getGameRun() {
        return gameRun;
    }

    private void setPlayerName() {
        String nickname = "";

        while (Optional.ofNullable(nickname).isEmpty() || nickname.isBlank()) {
            nickname = javax.swing.JOptionPane.showInputDialog("Enter your nickname:");
        }
        gameRun = new GameRun(nickname, timer.getFormattedTime());
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
                drawCount++;
            }

            if (updateInterval >= 1e9) {
                currentFPS = drawCount;
                drawCount = 0;
                updateInterval = 0;
            }
        }
    }

    private void handleItemCollection() {
        gameItems.removeIf(item -> {
            if (bald.getBounds().intersects(item.getBounds())) {
                // DEBUG: Controlla le coordinate e le dimensioni per il debug delle collisioni
                System.out.printf("DEBUG: Bald Bounds: %s, Item Bounds: %s%n", bald.getBounds(), item.getBounds());
                System.out.println("DEBUG: Collisione rilevata con: " + item.getName() + " (" + item.getClass().getSimpleName() + ")");
                
                if (item instanceof Chest) {
                    System.out.println("DEBUG: Colliso con Chest, ignorato per raccolta automatica.");
                    return false; // La cassa non viene rimossa dalla handleItemCollection
                }
                
                if (item instanceof UsableItem) {
                    ((UsableItem) item).applyEffect(bald);
                    System.out.println("DEBUG: Item " + item.getName() + " (UsableItem) usato automaticamente.");
                    return true; // Gli UsableItem vengono rimossi dopo l'uso
                } else if (item instanceof InventoryItem) {
                    ((InventoryItem) item).onCollect(inventory);
                    System.out.println("DEBUG: Item " + item.getName() + " (InventoryItem) raccolto automaticamente.");
                    return true; // Gli InventoryItem vengono rimossi dopo la raccolta
                } else if (item instanceof Trap) { 
                    Trap trap = (Trap) item;
                    System.out.println("DEBUG: Colliso con Trap. isTriggered: " + trap.isTriggered());
                    if (!trap.isTriggered()) {
                        trap.interact(bald, inventory); 
                        System.out.println("DEBUG: Trap " + item.getName() + " attivata (non rimossa dalla lista).");
                    } else {
                        System.out.println("DEBUG: Trap " + item.getName() + " già attivata, ignorata.");
                    }
                    return false; // <--- CAMBIATO QUI! La trappola NON viene rimossa.
                }
            }
            return false; // Nessuna collisione, o collisione con item che non devono essere rimossi qui
        });
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

    public void update() {
        handleInput();
        bald.updateAnimation();
        bald.move();
        bald.updateBuffs(); 
    
        if (isAtMapTransitionPoint()) {
            switchToNextMap();
            return;
        }
    
        combatManager.checkEnemyAttacks();
        enemies.removeIf(enemy -> !enemy.isAlive());
        enemies.forEach(enemy -> {
            if(enemy.isCloseTo(bald)){
                enemy.followPlayer(bald);
                enemy.updateAnimation();
            }
        });
    
        combatManager.getProjectiles().forEach(Projectile::move);
        combatManager.checkProjectiles();
    
        handleItemCollection();
    
        // !!! AGGIUNGI QUESTO BLOCCO !!!
        // Aggiorna l'animazione di tutti gli item che ne hanno una
        gameItems.forEach(item -> {
            if (item instanceof Trap) { // O qualsiasi altro item che ha updateAnimation()
                ((Trap) item).updateAnimation();
            }
            // Aggiungi qui altri if per altri tipi di item animati
        });
    }

    public void changeMap(String mapName) {
        currentMapName = mapName;
        tileMap.changeMap(mapName);
        bald.setTileMap(tileMap);
        
        this.gameItems.clear(); 

        Point spawnPoint = tileMap.findSpawnPoint(5);
        if (spawnPoint != null) {
            int tileSize = tileMap.TILE_SIZE;
            bald.setX(spawnPoint.x + (tileSize - bald.getWidth()) / 2);
            bald.setY(spawnPoint.y + tileSize - bald.getHeight());
        } else {
            System.out.println("Spawn point not found!");
        }
    }

    private String getNextMapName(String currentMap) {
        return mapTransitions.get(currentMap);
    }

    private void switchToNextMap() {
        String nextMapName = getNextMapName(currentMapName);
        if (nextMapName != null) {
            changeAndLoadMap(nextMapName);
        } else {
            System.out.println("Nessuna mappa successiva definita.");
        }
    }
    
    private void changeAndLoadMap(String mapName) {
        changeMap(mapName);
        String itemFileName = "item_" + mapName + ".txt";
        this.itemSpawner = new MapItemSpawner(tileMap, this.itemFactory, itemFileName);
        this.itemSpawner.spawnItems();
        // Crea una nuova ArrayList a partire dalla lista non modificabile
        this.gameItems = new ArrayList<>(itemSpawner.getItems()); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        super.paintComponent(g2d);

        this.scaleGraphics(g2d);
        tileMap.paint(g2d);
        gridPanel.paintComponent(g2d);

        this.gameItems.forEach(item -> item.render(g2d));
        
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
        this.add(optionsPanel, optionsGBC);
        this.add(inventoryPanel, inventoryGBC);

        JButton shopButton = new JButton("Shop");
        shopButton.setBackground(Color.YELLOW);
        shopButton.setOpaque(true);
        shopButton.setFocusable(false);
        shopButton.addActionListener(e -> {
            ShopPanel shopPanel = new ShopPanel(this.combatManager, bald.getWallet());
            JOptionPane.showMessageDialog(this, shopPanel, "Negozio", JOptionPane.PLAIN_MESSAGE);
        });

        GridBagConstraints fillerGBC = new GridBagConstraints();
        fillerGBC.gridx   = 0;
        fillerGBC.gridy   = 0;
        fillerGBC.weightx = 1;
        fillerGBC.weighty = 1;
        fillerGBC.fill    = GridBagConstraints.BOTH;
        this.add(Box.createGlue(), fillerGBC);

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
}