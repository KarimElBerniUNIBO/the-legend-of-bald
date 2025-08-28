package com.thelegendofbald.model.item.map;

/**
 * Represents the data required to spawn an item on the map.
 * Contains the item ID and its position in terms of row and column.
 */
public class ItemSpawnData {

    /** The ID of the item to be spawned. */
    private final int id;

    /** The row position on the map. */
    private final int row;

    /** The column position on the map. */
    private final int col;

    /**
     * Constructs an ItemSpawnData instance with the specified item ID, row, and column.
     *
     * @param id  the ID of the item to be spawned
     * @param row the row position on the map
     * @param col the column position on the map
     */
    public ItemSpawnData(final int id, final int row, final int col) {
        this.id = id;
        this.row = row;
        this.col = col;
    }

    /** @return the item ID */
    public int getId() {
        return id;
    }

    /** @return the row position */
    public int getRow() {
        return row;
    }

    /** @return the column position */
    public int getCol() {
        return col;
    }
}
