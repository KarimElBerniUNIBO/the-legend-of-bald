package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.common.Wallet;
import com.thelegendofbald.model.item.ShopItem;
import com.thelegendofbald.model.item.weapons.Axe;
import com.thelegendofbald.model.item.weapons.FireBall;
import com.thelegendofbald.model.item.weapons.Sword;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * Pannello negozio: mostra una lista di oggetti acquistabili e l'oro disponibile.
 * <p>La classe è <b>final</b> e non è pensata per estensione.</p>
 */
public final class ShopPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /* ====== Costanti UI (no magic numbers) ====== */
    private static final int PANEL_W = 400;
    private static final int PANEL_H = 300;

    private static final int PADDING = 20;
    private static final int TITLE_Y = 40;
    private static final int LIST_START_Y = 80;
    private static final int LIST_SPACING = 50;

    private static final int ITEM_RECT_X = 20;
    private static final int ITEM_RECT_H = 35;
    private static final int ITEM_RECT_ARC = 10;
    private static final int ITEM_RECT_OFFSET_Y = 25;
    private static final int ITEM_IMAGE_X = 30;
    private static final int ITEM_IMAGE_W = 24;
    private static final int ITEM_IMAGE_H = 24;
    private static final int ITEM_TEXT_X = 60;

    private static final int DESC_Y_OFFSET = 30;

    private static final int GOLD_LABEL_FONT_SIZE = 14;
    private static final int TITLE_FONT_SIZE = 22;
    private static final int ITEM_FONT_SIZE  = 16;
    private static final int DESC_FONT_SIZE  = 14;

    private static final int GOLD_LABEL_BOTTOM_Y = 270; // posizione verticale costante
    private static final int GOLD_LABEL_RIGHT_OFFSET = 20;

    private static final Color BG_COLOR = Color.DARK_GRAY;
    private static final Color FG_COLOR = Color.WHITE;
    private static final Color SELECT_COLOR = new Color(70, 130, 180);
    private static final Color DESC_COLOR = Color.LIGHT_GRAY;

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, TITLE_FONT_SIZE);
    private static final Font ITEM_FONT  = new Font("Arial", Font.PLAIN, ITEM_FONT_SIZE);
    private static final Font DESC_FONT  = new Font("Arial", Font.ITALIC, DESC_FONT_SIZE);
    private static final Font GOLD_FONT  = new Font("Monospaced", Font.BOLD, GOLD_LABEL_FONT_SIZE);

    /* ====== Stato ====== */
    // Transient: runtime only; non serve serializzarli.
    private transient List<ShopItem> items;
    // Evitiamo di memorizzare direttamente Wallet (niente EI2).
    private transient IntSupplier coinsSupplier;

    private int selectedIndex = -1;
    private JLabel goldLabel;

    /**
     * Crea il pannello negozio.
     *
     * @param combatManager gestore del combattimento (usato dagli item)
     * @param wallet        portafoglio del giocatore (usato solo per leggere le monete)
     */
    public ShopPanel(final CombatManager combatManager, final Wallet wallet) {
        initRuntimeState(combatManager, wallet);

        // Setup UI
        setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        setBackground(BG_COLOR);
        setLayout(null); // layout assoluto

        // Etichetta oro in basso a destra
        this.goldLabel = new JLabel("Oro: " + coinsSupplier.getAsInt());
        goldLabel.setFont(GOLD_FONT);
        goldLabel.setForeground(Color.YELLOW);
        final Dimension size = goldLabel.getPreferredSize();
        goldLabel.setBounds(PANEL_W - size.width - GOLD_LABEL_RIGHT_OFFSET,
                GOLD_LABEL_BOTTOM_Y, size.width, size.height);
        add(goldLabel);

        // Click sugli oggetti
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                int y = LIST_START_Y;
                for (int i = 0; i < items.size(); i++) {
                    final Rectangle rect = new Rectangle(
                            ITEM_RECT_X, y - ITEM_RECT_OFFSET_Y,
                            PANEL_W - 2 * ITEM_RECT_X, ITEM_RECT_H);
                    if (rect.contains(e.getPoint())) {
                        selectedIndex = i;
                        repaint();
                        LoggerUtils.info("Selezionato: " + items.get(i).getDisplayName());
                        break;
                    }
                    y += LIST_SPACING;
                }
            }
        });
    }

    /**
     * Inizializza lo stato runtime (lista item e supplier delle monete).
     *
     * @param combatManager gestore del combattimento usato per popolare gli item
     * @param wallet        portafoglio del giocatore da cui leggere le monete
     */
    private void initRuntimeState(final CombatManager combatManager, final Wallet wallet) {
        this.items = new ArrayList<>();
        this.coinsSupplier = wallet != null ? wallet::getCoins : () -> 0;

        // Popola con item reali (implementano ShopItem)
        items.add(new Sword(0, 0, 32, 32, combatManager));
        items.add(new Axe(0, 0, 32, 32, combatManager));
        items.add(new FireBall(0, 0, 32, 32, combatManager));
    }


    /** Aggiorna il testo della label che mostra l'oro corrente. */
    public void updateGoldDisplay() {
        goldLabel.setText("Oro: " + coinsSupplier.getAsInt());
        goldLabel.repaint();
    }

    /**
     * Disegna la UI del negozio.
     *
     * @param g contesto grafico
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Titolo
        g2.setColor(FG_COLOR);
        g2.setFont(TITLE_FONT);
        g2.drawString("Negozio", PADDING, TITLE_Y);

        // Lista item
        int y = LIST_START_Y;
        for (int i = 0; i < items.size(); i++) {
            final ShopItem item = items.get(i);

            if (i == selectedIndex) {
                g2.setColor(SELECT_COLOR);
                g2.fillRoundRect(
                        ITEM_RECT_X, y - ITEM_RECT_OFFSET_Y,
                        PANEL_W - 2 * ITEM_RECT_X, ITEM_RECT_H,
                        ITEM_RECT_ARC, ITEM_RECT_ARC);
            }

            g2.setColor(FG_COLOR);
            if (item.getSprite() != null) {
                g2.drawImage(item.getSprite(), ITEM_IMAGE_X, y - ITEM_IMAGE_H + 4,
                        ITEM_IMAGE_W, ITEM_IMAGE_H, null);
            }

            g2.setFont(ITEM_FONT);
            g2.drawString(item.getDisplayName() + " - " + item.getPrice() + "G", ITEM_TEXT_X, y);

            y += LIST_SPACING;
        }

        // Descrizione item selezionato
        if (selectedIndex >= 0) {
            final ShopItem selectedItem = items.get(selectedIndex);
            g2.setColor(DESC_COLOR);
            g2.setFont(DESC_FONT);
            g2.drawString(selectedItem.getDescription(), PADDING, getHeight() - DESC_Y_OFFSET);
        }
    }

    /**
     * Reinizializza i campi transient dopo una (eventuale) deserializzazione.
     * Qui non abbiamo a disposizione CombatManager/Wallet originali; ripristiniamo uno stato safe.
     *
     * @param in stream di input usato per leggere l'oggetto serializzato
     * @throws IOException            se si verifica un errore di I/O
     * @throws ClassNotFoundException se una classe richiesta non viene trovata
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Stato runtime “safe” dopo deserializzazione (niente item reali, monete 0)
        this.items = new ArrayList<>();
        this.coinsSupplier = () -> 0;

        if (this.goldLabel == null) {
            this.goldLabel = new JLabel("Oro: 0");
            goldLabel.setFont(GOLD_FONT);
            goldLabel.setForeground(Color.YELLOW);
            final Dimension size = goldLabel.getPreferredSize();
            goldLabel.setBounds(PANEL_W - size.width - GOLD_LABEL_RIGHT_OFFSET,
                    GOLD_LABEL_BOTTOM_Y, size.width, size.height);
            add(goldLabel);
        }
    }

}
