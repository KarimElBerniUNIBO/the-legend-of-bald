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
import com.thelegendofbald.ui.settingsmenu.api.AudioSettings;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditor;
import com.thelegendofbald.ui.settingsmenu.model.ConfigPanel;

class AudioEditorPanel extends JPanel implements SettingsEditor {

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final List<ConfigPanel> audioConfigs = this.getConfigsPanels();
    private boolean initialized = false;

    public AudioEditorPanel(Dimension size) {
        this.setMaximumSize(size);
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
        this.addComponentListener(new ResizeListener(this::onResize));
    }

    @Override
    public void onResize() {
        if (!this.initialized && this.getWidth() > 0 && this.getHeight() > 0) {
            this.addComponentsToPanel();
            this.initialized = true;
        }
    }

    @Override
    public List<ConfigPanel> getConfigsPanels() {
        return Arrays.stream(AudioSettings.values())
                .map(as -> new ConfigPanel(as.getText(), as.getJcomponent()))
                .toList();
    }

    @Override
    public void addComponentsToPanel() {
        this.audioConfigs.stream()
            .forEach(ac -> {
                gbc.gridy = audioConfigs.indexOf(ac);
                this.add(ac, gbc);
            });

        this.revalidate();
        this.repaint();
    }

}
