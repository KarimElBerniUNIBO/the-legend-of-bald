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
<<<<<<< HEAD
import com.thelegendofbald.model.item.ShopItem;
=======
import com.thelegendofbald.model.system.Wallet;
>>>>>>> f2bbfff79374462da6c37903b0911f2b7b6949da

/**
 * Unit tests for the {@link ShopPanel} class without using Mockito.
 * <p>
 * Uses manual stubs (inner classes) to simulate Wallet and Inventory behavior.
 */
class ShopPanelTest {

    private StubWallet wallet;
    private StubInventory inventory;
    private ShopPanel shopPanel;

<<<<<<< HEAD
    private static final class DummyCombatManager extends CombatManager {
        DummyCombatManager() {
            super(null, Collections.emptyList());
        }
=======
    /**
     * Sets up the test environment before each test execution.
     */
    @BeforeEach
    void setUp() {
        wallet = new StubWallet(100);
        inventory = new StubInventory();
        CombatManager dummyCombatManager = null;

        shopPanel = new ShopPanel(dummyCombatManager, wallet, inventory);
>>>>>>> f2bbfff79374462da6c37903b0911f2b7b6949da
    }

    /**
     * Verifies that the shop initializes with the correct gold display label.
     */
    @Test
<<<<<<< HEAD
    @DisplayName("Setup iniziale: size, bg, layout, label oro e popolamento items")
    void initialSetup_ok() {
        ShopPanel p = new ShopPanel(new DummyCombatManager(), null, null);

        assertEquals(new Dimension(400, 300), p.getPreferredSize());
        assertEquals(Color.DARK_GRAY, p.getBackground());
        assertNull(p.getLayout());

        JLabel gold = getGoldLabel(p);
        assertNotNull(gold);
        assertTrue(gold.getText().startsWith("Gold: "), "La label deve iniziare con 'Gold: '");

        List<?> items = getItems(p);
        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.size() >= 3, "Attesi almeno 3 item (Sword, Axe, FireBall)");
=======
    void testInitialGoldDisplay() {
        JLabel goldLabel = findGoldLabel();
        assertNotNull(goldLabel);
        assertEquals("Gold: 100", goldLabel.getText());
>>>>>>> f2bbfff79374462da6c37903b0911f2b7b6949da
    }

    /**
     * Verifies that purchase buttons are correctly created.
     */
    @Test
<<<<<<< HEAD
    @DisplayName("updateGoldDisplay() riflette il valore del coinsSupplier")
    void updateGoldDisplay_updatesText() {
        ShopPanel p = new ShopPanel(new DummyCombatManager(), null, null);

        setCoinsSupplier(p, () -> 123);

        p.updateGoldDisplay();
        JLabel gold = getGoldLabel(p);
        assertEquals("Gold: 123", gold.getText());
=======
    void testButtonsCreation() {
        List<JButton> buttons = findBuyButtons();
        assertEquals(3, buttons.size());
>>>>>>> f2bbfff79374462da6c37903b0911f2b7b6949da
    }

    /**
     * Verifies that purchasing logic works when the player has sufficient gold.
     */
    @Test
<<<<<<< HEAD
    @DisplayName("paint/render non lancia eccezioni")
    void paint_noThrow() {
        ShopPanel p = new ShopPanel(new DummyCombatManager(), null, null);
        BufferedImage surface = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);

        assertDoesNotThrow(() -> p.paint(surface.createGraphics()));
=======
    void testPurchaseSuccess() {
        wallet.setCoins(1000);

        List<JButton> buttons = findBuyButtons();
        assertFalse(buttons.isEmpty());
        JButton buyButton = buttons.get(0);

        buyButton.doClick();

        assertTrue(wallet.getCoins() < 1000);
        assertEquals(1, inventory.getStoredItems().size());
>>>>>>> f2bbfff79374462da6c37903b0911f2b7b6949da
    }

    /**
     * Verifies that purchasing logic prevents buying when the player has insufficient gold.
     */
    @Test
<<<<<<< HEAD
    @DisplayName("Click su primo item imposta selectedIndex = 0")
    void mouseClick_selectsFirstItem() {
        ShopPanel p = new ShopPanel(new DummyCombatManager(), null, null);

        int clickX = 21;
        int clickY = 80 - 25 + 1;
=======
    void testPurchaseFailNotEnoughGold() {
        wallet.setCoins(0);

        List<JButton> buttons = findBuyButtons();
        JButton buyButton = buttons.get(0);
>>>>>>> f2bbfff79374462da6c37903b0911f2b7b6949da

        buyButton.doClick();

        assertEquals(0, wallet.getCoins());
        assertEquals(0, inventory.getStoredItems().size());
    }

    /**
     * Verifies that the gold display label updates automatically after a purchase.
     */
    @Test
<<<<<<< HEAD
    @DisplayName("Deserializzazione: readObject ripristina stato 'safe' (no throw)")
    void deserialize_resetsTransient_noThrow() {
        ShopPanel original = new ShopPanel(new DummyCombatManager(), null, null);

        assertDoesNotThrow(() -> {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(original);
            }
            byte[] bytes = bos.toByteArray();

            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                Object obj = ois.readObject();
                assertTrue(obj instanceof ShopPanel);
                ShopPanel restored = (ShopPanel) obj;

                List<?> items = getItems(restored);
                assertNotNull(items);
                assertTrue(items.isEmpty(), "Items deve essere vuoto (reinizializzato) dopo deserializzazione");

                setCoinsSupplier(restored, () -> 77);
                restored.updateGoldDisplay();
                JLabel gold = getGoldLabel(restored);
                assertNotNull(gold);
                assertEquals("Gold: 77", gold.getText());
            }
        });
=======
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
>>>>>>> f2bbfff79374462da6c37903b0911f2b7b6949da
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
