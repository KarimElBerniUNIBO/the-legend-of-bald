package com.thelegendofbald.model.item;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics;
import java.util.List;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thelegendofbald.api.common.animation.Animatable;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.model.item.loot.LootGenerator;
import com.thelegendofbald.model.item.map.MapItemLoader;
import com.thelegendofbald.view.main.TileMap;

/**
 * Additional unit tests for ItemManager covering unmodifiable lists,
 * multiple animatables, non-intersecting item retention, and snapshot-based rendering.
 */
class ItemManagerTest {

    private ItemManager itemManager;

    @BeforeEach
    void setUp() {
        itemManager = new ItemManager(new TileMap(20, 20, 1), new ItemGenerator(), new MapItemLoader(), new LootGenerator(new ItemGenerator(), List.of(7)));
    }

    @Test
    void testGetItemsReturnsUnmodifiableList() {
        final GameItem g = new GameItem(0, 0, 10, 10, "it");
        itemManager.addItem(g);

        final java.util.List<GameItem> items = itemManager.getItems();
        final GameItem toAdd = new GameItem(1, 1, 2, 2, "x");
        assertThrows(UnsupportedOperationException.class, () -> items.add(toAdd));
    }

    @Test
    void testUpdateAllMultipleAnimatables() {
        class MultiAnim extends GameItem implements Animatable {
            boolean updated = false;

            MultiAnim(final int x, final int y) {
                super(x, y, 10, 10, "multi");
            }

            @Override
            public void updateAnimation() {
                updated = true;
            }
        }

        final MultiAnim a1 = new MultiAnim(0, 0);
        final MultiAnim a2 = new MultiAnim(5, 5);
        itemManager.addItem(a1);
        itemManager.addItem(a2);

        itemManager.updateAll();

        assertTrue(a1.updated, "First animatable should be updated");
        assertTrue(a2.updated, "Second animatable should be updated");
    }

    @Test
    void testHandleItemCollection_nonIntersectingItemNotRemoved() {
        // Bald far away so no intersection with item at 0,0
        final Bald bald = new Bald(1000, 1000, 100, "Bald", 10);
        final GameItem distant = new GameItem(0, 0, 10, 10, "distant");
        itemManager.addItem(distant);

        itemManager.handleItemCollection(bald);

        assertTrue(itemManager.getItems().contains(distant), "Non-intersecting item should remain");
    }

    @Test
    void testRenderAllAllowsAddingItemsDuringRender_dueToSnapshot() {
        final BufferedImage buf = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = buf.getGraphics();

        class TestRenderableDuringRender extends GameItem {
            boolean rendered = false;
            private final ItemManager manager;

            TestRenderableDuringRender(final int x, final int y, final ItemManager manager) {
                super(x, y, 10, 10, "renderDuring");
                this.manager = manager;
            }

            @Override
            public void render(final Graphics g) {
                rendered = true;
                // mutate manager during render to ensure snapshot protection
                manager.addItem(new GameItem(getX() + 1, getY() + 1, 5, 5, "added"));
            }
        }

        final TestRenderableDuringRender renderer = new TestRenderableDuringRender(0, 0, itemManager);
        itemManager.addItem(renderer);

        // This should not throw ConcurrentModificationException because ItemManager uses a snapshot
        itemManager.renderAll(g);

        assertTrue(renderer.rendered, "Renderer's render should be invoked");
        // The render method adds one new item to the manager
        assertTrue(itemManager.getItems().stream().anyMatch(i -> i != renderer),
            "Item added during render should be present after renderAll");
    }
}
