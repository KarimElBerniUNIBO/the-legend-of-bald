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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.common.Wallet;
import com.thelegendofbald.model.item.GameItem;
import com.thelegendofbald.model.item.ShopItem;
import com.thelegendofbald.model.item.weapons.Axe;
import com.thelegendofbald.model.item.weapons.FireBall;
import com.thelegendofbald.model.item.weapons.Sword;
import com.thelegendofbald.utils.LoggerUtils;

/**
 * Panel that displays a shop interface where players can buy items.
 */
public final class ShopPanel extends JPanel {

    private static final long serialVersionUID = 1L;

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

    private static final int GOLD_LABEL_BOTTOM_Y = 270;
    private static final int GOLD_LABEL_RIGHT_OFFSET = 20;

    private static final int BUY_BUTTON_WIDTH = 90;
    private static final int BUY_BUTTON_HEIGHT = 28;

    private static final Color BG_COLOR = Color.DARK_GRAY;
    private static final Color FG_COLOR = Color.WHITE;
    private static final Color SELECT_COLOR = new Color(70, 130, 180);
    private static final Color DESC_COLOR = Color.LIGHT_GRAY;

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, TITLE_FONT_SIZE);
    private static final Font ITEM_FONT  = new Font("Arial", Font.PLAIN, ITEM_FONT_SIZE);
    private static final Font DESC_FONT  = new Font("Arial", Font.ITALIC, DESC_FONT_SIZE);
    private static final Font GOLD_FONT  = new Font("Monospaced", Font.BOLD, GOLD_LABEL_FONT_SIZE);

    private transient List<ShopItem> items;
    private transient List<JButton> buyButtons;
    private transient IntSupplier coinsSupplier;
    private transient Wallet wallet;
    private final Inventory inventory;
    private int selectedIndex = -1;
    private JLabel goldLabel;

    /**
     * Creates a new shop panel.
     *
     * @param combatManager the combat manager
     * @param wallet the player's wallet
     * @param inventory the player's inventory
     */
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
     * Initializes the purchasable items and runtime state.
     *
     * @param combatManager the combat manager
     * @param wallet the player's wallet
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
     * Creates the "Buy" buttons for each item.
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
     * Updates the gold display label.
     */
    public void updateGoldDisplay() {
        goldLabel.setText("Gold: " + coinsSupplier.getAsInt());
        goldLabel.repaint();
    }

    /**
     * Attempts to purchase an item.
     *
     * @param item the purchasable item
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
     * Draws the panel's components.
     *
     * @param g the graphics context
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
     * Reconstructs transient fields after deserialization.
     *
     * @param in the input stream
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
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
