package com.thelegendofbald.view.common;

import java.util.List;
import javax.swing.JComboBox;

public class CustomComboBox<X> extends JComboBox<X> {

    private X lastSelectedItem;

    public CustomComboBox(List<X> items) {
        super();
        items.forEach(this::addItem);
        this.setRenderer(new CustomComboBoxRenderer(this));
        this.setSelectedItem(items.getFirst());
        this.lastSelectedItem = items.getFirst();
    }

    public X getLastSelectedItem() {
        return lastSelectedItem;
    }

    public void setLastSelectedItem(X lastSelectedItem) {
        this.lastSelectedItem = lastSelectedItem;
    }

}
