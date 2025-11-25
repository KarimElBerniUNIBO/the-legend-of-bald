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
 * Panel that displays the shop interface.
 * <p>
 * Allows the player to view a list of purchasable items,
 * select them to see the description, and purchase them if they have enough
 * gold.
 * Manages the graphical update of the wallet and interaction with the
 * inventory.
 * </p>
 */
public final class ShopPanel extends JPanel {

    /** UID for serialization. */
    private static final long serialVersionUID = 1L;

    /** Preferred panel width in pixels. */
    private static final int PANEL_W = 400;
    /** Preferred panel height in pixels. */
    private static final int PANEL_H = 300;

    /** Generic padding for element positioning. */
    private static final int PADDING = 20;
    /** Y position of the shop title. */
    private static final int TITLE_Y = 40;
    /** Starting Y position for the item list. */
    private static final int LIST_START_Y = 80;
    /** Vertical spacing between items in the list. */
    private static final int LIST_SPACING = 50;

    /** X coordinate of the item selection rectangle. */
    private static final int ITEM_RECT_X = 20;
    /** Height of the item selection rectangle. */
    private static final int ITEM_RECT_H = 35;
    /** Corner radius of the selection rectangle. */
    private static final int ITEM_RECT_ARC = 10;
    /** Vertical offset to center the rectangle relative to the text. */
    private static final int ITEM_RECT_OFFSET_Y = 25;
    /** X coordinate for the item icon. */
    private static final int ITEM_IMAGE_X = 30;
    /** Width of the item icon. */
    private static final int ITEM_IMAGE_W = 24;
    /** Height of the item icon. */
    private static final int ITEM_IMAGE_H = 24;
    /** X coordinate for the item text (name and price). */
    private static final int ITEM_TEXT_X = 60;

    /** Vertical offset from the bottom for the selected item description. */
    private static final int DESC_Y_OFFSET = 30;

    /** Font size for the gold label. */
    private static final int GOLD_LABEL_FONT_SIZE = 14;
    /** Font size for the panel title. */
    private static final int TITLE_FONT_SIZE = 22;
    /** Font size for item names. */
    private static final int ITEM_FONT_SIZE = 16;
    /** Font size for item descriptions. */
    private static final int DESC_FONT_SIZE = 14;

    /** Y position from the bottom for the gold label. */
    private static final int GOLD_LABEL_BOTTOM_Y = 270;
    /** Right offset for the gold label. */
    private static final int GOLD_LABEL_RIGHT_OFFSET = 20;

    /** Width of buy buttons. */
    private static final int BUY_BUTTON_WIDTH = 90;
    /** Height of buy buttons. */
    private static final int BUY_BUTTON_HEIGHT = 28;

    /** Background color of the panel. */
    private static final Color BG_COLOR = Color.DARK_GRAY;
    /** Main text color. */
    private static final Color FG_COLOR = Color.WHITE;
    /** Highlight color for the selected item. */
    private static final Color SELECT_COLOR = new Color(70, 130, 180);
    /** Description text color. */
    private static final Color DESC_COLOR = Color.LIGHT_GRAY;

    /** Font used for the "Shop" title. */
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, TITLE_FONT_SIZE);
    /** Font used for item names in the list. */
    private static final Font ITEM_FONT = new Font("Arial", Font.PLAIN, ITEM_FONT_SIZE);
    /** Font used for the description at the bottom. */
    private static final Font DESC_FONT = new Font("Arial", Font.ITALIC, DESC_FONT_SIZE);
    /** Monospaced font used to display gold. */
    private static final Font GOLD_FONT = new Font("Monospaced", Font.BOLD, GOLD_LABEL_FONT_SIZE);

    /** List of items available for purchase in the shop. */
    private transient List<ShopItem> items;
    /** List of buy buttons associated with items. */
    private transient List<JButton> buyButtons;
    /** Functional supplier to retrieve current coin balance. */
    private transient IntSupplier coinsSupplier;
    /** Reference to the player's wallet. */
    private transient Wallet wallet;
    /** Reference to the player's inventory where purchased items are added. */
    private final transient Inventory inventory;
    /** Index of the currently selected item (-1 if none). */
    private int selectedIndex = -1;
    /** Swing label to display current balance. */
    private JLabel goldLabel;

    /**
     * Creates a new shop panel.
     *
     * @param combatManager the combat manager (needed to initialize weapons)
     * @param wallet        the player's wallet
     * @param inventory     the player's inventory
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Must modify the inventory when items are purchased.")
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
     * Initializes runtime state, populating the item list and configuring the
     * wallet.
     *
     * @param combatManager the combat manager
     * @param wallet        the player's wallet
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
     * Creates and positions "Buy Item" buttons for each item in the list.
     * Assigns listeners to handle purchase clicks.
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
     * Updates the gold label text with the current wallet value.
     */
    public void updateGoldDisplay() {
        goldLabel.setText("Gold: " + coinsSupplier.getAsInt());
        goldLabel.repaint();
    }

    /**
     * Attempts to purchase a specific item.
     * Checks if the player has enough gold and if wallet and inventory references
     * are valid.
     * On success, deducts gold and adds the item to the inventory.
     *
     * @param item the item to purchase
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
     * Performs custom rendering of the panel.
     * Draws the title, item list (with icons and prices), selection highlight,
     * and the description of the selected item.
     *
     * @param g the graphics context to draw on
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
     * Reconstructs transient fields after object deserialization.
     * Reinitializes lists and recreates the gold label if necessary.
     *
     * @param in the input stream to read the object from
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if the class of a serialized object is not
     *                                found
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
