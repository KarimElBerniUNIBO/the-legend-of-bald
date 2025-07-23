package com.thelegendofbald.view.inventory;

import java.awt.Color;
import java.awt.Dimension;
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

public class InventoryPanel extends AdapterPanel {

    private static final Color DEFAULT_BG_COLOR = new Color(0, 0, 0, 180);
    private static final Pair<Double, Double> TITLE_PROPORTION = Pair.of(1.0, 0.3);

    private static final double INVENTORY_CONTENT_WIDTH_INSETS = 0.05;
    private static final double INVENTORY_CONTENT_HEIGHT_INSETS = 0.05;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();
    private final GridBagConstraints inventoryContentGBC = gbcFactory.createBothGridBagConstraints();

    private final TextLabelFactory tlFactory = new TextLabelFactoryImpl();

    private final String titleText;
    private final int maxSlotsPerRow;
    private final int maxRows;
    private final Inventory inventoryManager;

    private Optional<TextLabel> title = Optional.empty();
    private Optional<JPanel> inventoryContent = Optional.empty();

    public InventoryPanel(String title, Dimension size, int columns, int rows) {
        super(size);
        this.titleText = title;
        this.maxSlotsPerRow = columns;
        this.maxRows = rows;
        this.inventoryManager = new InventoryManager(rows, columns);

        this.setLayout(new GridBagLayout());
        this.setVisible(true);
        SwingUtilities.invokeLater(() -> this.setBackground(DEFAULT_BG_COLOR));
    }

    @Override
    protected void initializeComponents() {
        this.title = Optional.of(tlFactory.createTextLabelWithProportion(titleText, this.getSize(),
                Optional.of(TITLE_PROPORTION), Optional.empty(), Optional.empty(), Optional.empty()));
        this.inventoryContent = Optional.of(new InventoryContent(this.getSize(), 
                this.maxSlotsPerRow, this.maxRows, this.inventoryManager));

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

    public Inventory getInventory() {
        return inventoryManager;
    }

}
