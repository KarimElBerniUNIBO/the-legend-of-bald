package com.thelegendofbald.view.panel.game;

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

import com.thelegendofbald.view.layout.GridBagConstraintsFactory;
import com.thelegendofbald.model.system.Game;
import com.thelegendofbald.model.item.Interactable;
import com.thelegendofbald.model.inventory.Inventory;
import com.thelegendofbald.view.panel.hud.LifePanel;
import com.thelegendofbald.view.panel.base.MenuPanel;
import com.thelegendofbald.view.panel.base.Panels;
import com.thelegendofbald.model.config.ControlsSettings;
import com.thelegendofbald.model.config.VideoSettings;
import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.model.entity.DummyEnemy;
import com.thelegendofbald.model.entity.FinalBoss;
import com.thelegendofbald.controller.navigation.SwitchToOtherPanel;
import com.thelegendofbald.model.entity.LifeComponent;
import com.thelegendofbald.model.system.CombatManager;
import com.thelegendofbald.model.system.DataManager;
import com.thelegendofbald.model.system.GameRun;
import com.thelegendofbald.model.system.Timer;
import com.thelegendofbald.model.system.Timer.TimeData;
import com.thelegendofbald.model.item.ItemGenerator;
import com.thelegendofbald.model.item.ItemManager;
import com.thelegendofbald.model.item.loot.LootGenerator;
import com.thelegendofbald.model.item.map.MapItemLoader;
import com.thelegendofbald.model.item.weapons.Sword;
import com.thelegendofbald.model.item.weapons.MeleeWeapon;
import com.thelegendofbald.model.item.weapons.Weapon;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.layout.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.view.panel.inventory.InventoryPanel;
import com.thelegendofbald.view.window.GameWindow;
import com.thelegendofbald.view.render.GridPanel;
import com.thelegendofbald.view.panel.shop.ShopPanel;
import com.thelegendofbald.view.render.Tile;
import com.thelegendofbald.view.render.TileMap;

/**
 * Pannello principale di gioco che gestisce il ciclo di vita, il rendering e l'input.
 * <p>
 * Questa classe funge da controller centrale per la partita attiva. Le sue responsabilità includono:
 * <ul>
 * <li>Inizializzazione e layout dei componenti UI (mappa, giocatore, nemici, HUD, inventario, shop).</li>
 * <li>Esecuzione del game loop su un thread dedicato, aggiornando la logica e richiedendo il repaint.</li>
 * <li>Gestione dell'input del giocatore tramite KeyBindings.</li>
 * <li>Gestione delle transizioni tra mappe, spawn dei nemici e caricamento oggetti.</li>
 * <li>Rendering degli elementi grafici e overlay (FPS, Timer, HUD).</li>
 * </ul>
 * </p>
 */
public final class GamePanel extends MenuPanel implements Runnable, Game {

    /** Trasparenza dello sfondo per le schermate di vittoria/sconfitta (0-255). */
    private static final int FINAL_GAME_SCREEN_TITLE_TRANSPARENCY = 150;

    /** UID per la serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Identificativo della mappa 1. */
    private static final String MAP_1 = "map_1";
    /** Identificativo della mappa 2. */
    private static final String MAP_2 = "map_2";
    /** Identificativo della mappa 3. */
    private static final String MAP_3 = "map_3";
    /** Identificativo della mappa 4. */
    private static final String MAP_4 = "map_4";

    /** Larghezza predefinita del pannello di gioco. */
    private static final int DEFAULT_W = 1280;
    /** Altezza predefinita del pannello di gioco. */
    private static final int DEFAULT_H = 704;

    /** Larghezza del pannello della vita. */
    private static final int LIFE_W = 200;
    /** Altezza del pannello della vita. */
    private static final int LIFE_H = 20;
    /** Posizione Y del pannello della vita. */
    private static final int LIFE_Y = 800;

    /** Dimensione del lato di un tile in pixel. */
    private static final int TILE_SIZE = 32;
    /** Frame rate massimo predefinito. */
    private static final int DEFAULT_MAX_FPS = 60;

    /** Larghezza del personaggio Bald. */
    private static final int BALD_W = 60;
    /** Altezza del personaggio Bald. */
    private static final int BALD_H = 60;

    /** Larghezza standard di un nemico. */
    private static final int ENEMY_W = 60;
    /** Altezza standard di un nemico. */
    private static final int ENEMY_H = 60;

    /** Larghezza del boss finale. */
    private static final int BOSS_W = 96;
    /** Altezza del boss finale. */
    private static final int BOSS_H = 96;

    /** ID del tile portale. */
    private static final int ID_PORTAL = 4;
    /** ID del tile di spawn. */
    private static final int ID_SPAWN = 5;
    /** ID del tile negozio. */
    private static final int ID_SHOP = 6;
    /** ID del tile generatore di nemici. */
    private static final int ID_ENEMY = 7;
    /** ID del tile portale precedente. */
    private static final int ID_PREV_PORTAL = 8;
    /** ID del tile boss. */
    private static final int ID_BOSS = 9;
    /** ID del tile trigger mappa successiva. */
    private static final int ID_NEXT_MAP_TRIGGER = 10;

    /** Percentuale di insets per la larghezza del pannello opzioni. */
    private static final double OPTIONS_WIDTH_INSETS = 0.25;
    /** Percentuale di insets per l'altezza del pannello opzioni. */
    private static final double OPTIONS_HEIGHT_INSETS = 0.1;
    /** Percentuale di insets per la larghezza del pannello inventario. */
    private static final double INVENTORY_WIDTH_INSETS = 0.25;
    /** Percentuale di insets per l'altezza del pannello inventario. */
    private static final double INVENTORY_HEIGHT_INSETS = 0.25;

    /** Font predefinito per HUD e overlay. */
    private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);
    /** Posizione a schermo del contatore FPS. */
    private static final Pair<Integer, Integer> FPS_POSITION = Pair.of(15, 25);
    /** Posizione a schermo del timer di gioco. */
    private static final Pair<Integer, Integer> TIMER_POSITION = Pair.of(1085, 25);
    /** Colore dell'area di attacco visualizzata per debug/feedback. */
    private static final Color ATTACK_AREA_COLOR = new Color(200, 200, 200, 100);

    /** Velocità di movimento del giocatore. */
    private static final double MOVE_SPEED = 2.0;

    /** Nanosecondi in un secondo. */
    private static final long NANOS_IN_SECOND = 1_000_000_000L;
    /** Millisecondi in un secondo. */
    private static final long MILLIS_IN_SECOND = 1000L;
    /** Nanosecondi in un millisecondo. */
    private static final long NANOS_IN_MILLI = 1_000_000L;
    /** Intervallo di sleep del thread quando il gioco è in pausa. */
    private static final long SLEEP_INTERVAL_WHEN_PAUSED = 100L;
    /** Tempo di attesa minimo per loop in caso di frame in ritardo. */
    private static final long LATE_FRAME_BACKOFF_NANOS = 250_000L;
    /** Tempo di cooldown per l'utilizzo dei portali in millisecondi. */
    private static final long PORTAL_COOLDOWN_MS = 2000;

    /** Dimensione icona arma. */
    private static final int WEAPON_ICON = 50;

    /** Passo incremento movimento X. */
    private static final double DX_STEP = 1.0;
    /** Passo incremento movimento Y. */
    private static final double DY_STEP = 1.0;

    /** Colonne inventario. */
    private static final int INVENTORY_COLS = 5;
    /** Righe inventario. */
    private static final int INVENTORY_ROWS = 3;

    /** ID Pozione salute. */
    private static final int HEALTH_POTION = 7;
    /** ID Pozione forza. */
    private static final int STRENGTH_POTION = 8;
    /** ID Moneta. */
    private static final int COIN = 9;

    /** Timestamp fino al quale i portali sono disabilitati. */
    private volatile long portalCooldownUntil;

    /** ID del tile di destinazione pendente per il cambio mappa. */
    private Integer pendingEntryTileId;
    /** Indice dell'entry point pendente. */
    private Integer pendingEntryIndex;

    /** Direzione pendente del giocatore dopo il cambio mappa. */
    private Boolean pendingFacingRight;

    /** Factory per la creazione dei vincoli di layout. */
    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    /** Vincoli layout per il pannello opzioni. */
    private final GridBagConstraints optionsGBC = gbcFactory.createBothGridBagConstraints();
    /** Vincoli layout per il pannello inventario. */
    private final GridBagConstraints inventoryGBC = gbcFactory.createBothGridBagConstraints();

    /** Istanza del giocatore. */
    private final transient Bald bald = new Bald(BALD_W, BALD_H, 100, "Bald", 50);
    /** Nome della mappa corrente. */
    private String currentMapName = MAP_1;

    /** Mappa delle transizioni in avanti tra livelli. */
    private final Map<String, String> mapTransitions = Map.of(
            MAP_1, MAP_2,
            MAP_2, MAP_3,
            MAP_3, MAP_4);

    /** Mappa delle transizioni all'indietro tra livelli. */
    private final Map<String, String> reverseTransitions = Map.of(
            MAP_2, MAP_1,
            MAP_3, MAP_2);

    /** Pannello per la griglia di sfondo. */
    private final GridPanel gridPanel;
    /** Gestore della mappa a tile. */
    private final transient TileMap tileMap;
    /** Gestore degli oggetti sulla mappa. */
    private final transient ItemManager itemManager;
    /** Generatore di loot. */
    private final transient LootGenerator lootGenerator;

    /** Pannello HUD per la vita del giocatore. */
    private final LifePanel lifePanel;
    /** Lista dei nemici attivi nella mappa corrente. */
    private transient List<DummyEnemy> enemies = new ArrayList<>();
    /** Pannello delle opzioni di gioco (pausa). */
    private final JPanel optionsPanel;
    /** Timer di gioco. */
    private final transient Timer timer = new Timer();
    /** Dati della partita corrente (nome, tempo). */
    private transient GameRun gameRun;
    /** Gestore del combattimento. */
    private final transient CombatManager combatManager;
    /** Gestore del salvataggio dati. */
    private final transient DataManager saveDataManager = new DataManager();

    /** Pannello visuale dell'inventario. */
    private final JPanel inventoryPanel;
    /** Modello logico dell'inventario. */
    private final transient Inventory inventory;

    /** Thread principale del game loop. */
    private transient Thread gameThread;
    /** Flag di esecuzione del gioco. */
    private volatile boolean running;
    /** Flag di stato game over. */
    private volatile boolean gameOver;
    /** Flag di stato vittoria. */
    private volatile boolean gameWon;
    /** Flag di pausa. */
    private volatile boolean paused;
    /** Limite FPS desiderato. */
    private volatile int maxFPS = DEFAULT_MAX_FPS;
    /** Flag per mostrare/nascondere FPS. */
    private volatile boolean showingFPS = (boolean) VideoSettings.SHOW_FPS.getValue();
    /** Valore FPS corrente. */
    private volatile int currentFPS;
    /** Flag per mostrare/nascondere il timer. */
    private volatile boolean showingTimer = (boolean) VideoSettings.SHOW_TIMER.getValue();

    /** Insieme dei tasti attualmente premuti. */
    private final Set<Integer> pressedKeys = new HashSet<>();
    /** Pulsante per aprire lo shop. */
    private final JButton shopButton = new JButton("Shop");
    /** Pulsante per tornare al menu principale. */
    private final JButton mainMenuButton = new JButton("Ritorna alla pagina principale");
    /** Pannello dello shop. */
    private final ShopPanel shopPanel;

    /** Riferimento al boss finale (se presente nella mappa). */
    private transient FinalBoss boss;

    /**
     * Costruisce il pannello di gioco principale e inizializza i sistemi core e l'UI.
     * <p>
     * Configura la tile map, il giocatore, l'inventario, i gestori di item e loot,
     * e prepara la mappa iniziale. Imposta anche i key bindings e la posizione di spawn.
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

        this.lootGenerator = new LootGenerator(new ItemGenerator(), List.of(HEALTH_POTION, STRENGTH_POTION, COIN));
        this.itemManager = new ItemManager(tileMap, new ItemGenerator(), new MapItemLoader(), lootGenerator);
        this.itemManager.loadItemsForMap(MAP_1);

        tileMap.changeMap(MAP_1);
        bald.setTileMap(tileMap);
        bald.setSpawnPosition(ID_SPAWN, tileMap.getTileSize());

        shopButton.setText("Shop");
        shopButton.setBackground(Color.YELLOW);
        shopButton.setOpaque(true);
        shopButton.setFocusable(false);
        shopButton.setVisible(false);
        shopButton.addActionListener(this::onShopButtonClicked);

        shopPanel = new ShopPanel(this.combatManager, bald.getWallet(), this.inventory);

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

    /**
     * Inizializzazione post-costruzione eseguita nell'EDT.
     */
    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setBackground(Color.BLACK);
            this.setFocusable(true);
            this.setLayout(new GridBagLayout());
        });
    }

    /**
     * Gestisce la deserializzazione per reinizializzare i campi transienti.
     *
     * @param in stream di input
     * @throws IOException errore I/O
     * @throws ClassNotFoundException classe non trovata
     */
    private void readObject(final ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.enemies = new ArrayList<>();
    }

    /**
     * Aggiunge le armi di base all'inventario del giocatore.
     */
    private void addWeaponsToInventory() {
        final Weapon sword = new Sword(0, 0, WEAPON_ICON, WEAPON_ICON, combatManager);
        inventory.add(sword);
    }

    /**
     * Mostra o nasconde il pannello delle opzioni (menu di pausa).
     */
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

    /**
     * Mostra o nasconde il pannello dell'inventario.
     */
    private void toggleOpenInventory() {
        if (!paused && !optionsPanel.isVisible() && !inventoryPanel.isVisible()) {
            inventoryPanel.setVisible(true);
        } else if (inventoryPanel.isVisible()) {
            inventoryPanel.setVisible(false);
        }
    }

    /**
     * Apre il pannello opzioni e mette in pausa il gioco.
     */
    private void openOptionsPanel() {
        pauseGame();
        pressedKeys.clear();
        this.optionsPanel.setVisible(true);
        this.optionsPanel.requestFocusInWindow();
        this.repaint();
    }

    /**
     * Chiude il pannello opzioni e riprende il gioco.
     */
    private void closeOptionsPanel() {
        this.optionsPanel.setVisible(false);
        pressedKeys.clear();
        resumeGame();
        this.requestFocusInWindow();
        this.repaint();
    }

    /**
     * Aggiorna i binding dei tasti in base alle impostazioni correnti.
     */
    public void refreshKeyBindings() {
        pressedKeys.clear();
        setupKeyBindings();
    }

    /**
     * Configura la mappa degli input e delle azioni per la gestione della tastiera.
     */
    private void setupKeyBindings() {
        final InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        final ActionMap am = this.getActionMap();
        im.clear();
        am.clear();

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

        bindKey(im, am, "released UP", ControlsSettings.UP.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.UP.getKey()));
        bindKey(im, am, "released DOWN", ControlsSettings.DOWN.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.DOWN.getKey()));
        bindKey(im, am, "released LEFT", ControlsSettings.LEFT.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.LEFT.getKey()));
        bindKey(im, am, "released RIGHT", ControlsSettings.RIGHT.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.RIGHT.getKey()));
    }

    /**
     * Helper per associare un tasto ad un'azione Runnable.
     *
     * @param im InputMap
     * @param am ActionMap
     * @param name nome dell'azione
     * @param key codice tasto
     * @param pressed true per evento pressione, false per rilascio
     * @param action l'azione da eseguire
     */
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
     * Gestisce l'input di movimento calcolando il vettore di velocità del giocatore.
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

    /**
     * Gestisce l'interazione con oggetti interagibili vicini al giocatore.
     */
    private void interactWithItems() {
        itemManager.getItems().stream()
                .filter(item -> bald.getBounds().intersects(item.getBounds()))
                .filter(item -> item instanceof Interactable)
                .map(item -> (Interactable) item)
                .findFirst()
                .ifPresent(Interactable::interact);
    }

    /**
     * Imposta la direzione del giocatore durante le transizioni tra mappe specifiche.
     *
     * @param from mappa di origine
     * @param to mappa di destinazione
     */
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
     * Restituisce i dati della partita corrente.
     *
     * @return istanza di {@link GameRun}
     */
    public GameRun getGameRun() {
        return gameRun;
    }

    /**
     * Richiede al giocatore di inserire un nickname prima di iniziare la partita.
     */
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

    /**
     * Resetta lo stato del gioco ai valori iniziali (mappa 1, inventario base, ecc.).
     */
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
     * Aggiorna la logica di gioco (movimento, collisioni, combat).
     *
     * @param deltatime tempo trascorso dall'ultimo frame in secondi.
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
     * Gestisce la logica di vittoria: pausa, salvataggio e mostra schermata finale.
     */
    private void handleGameWon() {
        this.gameWon = true;
        this.pauseGame();
        this.saveGame();
        this.pressedKeys.clear();
        this.mainMenuButton.setVisible(true);
    }

    /**
     * Gestisce la logica di sconfitta: pausa e mostra schermata game over.
     */
    private void handleGameOver() {
        this.gameOver = true;
        this.pauseGame();
        this.pressedKeys.clear();
        this.mainMenuButton.setVisible(true);
    }

    /**
     * Verifica se il giocatore sta toccando o è adiacente ad un tile specifico.
     *
     * @param tileId ID del tile da cercare
     * @return true se c'è contatto/adiacenza
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

    /**
     * Verifica se un tile alle coordinate specificate ha un certo ID.
     *
     * @param tx coordinata x (colonna)
     * @param ty coordinata y (riga)
     * @param id identificativo del tile da controllare
     * @return true se il tile corrisponde all'ID, false altrimenti
     */
    private boolean tileHasId(final int tx, final int ty, final int id) {
        final Tile t = tileMap.getTileAt(tx, ty);
        return t != null && t.getId() == id;
    }

    /**
     * Passa alla mappa successiva definita nelle transizioni.
     */
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

    /**
     * Passa alla mappa precedente definita nelle transizioni.
     */
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

    /**
     * Carica la nuova mappa e riposiziona il giocatore.
     *
     * @param mapName nome della mappa da caricare
     */
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

    /**
     * Spawna i nemici e il boss in base ai tile della mappa corrente.
     */
    private void spawnActorsFromMap() {
        enemies.clear();
        boss = null;

        final List<Point> enemyTiles = tileMap.findAllWithId(ID_ENEMY);
        final List<Point> bossTiles = tileMap.findAllWithId(ID_BOSS);

        enemyTiles.forEach(this::spawnEnemyAt);
        bossTiles.stream().findFirst().ifPresent(this::spawnBossAt);
    }

    /**
     * Crea un nemico standard alle coordinate specificate.
     *
     * @param topLeft punto in alto a sinistra del tile di spawn
     */
    private void spawnEnemyAt(final Point topLeft) {
        final int ts = tileMap.getTileSize();
        final int x = topLeft.x + (ts - ENEMY_W) / 2;
        final int y = topLeft.y + (ts - ENEMY_H) / 2;
        enemies.add(new DummyEnemy(x, y, ENEMY_W, "ZioBilly", 10, tileMap));
    }

    /**
     * Crea il boss finale alle coordinate specificate.
     *
     * @param topLeft punto in alto a sinistra del tile di spawn
     */
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

    /**
     * Disegna la schermata di vittoria in sovraimpressione.
     *
     * @param g2d contesto grafico
     */
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

    /**
     * Disegna la schermata di game over in sovraimpressione.
     *
     * @param g2d contesto grafico
     */
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

    /**
     * Disegna l'area di attacco dell'arma corrente (debug/feedback).
     *
     * @param g contesto grafico
     */
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

    /**
     * Disegna la barra HP del boss in cima allo schermo.
     *
     * @param g2d contesto grafico
     */
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

    /**
     * Disegna il contatore FPS.
     *
     * @param g contesto grafico
     */
    private void drawFPS(final Graphics g) {
        if (showingFPS) {
            g.setColor(Color.YELLOW);
            g.setFont(DEFAULT_FONT);
            g.drawString("FPS: " + currentFPS,
                    FPS_POSITION.getLeft(), FPS_POSITION.getRight());
        }
    }

    /**
     * Disegna il timer di gioco.
     *
     * @param g contesto grafico
     */
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

    /**
     * Scala il contesto grafico in base alla dimensione della finestra corrente.
     *
     * @param g contesto grafico
     */
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

    /**
     * Callback per il click sul pulsante shop.
     *
     * @param event l'evento di azione scatenato dal click
     */
    private void onShopButtonClicked(final java.awt.event.ActionEvent event) {
        Objects.requireNonNull(event, "event");
        JOptionPane.showMessageDialog(this, shopPanel, "SHOP", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Mostra o nasconde il pulsante shop se il giocatore è sopra un tile negozio.
     */
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
     * Imposta la visibilità del timer.
     *
     * @param showingTimer true per mostrare il timer
     */
    public void setShowingTimer(final boolean showingTimer) {
        this.showingTimer = showingTimer;
    }
}
