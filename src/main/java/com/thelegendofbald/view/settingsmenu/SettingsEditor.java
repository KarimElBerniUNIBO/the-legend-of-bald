package com.thelegendofbald.view.settingsmenu;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.SwingUtilities;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.api.settingsmenu.Settings;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

/**
 * The {@code SettingsEditor} class is a custom panel for editing application settings.
 * It extends {@link AdapterPanel} and displays a list of configuration panels,
 * each representing a configurable setting.
 * <p>
 * The panel uses a {@link GridBagLayout} to arrange its child {@link ConfigPanel} components,
 * which are dynamically created based on the provided {@link Settings} object.
 * </p>
 *
 * @see AdapterPanel
 * @see Settings
 * @see ConfigPanel
 */
public final class SettingsEditor extends AdapterPanel {

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final Settings settings;
    private final List<ConfigPanel> configsPanels;

    /**
     * Constructs a new {@code SettingsEditor} with the specified size and settings.
     * <p>
     * Initializes the editor panel with the given dimensions and applies the provided
     * {@link Settings} instance. The panel is set to be non-opaque and uses a
     * {@link GridBagLayout} for component arrangement. Configuration panels are
     * initialized via {@code getConfigsPanels()}.
     *
     * @param size      the preferred size of the editor panel
     * @param settings  the settings object to be edited
     */
    public SettingsEditor(final Dimension size, final Settings settings) {
        super(size);
        this.settings = settings;
        this.configsPanels = this.getConfigsPanels();
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
    }

    private List<ConfigPanel> getConfigsPanels() {
        return this.settings.getConfigs().stream()
                .map(config -> new ConfigPanel(config.getText(), config.getJcomponent())).toList();
    }

    @Override
    public void addComponentsToPanel() {
        this.configsPanels.stream()
                .forEach(cp -> {
                    gbc.gridy = configsPanels.indexOf(cp);
                    this.add(cp, gbc);
                });
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(() -> {
            this.setMaximumSize(size);
            this.updateComponentsSize();
            this.addComponentsToPanel();
        });
    }

    @Override
    public void updateComponentsSize() {
        this.configsPanels.forEach(cp -> cp.setPreferredSize(this.getSize()));
    }

}
