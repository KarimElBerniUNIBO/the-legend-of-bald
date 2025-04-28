package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.swing.JPanel;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.controller.ResizeListener;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.KeybindsSettings;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditor;
import com.thelegendofbald.ui.settingsmenu.model.ConfigPanel;

class KeybindsEditorPanel extends JPanel implements SettingsEditor {

    private static final double KEYBIND_BUTTONS_ARCPROPORTION = 0.05;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private List<ConfigPanel> configsPanels;

    private boolean initialized = false;

    public KeybindsEditorPanel(Dimension size) {
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
        return Arrays.stream(KeybindsSettings.values())
                .map(ks -> new ConfigPanel(ks.getText(), jbFactory.createKeybindingButton(
                        ks.getButtonText(),
                        this.getSize(),
                        KEYBIND_BUTTONS_ARCPROPORTION,
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty())))
                .toList();
    }

    @Override
    public void addComponentsToPanel() {
        this.configsPanels = this.getConfigsPanels();
        this.configsPanels.stream().forEach(cp -> {
            gbc.gridy = this.configsPanels.indexOf(cp);
            this.add(cp, gbc);
        });

        this.revalidate();
        this.repaint();
    }

}
