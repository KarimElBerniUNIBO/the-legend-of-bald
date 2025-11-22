package com.thelegendofbald.view.panel.settings;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JPanel;

import com.thelegendofbald.view.panel.base.AdapterPanel;
import com.thelegendofbald.view.panel.base.MenuPanel;
import com.thelegendofbald.model.config.SettingsEditorsManager;
import com.thelegendofbald.view.component.BackToPreviousPanel;

/**
 * The {@code SettingsPanel} class represents the main panel for the settings
 * menu UI.
 * It extends {@link AdapterPanel} and organizes its subcomponents using a
 * {@link BorderLayout}.
 * <p>
 * This panel contains:
 * <ul>
 * <li>A {@link BackToPreviousPanel} component on the west side for navigation back
 * to the main menu.</li>
 * <li>A {@link NorthPanel} component at the north, which may contain settings
 * categories or navigation.</li>
 * <li>A {@link SettingsEditorsManager} (typically a
 * {@link SettingsEditorManagerPanel}) at the center for editing settings.</li>
 * </ul>
 * The panel is opaque and uses a black background.
 * </p>
 */
public final class SettingsPanel extends MenuPanel {

    private static final long serialVersionUID = 1L;

    private BackToPreviousPanel backToMainPanel;
    private JPanel northPanel;
    private SettingsEditorsManager sem;

    @Override
    protected void initializeComponents() {
        this.backToMainPanel = new BackToPreviousPanel();
        this.sem = new SettingsEditorManagerPanel();
        this.northPanel = new NorthPanel(this.sem);
        super.initializeComponents();
    }

    @Override
    public void addComponentsToPanel() {
        this.add(this.backToMainPanel, BorderLayout.WEST);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add((JPanel) this.sem, BorderLayout.CENTER);
        this.updateComponentsSize();
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents()).forEach(component -> component.setPreferredSize(this.getSize()));
    }

}
