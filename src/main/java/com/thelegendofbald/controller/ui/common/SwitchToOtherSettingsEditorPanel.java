package com.thelegendofbald.controller.ui.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import com.thelegendofbald.api.settingsmenu.Settings;
import com.thelegendofbald.api.settingsmenu.SettingsEditorsManager;

/**
 * An {@link ActionListener} implementation that switches the current settings editor panel
 * in the {@link SettingsEditorsManager} to the editor associated with a specific {@link Settings} instance.
 * <p>
 * When an action is performed, this listener schedules the panel switch on the Event Dispatch Thread (EDT)
 * using {@link javax.swing.SwingUtilities#invokeLater(Runnable)} to ensure thread safety with Swing components.
 * </p>
 *
 * @see SettingsEditorsManager
 * @see Settings
 */
public class SwitchToOtherSettingsEditorPanel implements ActionListener {

    private final SettingsEditorsManager sem;
    private final Settings setting;

    /**
     * Constructs a new {@code SwitchToOtherSettingsEditorPanel} with the specified
     * {@link SettingsEditorsManager} and {@link Settings} objects.
     *
     * @param sem     the manager responsible for handling settings editors
     * @param setting the specific setting to be edited or managed by this panel
     */
    public SwitchToOtherSettingsEditorPanel(final SettingsEditorsManager sem, final Settings setting) {
        this.sem = sem;
        this.setting = setting;
    }

    /**
     * Handles the action event to switch the current settings editor panel.
     * <p>
     * Subclasses can override this method to provide custom behavior when an action
     * is performed. If overridden, ensure that {@code super.actionPerformed(ActionEvent)}
     * is called to preserve the default behavior of switching the settings editor panel
     * on the Event Dispatch Thread (EDT).
     * </p>
     *
     * @param e the {@link ActionEvent} triggered by the user interaction
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(() -> sem.changeSettingsEditorPanel(setting.getSettingsEditor()));
    }

}
