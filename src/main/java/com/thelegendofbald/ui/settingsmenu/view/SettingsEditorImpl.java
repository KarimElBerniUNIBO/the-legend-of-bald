package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.controller.ResizeListener;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.Settings;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditor;
import com.thelegendofbald.ui.settingsmenu.model.ConfigPanel;

public final class SettingsEditorImpl extends JPanel implements SettingsEditor {

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private boolean initialized = false;
    private final Settings settings;
    private final List<ConfigPanel> configsPanels;

    public SettingsEditorImpl(Dimension size, Settings settings) {
        this.settings = settings;
        this.configsPanels = this.getConfigsPanels();
        this.setMaximumSize(size);
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
        this.addComponentListener(new ResizeListener(this::onResize));
    }

    @Override
    public List<ConfigPanel> getConfigsPanels() {
        return this.settings.getConfigs().stream()
                .map(config -> new ConfigPanel(config.getText(), config.getJcomponent())).toList();
    }

    @Override
    public void onResize() {
        if (!this.initialized && this.getWidth() > 0 && this.getHeight() > 0) {
            this.addComponentsToPanel();
            this.initialized = true;
        }
    }

    @Override
    public void addComponentsToPanel() {
        this.configsPanels.stream()
                .forEach(cp -> {
                    gbc.gridy = configsPanels.indexOf(cp);
                    this.add(cp, gbc);
                });

        this.revalidate();
        this.repaint();
    }

    @Override
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(size);
        this.setMaximumSize(size);
        //this.configsPanels.forEach(cp -> cp.setPreferredSize(size));
    }

}
