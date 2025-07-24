package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.api.settingsmenu.KeybindsSettings;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;

public class GamePanel extends MenuPanel {

    private final Bald bald = new Bald(60, 60, 100, "Bald", 50);
    private final DummyEnemy dummyenemy = new DummyEnemy(500, 200, 50, "ZioBilly", 50);
    private final TileMap tileMap;
    private String currentMapName = "map_1";
    private final JButton shopButton = new JButton("Apri Shop");
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Timer timer;

    public GamePanel() {
        super();
        Dimension size = new Dimension(1280, 704);
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(null);

        this.tileMap = new TileMap(size.width, size.height, 32);

        // Pulsante shop sopra la mappa
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

        this.requestFocusInWindow();

        timer = new Timer(16, e -> update());
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

    // --- LOGICA SHOP BUTTON ---
    boolean nearShop = false;
    int[][] directions = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} }; // sopra, sotto, sinistra, destra
    for (int[] dir : directions) {
        int nx = tileCenterX + dir[0];
        int ny = tileFeetY + dir[1];
        Tile t = tileMap.getTileAt(nx, ny);
        if (t != null && t.getId() == 6) {
            nearShop = true;
            // Posiziona il bottone accanto a Bald (a destra), ma sempre dentro il pannello
            int maxX = getWidth() - shopButton.getWidth();
            int maxY = getHeight() - shopButton.getHeight();
            int btnX = Math.min(baldX + baldW + 10, maxX);
            int btnY = Math.max(0, Math.min(baldY, maxY));
            shopButton.setLocation(btnX, btnY);
            System.out.println("Bottone shop posizionato a: " + btnX + "," + btnY);
            break;
        }
    }
    shopButton.setVisible(nearShop);
    System.out.println("ShopButton visible: " + shopButton.isVisible());
    System.out.println("nearShop: " + nearShop);

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
        super.paintComponent(g);
        // Esempio: disegna un quadrato rosso per test
        g.setColor(Color.RED);
        g.fillRect(10, 10, 100, 100);

        // Disegna la mappa e i personaggi
        if (tileMap != null) tileMap.paint(g);
        if (bald != null) bald.render(g);
        if (dummyenemy != null) dummyenemy.render(g);
    }

    @Override
    public void updateComponentsSize() {}

    @Override
    public void addComponentsToPanel() {}
}