package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.controller.ResizeListener;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditor;
import com.thelegendofbald.ui.settingsmenu.api.VideoSettings;
import com.thelegendofbald.ui.settingsmenu.model.ConfigPanel;

class VideoEditorPanel extends JPanel implements SettingsEditor {

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final List<ConfigPanel> videoConfigs = this.getConfigsPanels();

    private boolean initialized = false;
    private final Random random = new Random();

    VideoEditorPanel(Dimension size) {
        this.setMaximumSize(size);
        //this.setOpaque(false);
        this.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
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
        return Arrays.stream(VideoSettings.values())
            .map(vs -> new ConfigPanel(vs.getText(), vs.getJcomponent()))
            .toList();
    }

    @Override
    public void addComponentsToPanel() {
        this.videoConfigs.stream()
                    .forEach(cp -> {
                        gbc.gridy = videoConfigs.indexOf(cp);
                        this.add(cp, gbc);
                    });

        this.revalidate();
        this.repaint();
    }

}
