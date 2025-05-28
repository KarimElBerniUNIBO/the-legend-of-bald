package com.thelegendofbald.ui.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;

public class GamePanel extends JPanel {

    private final Bald bald = new Bald(60, 60, 100, "Bald", 50);
    private final DummyEnemy dummyenemy = new DummyEnemy(500, 200, 50, "ZioBilly", 50);
    private final GridPanel gridPanel;
    private final TileMap tileMap;
    private String currentMapName = "map_1";

    Timer timer = new Timer(16, e -> update());
    private final Set<Integer> pressedKeys = new HashSet<>();

    public GamePanel(Dimension size) {
        this.setPreferredSize(size);
        this.setFocusable(true);
        this.setLayout(null);

        this.gridPanel = new GridPanel();
        this.gridPanel.setOpaque(false);
        this.gridPanel.setBounds(0, 0, size.width, size.height);
        this.add(gridPanel);
        this.tileMap = new TileMap(size.width, size.height);
        tileMap.changeMap("map_1");  

        bald.setTileMap(tileMap);
        Point spawn = tileMap.findSpawnPoint(5);
        if (spawn != null) {
            bald.setX(spawn.x);
            bald.setY(spawn.y);
}
        
        this.requestFocusInWindow();

        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                updateSpeed();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                updateSpeed();
            }
        });
    }

    private void updateSpeed() {
        bald.updateAnimation();
        bald.setSpeedX(pressedKeys.contains(KeyEvent.VK_RIGHT) ? 5 :
                       pressedKeys.contains(KeyEvent.VK_LEFT) ? -5 : 0);
        bald.setSpeedY(pressedKeys.contains(KeyEvent.VK_DOWN) ? 5 :
                       pressedKeys.contains(KeyEvent.VK_UP) ? -5 : 0);
    }

    private void update() {
        bald.move();
        dummyenemy.followPlayer(bald);
        dummyenemy.updateAnimation();    

        int baldX = bald.getX();
        int baldY = bald.getY();
        int baldW = bald.getWidth();
        int baldH = bald.getHeight();
        int tileSize = tileMap.TILE_SIZE;

        // Calcolo i tile occupati dal rettangolo del personaggio
        int leftTile = baldX / tileSize;
        int rightTile = (baldX + baldW - 1) / tileSize;
        int topTile = baldY / tileSize;
        int bottomTile = (baldY + baldH - 1) / tileSize;

        for (int x = leftTile; x <= rightTile; x++) {
            for (int y = topTile; y <= bottomTile; y++) {
                if (x >= 0 && x < tileMap.getMapWidthInTiles() && y >= 0 && y < tileMap.getMapHeightInTiles()) {
                    Tile tile = tileMap.getTileAt(x, y);
                    if (tile != null) {
                        System.out.println("Tile at (" + x + "," + y + ") = " + tile + ", id = " + tile.getId());
                    } else {
                        System.out.println("Tile at (" + x + "," + y + ") is null");
                    }
                    if (tile != null && tile.getId() == 4) {
                        switchToNextMap();
                        return;  // esce subito dall'update dopo aver cambiato mappa
                    }
                }
            }
        }

        repaint();
    }


    public void changeMap(String mapName) {
        currentMapName = mapName;
        tileMap.changeMap(mapName);
        bald.setTileMap(tileMap);
    
        Point spawnPoint = tileMap.findSpawnPoint(5);
        if (spawnPoint != null) {
            int tileSize = tileMap.TILE_SIZE;

            bald.setX(spawnPoint.x + tileSize / 2 - bald.getWidth() / 2);
            bald.setY(spawnPoint.y + tileSize / 2 - bald.getHeight() / 2);
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

        tileMap.paint(g);           
        gridPanel.paintComponent(g); 
        bald.render(g);              
        dummyenemy.render(g);        
    }
}

