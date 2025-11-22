package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.function.IntSupplier;

import javax.swing.JLabel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.thelegendofbald.view.panel.shop.ShopPanel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.thelegendofbald.model.system.CombatManager;
// Assicurati di importare ShopItem corretto
import com.thelegendofbald.model.item.ShopItem;

class ShopPanelTest {

    /* -------------------- Dummies -------------------- */

    private static final class DummyCombatManager extends CombatManager {
        DummyCombatManager() {
            // Passiamo null/liste vuote per soddisfare il costruttore di CombatManager
            super(null, Collections.emptyList());
        }
    }

    /* -------------------- Helpers riflessione -------------------- */

    @SuppressWarnings("unchecked")
    private static List<ShopItem> getItems(ShopPanel p) {
        try {
            Field f = ShopPanel.class.getDeclaredField("items");
            f.setAccessible(true);
            return (List<ShopItem>) f.get(p);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static int getSelectedIndex(ShopPanel p) {
        try {
            Field f = ShopPanel.class.getDeclaredField("selectedIndex");
            f.setAccessible(true);
            return (int) f.get(p);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static JLabel getGoldLabel(ShopPanel p) {
        try {
            Field f = ShopPanel.class.getDeclaredField("goldLabel");
            f.setAccessible(true);
            return (JLabel) f.get(p);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static void setCoinsSupplier(ShopPanel p, IntSupplier sup) {
        try {
            Field f = ShopPanel.class.getDeclaredField("coinsSupplier");
            f.setAccessible(true);
            f.set(p, sup);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /* -------------------- Test -------------------- */

    @Test
    @DisplayName("Setup iniziale: size, bg, layout, label oro e popolamento items")
    void initialSetup_ok() {
        // FIX: Aggiunto il terzo parametro (Inventory) come null
        ShopPanel p = new ShopPanel(new DummyCombatManager(), null, null);

        // Preferred size / BG / layout absolute
        assertEquals(new Dimension(400, 300), p.getPreferredSize());
        assertEquals(Color.DARK_GRAY, p.getBackground());
        assertNull(p.getLayout());

        // Label oro presente
        JLabel gold = getGoldLabel(p);
        assertNotNull(gold);
        // FIX: Il codice usa "Gold:", non "Oro:"
        assertTrue(gold.getText().startsWith("Gold: "), "La label deve iniziare con 'Gold: '");

        // Lista item popolata (Sword, Axe, FireBall)
        List<?> items = getItems(p);
        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.size() >= 3, "Attesi almeno 3 item (Sword, Axe, FireBall)");
    }

    @Test
    @DisplayName("updateGoldDisplay() riflette il valore del coinsSupplier")
    void updateGoldDisplay_updatesText() {
        // FIX: Aggiunto il terzo parametro null
        ShopPanel p = new ShopPanel(new DummyCombatManager(), null, null);

        // Forziamo un supplier che ritorna 123
        setCoinsSupplier(p, () -> 123);

        p.updateGoldDisplay();
        JLabel gold = getGoldLabel(p);
        // FIX: Il codice usa "Gold:", non "Oro:"
        assertEquals("Gold: 123", gold.getText());
    }

    @Test
    @DisplayName("paint/render non lancia eccezioni")
    void paint_noThrow() {
        // FIX: Aggiunto il terzo parametro null
        ShopPanel p = new ShopPanel(new DummyCombatManager(), null, null);
        BufferedImage surface = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);

        // FIX: Rimossa la lettera 'a' che causava errore di compilazione
        assertDoesNotThrow(() -> p.paint(surface.createGraphics()));
    }

    @Test
    @DisplayName("Click su primo item imposta selectedIndex = 0")
    void mouseClick_selectsFirstItem() {
        // FIX: Aggiunto il terzo parametro null
        ShopPanel p = new ShopPanel(new DummyCombatManager(), null, null);

        // Calcolo coordinate click:
        // Nel codice: y start = 80, offset = 25.
        // Rect Y start = 80 - 25 = 55. Altezza 35. Range Y [55, 90].
        // Click Y = 80 - 25 + 1 = 56. (Valido)
        int clickX = 21;
        int clickY = 80 - 25 + 1;

        MouseEvent evt = new MouseEvent(
                p,
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                clickX,
                clickY,
                1,
                false
        );

        p.dispatchEvent(evt);

        assertEquals(0, getSelectedIndex(p), "Dopo il click il primo item deve risultare selezionato");
    }

    @Test
    @DisplayName("Deserializzazione: readObject ripristina stato 'safe' (no throw)")
    void deserialize_resetsTransient_noThrow() {
        // FIX: Aggiunto il terzo parametro null
        ShopPanel original = new ShopPanel(new DummyCombatManager(), null, null);

        assertDoesNotThrow(() -> {
            // Serializza
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(original);
            }
            byte[] bytes = bos.toByteArray();

            // Deserializza
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                Object obj = ois.readObject();
                assertTrue(obj instanceof ShopPanel);
                ShopPanel restored = (ShopPanel) obj;

                // Dopo readObject nel tuo codice: items = new ArrayList<>(), quindi è vuoto.
                List<?> items = getItems(restored);
                assertNotNull(items);
                assertTrue(items.isEmpty(), "Items deve essere vuoto (reinizializzato) dopo deserializzazione");

                setCoinsSupplier(restored, () -> 77);
                restored.updateGoldDisplay();
                JLabel gold = getGoldLabel(restored);
                assertNotNull(gold);
                // FIX: Il codice usa "Gold:", non "Oro:"
                assertEquals("Gold: 77", gold.getText());
            }
        });
    }
}