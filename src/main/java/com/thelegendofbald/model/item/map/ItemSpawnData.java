package com.thelegendofbald.model.item.map;

/**
 * Represents the data required to spawn an item on the map.
 * Contains the item ID and its position in terms of row and column.
 */
public class ItemSpawnData {
    public final int id;
    public final int row;
    public final int col;

    /**
     * Constructs an ItemSpawnData instance with the specified item ID, row, and column.
     *
     * @param id  the ID of the item to be spawned
     * @param row the row position on the map
     * @param col the column position on the map
     */
    public ItemSpawnData(final int id,final int row,final int col) {
        this.id = id;
        this.row = row;
        this.col = col;
    }
}