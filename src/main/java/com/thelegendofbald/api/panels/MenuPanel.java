package com.thelegendofbald.api.panels;

import java.awt.BorderLayout;

import javax.swing.SwingUtilities;

/**
 * Abstract class representing a menu panel in the application.
 * This class extends {@link AdapterPanel} and initializes the layout to BorderLayout.
 */
public abstract class MenuPanel extends AdapterPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor that initializes the menu panel.
     * It sets the layout to BorderLayout using {@link SwingUtilities#invokeLater}.
     */
    public MenuPanel() {
        super();
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> this.setLayout(new BorderLayout()));
    }

}
