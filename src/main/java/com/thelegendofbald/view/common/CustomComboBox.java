package com.thelegendofbald.view.common;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

/**
 * CustomComboBox is a specialized JComboBox that allows for custom rendering
 * and maintains the last selected item.
 * <p>
 * It initializes with a list of items and sets a custom renderer for better
 * visual representation.
 * The last selected item can be retrieved or set, allowing for state management
 * across different uses of the combo box.
 * </p>
 *
 * @param <X> the type of items in the combo box
 */
public final class CustomComboBox<X> extends JComboBox<X> {

    private static final long serialVersionUID = 1L;

    private X lastSelectedItem;

    /**
     * Constructs a CustomComboBox with the specified items.
     *
     * @param items the list of items to be added to the combo box
     */
    public CustomComboBox(final List<X> items) {
        super();
        items.forEach(this::addItem);
        this.lastSelectedItem = items.getFirst();
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setRenderer(new CustomComboBoxRenderer(this));
            this.setSelectedItem(lastSelectedItem);
        });
    }

    /**
     * Returns the last selected item in the combo box.
     *
     * @return the last selected item
     */
    public X getLastSelectedItem() {
        return lastSelectedItem;
    }

    /**
     * Sets the last selected item in the combo box.
     *
     * @param item the item to set as the last selected
     */
    public void setLastSelectedItem(final X item) {
        this.lastSelectedItem = item;
    }

}
