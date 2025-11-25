package com.thelegendofbald.controller.level;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.model.entity.DummyEnemy;
import com.thelegendofbald.model.entity.FinalBoss;
import com.thelegendofbald.model.entity.LifeComponent;
import com.thelegendofbald.model.item.ItemGenerator;
import com.thelegendofbald.model.item.ItemManager;
import com.thelegendofbald.model.item.loot.LootGenerator;
import com.thelegendofbald.model.item.map.MapItemLoader;
import com.thelegendofbald.model.system.CombatManager;
import com.thelegendofbald.utils.LoggerUtils;
import com.thelegendofbald.view.render.TileMap;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Manages the game levels, including map transitions, entity spawning, and item management.
 */
public class LevelManager {

    private static final String MAP_1 = "map_1";
    private static final String MAP_2 = "map_2";
    private static final String MAP_3 = "map_3";
    private static final String MAP_4 = "map_4";

    private static final int ID_PORTAL = 4;
    private static final int ID_SPAWN = 5;
    private static final int ID_ENEMY = 7;
    private static final int ID_BOSS = 9;

    private static final int ENEMY_W = 60;
    private static final int ENEMY_H = 60;
    private static final int BOSS_W = 96;
    private static final int BOSS_H = 96;

    private static final int BOSS_HP = 500;

    private final TileMap tileMap;
    private final ItemManager itemManager;
    private final LootGenerator lootGenerator;
    private final Bald bald;
    private final CombatManager combatManager;
    private final List<DummyEnemy> enemies;

    private String currentMapName = MAP_1;
    private FinalBoss boss;

    private final Map<String, String> mapTransitions = Map.of(
            MAP_1, MAP_2,
            MAP_2, MAP_3,
            MAP_3, MAP_4);

    private final Map<String, String> reverseTransitions = Map.of(
            MAP_2, MAP_1,
            MAP_3, MAP_2);

    private Integer pendingEntryTileId;
    private Integer pendingEntryIndex;
    private Boolean pendingFacingRight;

    /**
     * Constructs a new LevelManager.
     *
     * @param tileMap       the tile map used for the game levels.
     * @param bald          the player character.
     * @param combatManager the combat manager.
     * @param enemies       the list of enemies.
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "The LevelManager needs to modify the actual instances of these objects."
    )
    public LevelManager(final TileMap tileMap, final Bald bald, final CombatManager combatManager,
            final List<DummyEnemy> enemies) {
        this.tileMap = tileMap;
        this.bald = bald;
        this.combatManager = combatManager;
        this.enemies = enemies;


        final int healthPotion = 7;
        final int strengthPotion = 8;
        final int coin = 9;

        this.lootGenerator = new LootGenerator(new ItemGenerator(),
                List.of(healthPotion, strengthPotion, coin));
        this.itemManager = new ItemManager(tileMap, new ItemGenerator(), new MapItemLoader(), lootGenerator);
    }

    /**
     * Loads the initial map and spawns the player.
     */
    public void loadInitialMap() {
        itemManager.loadItemsForMap(MAP_1);
        tileMap.changeMap(MAP_1);
        bald.setTileMap(tileMap);
        bald.setSpawnPosition(ID_SPAWN, tileMap.getTileSize());

        final Point spawnPoint = tileMap.findSpawnPoint(ID_SPAWN);
        if (spawnPoint != null) {
            final int tileSize = tileMap.getTileSize();
            bald.setPosX(spawnPoint.x + (tileSize - bald.getWidth()) / 2);
            bald.setPosY(spawnPoint.y - bald.getHeight());
        }
    }

    /**
     * Switches to the next map in the sequence.
     */
    public void switchToNextMap() {
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
     * Switches to the previous map in the sequence.
     */
    public void switchToPreviousMap() {
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

        bald.setSpeedX(0);
        bald.setSpeedY(0);

        if (pendingFacingRight != null) {
            bald.setFacingRight(pendingFacingRight);
            pendingFacingRight = null;
        }

        spawnActorsFromMap();
        itemManager.loadItemsForMap(mapName);


        combatManager.setBoss(boss);
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

        final int bossHp = BOSS_HP;
        final int bossAtk = 1;
        final LifeComponent life = new LifeComponent(bossHp);

        boss = new FinalBoss(
                x, y,
                "Final Boss",
                bossHp,
                bossAtk,
                life,
                tileMap);
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
     * Resets the level manager to its initial state.
     */
    public void reset() {
        currentMapName = MAP_1;
        tileMap.changeMap(MAP_1);
        itemManager.loadItemsForMap(MAP_1);
        spawnActorsFromMap();
        combatManager.setBoss(boss);

        bald.setTileMap(tileMap);
        bald.setSpawnPosition(ID_SPAWN, tileMap.getTileSize());
    }

    /**
     * Returns the item manager.
     *
     * @return the item manager.
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "The ItemManager is intended to be accessed and modified externally."
    )
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * Returns the final boss instance.
     *
     * @return the final boss, or null if not spawned.
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "The Boss instance needs to be accessed directly."
    )
    public FinalBoss getBoss() {
        return boss;
    }

    /**
     * Returns the name of the current map.
     *
     * @return the current map name.
     */
    public String getCurrentMapName() {
        return currentMapName;
    }

    /**
     * Returns the list of enemies.
     *
     * @return the list of enemies.
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "The enemies list needs to be accessed and modified directly."
    )
    public List<DummyEnemy> getEnemies() {
        return enemies;
    }

    /**
     * Returns the tile map.
     *
     * @return the tile map.
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "The TileMap is a heavy object and shared across controllers."
    )
    public TileMap getTileMap() {
        return tileMap;
    }

    /**
     * Checks if the player is touching a tile with the specified ID.
     *
     * @param tileId the ID of the tile to check.
     * @return true if touching, false otherwise.
     */
    public boolean isBaldTouchingTile(final int tileId) {
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
        return Optional.ofNullable(tileMap.getTileAt(tx, ty))
                .map(t -> t.getId() == id)
                .orElse(false);
    }
}
