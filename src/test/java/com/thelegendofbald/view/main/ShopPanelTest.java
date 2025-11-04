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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.thelegendofbald.model.combat.CombatManager;

class ShopPanelTest {

    /* -------------------- Dummies -------------------- */

    /**
     * CombatManager nella tua codebase è una CLASSE, non un'interfaccia.
     * Quindi qui la estendiamo.
     *
     * NOTA: se la tua CombatManager NON ha costruttore vuoto,
     * sostituisci 'super();' con i parametri richiesti, es. 'super(dep1, dep2, ...);'
     */
    private static final class DummyCombatManager extends CombatManager {
        DummyCombatManager() {
            super(null, Collections.emptyList()); // <-- se non compila, passa i parametri necessari al costruttore reale
        }
    }

    /* -------------------- Helpers riflessione -------------------- */

    @SuppressWarnings("unchecked")
    private static List<com.thelegendofbald.model.item.ShopItem> getItems(ShopPanel p) {
        try {
            Field f = ShopPanel.class.getDeclaredField("items");
            f.setAccessible(true);
            return (List<com.thelegendofbald.model.item.ShopItem>) f.get(p);
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
        ShopPanel p = new ShopPanel(new DummyCombatManager(), /* wallet */ null);

        // Preferred size / BG / layout absolute
        assertEquals(new Dimension(400, 300), p.getPreferredSize());
        assertEquals(Color.DARK_GRAY, p.getBackground());
        assertNull(p.getLayout());

        // Label oro presente
        JLabel gold = getGoldLabel(p);
        assertNotNull(gold);
        assertTrue(gold.getText().startsWith("Oro: "));

        // Lista item popolata (Sword, Axe, FireBall)
        List<?> items = getItems(p);
        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertTrue(items.size() >= 3, "Attesi almeno 3 item (Sword, Axe, FireBall)");
    }

    @Test
    @DisplayName("updateGoldDisplay() riflette il valore del coinsSupplier")
    void updateGoldDisplay_updatesText() {
        ShopPanel p = new ShopPanel(new DummyCombatManager(), /* wallet */ null);

        // Forziamo un supplier che ritorna 123
        setCoinsSupplier(p, () -> 123);

        p.updateGoldDisplay();
        JLabel gold = getGoldLabel(p);
        assertEquals("Oro: 123", gold.getText());
    }

    @Test
    @DisplayName("paint/render non lancia eccezioni")
    void paint_noThrow() {
        ShopPanel p = new ShopPanel(new DummyCombatManager(), /* wallet */ null);
        BufferedImage surface = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);

        assertDoesNotThrow(() -> p.paint(surface.createGraphics()));
    }

    @Test
    @DisplayName("Click su primo item imposta selectedIndex = 0")
    void mouseClick_selectsFirstItem() {
        ShopPanel p = new ShopPanel(new DummyCombatManager(), /* wallet */ null);

        // Primo item: rettangolo x=20..(400-20), y=(80-25)..(80-25+35)
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
        ShopPanel original = new ShopPanel(new DummyCombatManager(), /* wallet */ null);

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

                // Dopo readObject: items vuoto, coinsSupplier funzionante, goldLabel presente
                List<?> items = getItems(restored);
                assertNotNull(items);
                assertTrue(items.isEmpty(), "Items deve essere vuoto dopo deserializzazione");

                setCoinsSupplier(restored, () -> 77);
                restored.updateGoldDisplay();
                JLabel gold = getGoldLabel(restored);
                assertNotNull(gold);
                assertEquals("Oro: 77", gold.getText());
            }
        });
    }
}
