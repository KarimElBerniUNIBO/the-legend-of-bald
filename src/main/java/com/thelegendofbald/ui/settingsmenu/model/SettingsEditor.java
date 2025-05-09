package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.Settings;

public final class SettingsEditor extends AdapterPanel {

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final Settings settings;
    private final List<ConfigPanel> configsPanels;

    public SettingsEditor(Dimension size, Settings settings) {
        super(size);
        this.settings = settings;
        this.configsPanels = this.getConfigsPanels();
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void initializeComponents() {
        
    }

    private List<ConfigPanel> getConfigsPanels() {
        return this.settings.getConfigs().stream()
                .map(config -> new ConfigPanel(config.getText(), config.getJcomponent())).toList();
    }

    /*private void updateSize(Dimension size) {
        this.configsPanels.forEach(cp -> cp.setPreferredSize(size));
    }*/

    @Override
    public void addComponentsToPanel() {
        this.configsPanels.stream()
                .forEach(cp -> {
                    gbc.gridy = configsPanels.indexOf(cp);
                    this.add(cp, gbc);
                });
    }

    @Override
    public void setPreferredSize(Dimension size) {
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
