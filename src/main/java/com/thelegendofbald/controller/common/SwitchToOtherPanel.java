package com.thelegendofbald.controller.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import com.thelegendofbald.api.panels.Panels;
import com.thelegendofbald.api.views.MainView;
import com.thelegendofbald.view.main.GameWindow;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * An {@link ActionListener} implementation that switches the main panel of the application window
 * to a specified panel when an action is performed.
 * <p>
 * This class is typically used to handle UI events (such as button clicks) that require changing
 * the currently displayed panel in the main application window.
 * </p>
 *
 * @see java.awt.event.ActionListener
 */
public final class SwitchToOtherPanel implements ActionListener {

    private final MainView window;
    private final Panels panelEnum;

    /**
     * Constructs a new {@code SwitchToOtherPanel} action that allows switching the displayed panel
     * in the specified {@link GameWindow} to the panel represented by the given {@link Panels} enum.
     *
     * @param window    the main game window where the panel switch will occur
     * @param panelEnum the enum value representing the panel to switch to
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "This constructor is intended to be used for initializing the action listener with a specific panel enum."
    )
    public SwitchToOtherPanel(final MainView window, final Panels panelEnum) {
        this.window = window;
        this.panelEnum = panelEnum;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            this.window.changeMainPanel(this.panelEnum);
        });
    }

}
