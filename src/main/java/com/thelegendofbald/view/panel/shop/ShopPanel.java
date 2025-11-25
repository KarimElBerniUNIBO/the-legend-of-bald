package com.thelegendofbald.view.panel.shop;

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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thelegendofbald.model.inventory.Inventory;
import com.thelegendofbald.model.system.CombatManager;
import com.thelegendofbald.model.system.Wallet;
import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.model.item.ShopItem;
import com.thelegendofbald.model.item.weapons.Axe;
import com.thelegendofbald.model.item.weapons.FireBall;
import com.thelegendofbald.model.item.weapons.Sword;
import com.thelegendofbald.utils.LoggerUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Pannello che visualizza l'interfaccia del negozio (Shop).
 * <p>
 * Permette al giocatore di visualizzare una lista di oggetti acquistabili,
 * selezionarli per vedere la descrizione e acquistarli se si dispone di abbastanza oro.
 * Gestisce l'aggiornamento grafico del portafoglio e l'interazione con l'inventario.
 * </p>
 */
public final class ShopPanel extends JPanel {

    /** UID per la serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Larghezza preferita del pannello in pixel. */
    private static final int PANEL_W = 400;
    /** Altezza preferita del pannello in pixel. */
    private static final int PANEL_H = 300;

    /** Padding generico per il posizionamento degli elementi. */
    private static final int PADDING = 20;
    /** Posizione Y del titolo del negozio. */
    private static final int TITLE_Y = 40;
    /** Posizione Y di partenza per la lista degli oggetti. */
    private static final int LIST_START_Y = 80;
    /** Spaziatura verticale tra un oggetto e l'altro nella lista. */
    private static final int LIST_SPACING = 50;

    /** Coordinata X del rettangolo di selezione dell'oggetto. */
    private static final int ITEM_RECT_X = 20;
    /** Altezza del rettangolo di selezione dell'oggetto. */
    private static final int ITEM_RECT_H = 35;
    /** Arrotondamento degli angoli del rettangolo di selezione. */
    private static final int ITEM_RECT_ARC = 10;
    /** Offset verticale per centrare il rettangolo rispetto al testo. */
    private static final int ITEM_RECT_OFFSET_Y = 25;
    /** Coordinata X per l'icona dell'oggetto. */
    private static final int ITEM_IMAGE_X = 30;
    /** Larghezza dell'icona dell'oggetto. */
    private static final int ITEM_IMAGE_W = 24;
    /** Altezza dell'icona dell'oggetto. */
    private static final int ITEM_IMAGE_H = 24;
    /** Coordinata X per il testo (nome e prezzo) dell'oggetto. */
    private static final int ITEM_TEXT_X = 60;

    /** Offset verticale dal fondo per la descrizione dell'oggetto selezionato. */
    private static final int DESC_Y_OFFSET = 30;

    /** Dimensione del font per l'etichetta dell'oro. */
    private static final int GOLD_LABEL_FONT_SIZE = 14;
    /** Dimensione del font per il titolo del pannello. */
    private static final int TITLE_FONT_SIZE = 22;
    /** Dimensione del font per il nome degli oggetti. */
    private static final int ITEM_FONT_SIZE  = 16;
    /** Dimensione del font per la descrizione degli oggetti. */
    private static final int DESC_FONT_SIZE  = 14;

    /** Posizione Y dal fondo per l'etichetta dell'oro. */
    private static final int GOLD_LABEL_BOTTOM_Y = 270;
    /** Offset destro per l'etichetta dell'oro. */
    private static final int GOLD_LABEL_RIGHT_OFFSET = 20;

    /** Larghezza dei pulsanti di acquisto. */
    private static final int BUY_BUTTON_WIDTH = 90;
    /** Altezza dei pulsanti di acquisto. */
    private static final int BUY_BUTTON_HEIGHT = 28;

    /** Colore di sfondo del pannello. */
    private static final Color BG_COLOR = Color.DARK_GRAY;
    /** Colore del testo principale. */
    private static final Color FG_COLOR = Color.WHITE;
    /** Colore di evidenziazione per l'oggetto selezionato. */
    private static final Color SELECT_COLOR = new Color(70, 130, 180);
    /** Colore del testo della descrizione. */
    private static final Color DESC_COLOR = Color.LIGHT_GRAY;

    /** Font utilizzato per il titolo "Shop". */
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, TITLE_FONT_SIZE);
    /** Font utilizzato per i nomi degli oggetti nella lista. */
    private static final Font ITEM_FONT  = new Font("Arial", Font.PLAIN, ITEM_FONT_SIZE);
    /** Font utilizzato per la descrizione in basso. */
    private static final Font DESC_FONT  = new Font("Arial", Font.ITALIC, DESC_FONT_SIZE);
    /** Font monospaziato utilizzato per visualizzare l'oro. */
    private static final Font GOLD_FONT  = new Font("Monospaced", Font.BOLD, GOLD_LABEL_FONT_SIZE);

    /** Lista degli oggetti disponibili per l'acquisto nel negozio. */
    private transient List<ShopItem> items;
    /** Lista dei pulsanti di acquisto associati agli oggetti. */
    private transient List<JButton> buyButtons;
    /** Fornitore funzionale per recuperare il saldo attuale delle monete. */
    private transient IntSupplier coinsSupplier;
    /** Riferimento al portafoglio del giocatore. */
    private transient Wallet wallet;
    /** Riferimento all'inventario del giocatore dove aggiungere gli oggetti acquistati. */
    private final transient Inventory inventory;
    /** Indice dell'oggetto attualmente selezionato (-1 se nessuno). */
    private int selectedIndex = -1;
    /** Etichetta Swing per visualizzare il saldo attuale. */
    private JLabel goldLabel;

    /**
     * Crea un nuovo pannello del negozio.
     *
     * @param combatManager il gestore del combattimento (necessario per inizializzare le armi)
     * @param wallet il portafoglio del giocatore
     * @param inventory l'inventario del giocatore
     */
    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "ShopPanel must modify the actual player inventory when items are purchased."
    )
    public ShopPanel(final CombatManager combatManager, final Wallet wallet, final Inventory inventory) {
        this.inventory = inventory;
        initRuntimeState(combatManager, wallet);

        setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        setBackground(BG_COLOR);
        setLayout(null);

        this.goldLabel = new JLabel("Gold: " + coinsSupplier.getAsInt());
        goldLabel.setFont(GOLD_FONT);
        goldLabel.setForeground(Color.YELLOW);
        final Dimension size = goldLabel.getPreferredSize();
        goldLabel.setBounds(PANEL_W - size.width - GOLD_LABEL_RIGHT_OFFSET,
                GOLD_LABEL_BOTTOM_Y, size.width, size.height);
        add(goldLabel);

        createBuyButtons();

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
                        break;
                    }
                    y += LIST_SPACING;
                }
            }
        });
    }

    /**
     * Inizializza lo stato a runtime, popolando la lista degli oggetti e configurando il wallet.
     *
     * @param combatManager il gestore del combattimento
     * @param wallet il portafoglio del giocatore
     */
    private void initRuntimeState(final CombatManager combatManager, final Wallet wallet) {
        this.items = new ArrayList<>();
        this.buyButtons = new ArrayList<>();
        this.wallet = wallet;
        this.coinsSupplier = wallet != null ? wallet::getCoins : () -> 0;

        items.add(new Sword(0, 0, 32, 32, combatManager));
        items.add(new Axe(0, 0, 32, 32, combatManager));
        items.add(new FireBall(0, 0, 32, 32, combatManager));
    }

    /**
     * Crea e posiziona i pulsanti "Buy Item" per ogni oggetto nella lista.
     * Assegna i listener per gestire il click di acquisto.
     */
    private void createBuyButtons() {
        int y = LIST_START_Y - ITEM_RECT_OFFSET_Y;
        for (final ShopItem shopItem : items) {
            final JButton buyButton = new JButton("Buy Item");
            buyButton.setBackground(Color.RED);
            buyButton.setOpaque(true);
            buyButton.setFocusable(false);

            buyButton.addActionListener(e -> attemptPurchase(shopItem));

            final int x = PANEL_W - BUY_BUTTON_WIDTH - ITEM_RECT_X;
            final int yButton = y + (ITEM_RECT_H - BUY_BUTTON_HEIGHT) / 2;

            buyButton.setBounds(x, yButton, BUY_BUTTON_WIDTH, BUY_BUTTON_HEIGHT);

            add(buyButton);
            buyButtons.add(buyButton);

            y += LIST_SPACING;
        }
    }

    /**
     * Aggiorna il testo dell'etichetta dell'oro con il valore attuale del portafoglio.
     */
    public void updateGoldDisplay() {
        goldLabel.setText("Gold: " + coinsSupplier.getAsInt());
        goldLabel.repaint();
    }

    /**
     * Tenta di acquistare un oggetto specifico.
     * Verifica se il giocatore ha abbastanza oro e se i riferimenti a wallet e inventario sono validi.
     * In caso di successo, deduce l'oro e aggiunge l'oggetto all'inventario.
     *
     * @param item l'oggetto che si intende acquistare
     */
    private void attemptPurchase(final ShopItem item) {
        if (wallet == null || inventory == null) {
            LoggerUtils.error("Cannot purchase: wallet or inventory is null.");
            return;
        }

        final int price = item.getPrice();
        final int currentCoins = wallet.getCoins();

        if (currentCoins < price) {
            LoggerUtils.info("Not enough gold to buy: " + item.getDisplayName());
            return;
        }

        wallet.removeCoins(price);
        final GameItem gameItem = (GameItem) item;
        inventory.add(gameItem);

        LoggerUtils.info("Purchased: " + item.getDisplayName() + " for " + price + " gold.");

        updateGoldDisplay();
    }

    /**
     * Esegue il rendering personalizzato del pannello.
     * Disegna il titolo, la lista degli oggetti (con icone e prezzi), l'evidenziazione della selezione
     * e la descrizione dell'oggetto selezionato.
     *
     * @param g il contesto grafico su cui disegnare
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(FG_COLOR);
        g2.setFont(TITLE_FONT);
        g2.drawString("Shop", PADDING, TITLE_Y);

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

        if (selectedIndex >= 0) {
            final ShopItem selectedItem = items.get(selectedIndex);
            g2.setColor(DESC_COLOR);
            g2.setFont(DESC_FONT);
            g2.drawString(selectedItem.getDescription(), PADDING, getHeight() - DESC_Y_OFFSET);
        }
    }

    /**
     * Ricostruisce i campi transienti dopo la deserializzazione dell'oggetto.
     * Reiniziata le liste e ricrea l'etichetta dell'oro se necessario.
     *
     * @param in lo stream di input da cui leggere l'oggetto
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe di un oggetto serializzato non viene trovata
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.items = new ArrayList<>();
        this.buyButtons = new ArrayList<>();
        this.coinsSupplier = () -> 0;
        this.wallet = null;

        if (this.goldLabel == null) {
            this.goldLabel = new JLabel("Gold: 0");
            goldLabel.setFont(GOLD_FONT);
            goldLabel.setForeground(Color.RED);
            final Dimension size = goldLabel.getPreferredSize();
            goldLabel.setBounds(PANEL_W - size.width - GOLD_LABEL_RIGHT_OFFSET,
                    GOLD_LABEL_BOTTOM_Y, size.width, size.height);
            add(goldLabel);
        }
    }
}
