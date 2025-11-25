package com.thelegendofbald.view.main;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.model.inventory.Slot;
import com.thelegendofbald.view.panel.shop.ShopPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thelegendofbald.model.inventory.Inventory;
import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.model.system.CombatManager;
import com.thelegendofbald.model.system.Wallet;

/**
 * Unit tests for the {@link ShopPanel} class without using Mockito.
 * <p>
 * Uses manual stubs (inner classes) to simulate Wallet and Inventory behavior.
 */
class ShopPanelTest {

    private StubWallet wallet;
    private StubInventory inventory;
    private ShopPanel shopPanel;

    /**
     * Sets up the test environment before each test execution.
     */
    @BeforeEach
    void setUp() {
        wallet = new StubWallet(100);
        inventory = new StubInventory();
        CombatManager dummyCombatManager = null;

        shopPanel = new ShopPanel(dummyCombatManager, wallet, inventory);
    }

    /**
     * Verifies that the shop initializes with the correct gold display label.
     */
    @Test
    void testInitialGoldDisplay() {
        JLabel goldLabel = findGoldLabel();
        assertNotNull(goldLabel);
        assertEquals("Gold: 100", goldLabel.getText());
    }

    /**
     * Verifies that purchase buttons are correctly created.
     */
    @Test
    void testButtonsCreation() {
        List<JButton> buttons = findBuyButtons();
        assertEquals(3, buttons.size());
    }

    /**
     * Verifies that purchasing logic works when the player has sufficient gold.
     */
    @Test
    void testPurchaseSuccess() {
        wallet.setCoins(1000);

        List<JButton> buttons = findBuyButtons();
        assertFalse(buttons.isEmpty());
        JButton buyButton = buttons.get(0);

        buyButton.doClick();

        assertTrue(wallet.getCoins() < 1000);
        assertEquals(1, inventory.getStoredItems().size());
    }

    /**
     * Verifies that purchasing logic prevents buying when the player has insufficient gold.
     */
    @Test
    void testPurchaseFailNotEnoughGold() {
        wallet.setCoins(0);

        List<JButton> buttons = findBuyButtons();
        JButton buyButton = buttons.get(0);

        buyButton.doClick();

        assertEquals(0, wallet.getCoins());
        assertEquals(0, inventory.getStoredItems().size());
    }

    /**
     * Verifies that the gold display label updates automatically after a purchase.
     */
    @Test
    void testGoldLabelUpdate() {
        wallet.setCoins(500);

        List<JButton> buttons = findBuyButtons();
        JButton buyButton = buttons.get(0);

        buyButton.doClick();

        JLabel goldLabel = findGoldLabel();
        String expectedText = "Gold: " + wallet.getCoins();
        assertEquals(expectedText, goldLabel.getText());
    }

    /**
     * Helper method to find all "Buy Item" buttons within the panel.
     *
     * @return a list of JButtons used for purchasing items.
     */
    private List<JButton> findBuyButtons() {
        List<JButton> buttons = new ArrayList<>();
        for (Component comp : shopPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if ("Buy Item".equals(btn.getText())) {
                    buttons.add(btn);
                }
            }
        }
        return buttons;
    }

    /**
     * Helper method to find the label displaying the gold amount.
     *
     * @return the JLabel component for gold display.
     */
    private JLabel findGoldLabel() {
        for (Component comp : shopPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().startsWith("Gold:")) {
                    return label;
                }
            }
        }
        return null;
    }

    /**
     * Manual Stub for Wallet to simulate logic without Mockito.
     */
    private static class StubWallet extends Wallet {
        private int coins;

        public StubWallet(int initialCoins) {
            super();
            this.coins = initialCoins;
        }

        @Override
        public int getCoins() {
            return coins;
        }

        @Override
        public void removeCoins(int amount) {
            this.coins -= amount;
        }

        public void setCoins(int coins) {
            this.coins = coins;
        }
    }

    /**
     * Manual Stub for Inventory to simulate logic without Mockito.
     */
    private static class StubInventory implements Inventory {
        private final List<GameItem> storedItems = new ArrayList<>();

        public StubInventory() {
            super();
        }

        @Override
        public void setBald(Bald bald) {

        }

        @Override
        public void add(GameItem item) {
            storedItems.add(item);
        }

        @Override
        public void set(GameItem item, int row, int column) {

        }

        @Override
        public void remove(int row, int column) {

        }

        @Override
        public void clear() {

        }

        @Override
        public Slot get(int row, int column) {
            return null;
        }

        @Override
        public void select(int row, int column) {

        }

        @Override
        public void select(int index) {

        }

        @Override
        public void select(Slot slot) {

        }

        @Override
        public List<Slot> getSlots() {
            return List.of();
        }

        public List<GameItem> getStoredItems() {
            return storedItems;
        }
    }
}
