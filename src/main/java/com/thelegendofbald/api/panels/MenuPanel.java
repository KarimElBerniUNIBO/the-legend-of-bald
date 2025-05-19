package com.thelegendofbald.api.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;

public abstract class MenuPanel extends AdapterPanel {

    public MenuPanel() {
        super(new Dimension(0, 0));
        this.setLayout(new BorderLayout());
    }

}
