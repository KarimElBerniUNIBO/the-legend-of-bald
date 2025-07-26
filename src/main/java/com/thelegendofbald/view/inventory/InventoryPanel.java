package com.thelegendofbald.view.inventory;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.common.TextLabelFactory;
import com.thelegendofbald.api.inventory.Inventory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.model.inventory.InventoryManager;
import com.thelegendofbald.view.common.TextLabel;
import com.thelegendofbald.view.common.TextLabelFactoryImpl;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The InventoryPanel class represents a panel that displays the inventory of the game.
 * It includes a title and a grid layout for the inventory items.
 */
public final class InventoryPanel extends AdapterPanel {

    private static final long serialVersionUID = 1L;

    private static final Color DEFAULT_BG_COLOR = new Color(0, 0, 0, 180);
    private static final Pair<Double, Double> TITLE_PROPORTION = Pair.of(1.0, 0.3);

    private static final double INVENTORY_CONTENT_WIDTH_INSETS = 0.05;
    private static final double INVENTORY_CONTENT_HEIGHT_INSETS = 0.05;

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();
    private final GridBagConstraints inventoryContentGBC = gbcFactory.createBothGridBagConstraints();

    private final transient TextLabelFactory tlFactory = new TextLabelFactoryImpl();

    private final String titleText;
    private final int columns;
    private final int rows;
    private final transient Inventory inventoryManager;

    private transient Optional<TextLabel> title = Optional.empty();
    private transient Optional<JPanel> inventoryContent = Optional.empty();

    /**
     * Constructs an InventoryPanel with the specified title, number of columns, and rows.
     *
     * @param title   the title of the inventory panel
     * @param columns the number of columns in the inventory grid
     * @param rows    the number of rows in the inventory grid
     */
    public InventoryPanel(final String title, final int columns, final int rows) {
        super();
        this.titleText = title;
        this.columns = columns;
        this.rows = rows;
        this.inventoryManager = new InventoryManager(this.rows, this.columns);

        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setLayout(new GridBagLayout());
            this.setVisible(false);
            this.setBackground(DEFAULT_BG_COLOR);
        });
    }

    @Override
    protected void initializeComponents() {
        this.title = Optional.of(tlFactory.createTextLabelWithProportion(titleText, this.getSize(),
                Optional.of(TITLE_PROPORTION), Optional.empty(), Optional.empty(), Optional.empty()));
        this.inventoryContent = Optional.of(new InventoryContent(this.columns, 
                this.inventoryManager));

        super.initializeComponents();
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents()).forEach(component -> component.setPreferredSize(this.getSize()));
    }

    @Override
    public void addComponentsToPanel() {
        this.inventoryContentGBC.insets.set((int) (this.getHeight() * INVENTORY_CONTENT_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_CONTENT_WIDTH_INSETS),
                (int) (this.getHeight() * INVENTORY_CONTENT_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_CONTENT_WIDTH_INSETS));

        this.title.ifPresent(t -> {
            this.gbc.gridy = 0;
            this.gbc.weighty = TITLE_PROPORTION.getRight();
            this.add(t, this.gbc);
        });
        this.inventoryContent.ifPresent(ic -> {
            this.inventoryContentGBC.gridy = 1;
            this.inventoryContentGBC.weighty = 1.0 - TITLE_PROPORTION.getRight();
            this.add(ic, this.inventoryContentGBC);
        });

        this.updateComponentsSize();
    }

    /**
     * Returns the inventory manager associated with this panel.
     *
     * @return the inventory manager
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "This method is intended to be used by the InventoryManager to access the inventory."
    )
    public Inventory getInventory() {
        return inventoryManager;
    }

}
