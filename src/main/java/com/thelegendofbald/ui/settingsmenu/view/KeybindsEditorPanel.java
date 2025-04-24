package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.swing.JPanel;

import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.controller.ResizeListener;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditor;
import com.thelegendofbald.ui.settingsmenu.model.ConfigPanel;

class KeybindsEditorPanel extends JPanel implements SettingsEditor {
    
    private final JButtonFactory jbFactory = new JButtonFactoryImpl();

    private boolean initialized = false;
    private final Random random = new Random();

    public KeybindsEditorPanel(Dimension size) {
        this.setMaximumSize(size);
        this.setOpaque(false);
        this.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConfigsPanels'");
    }

    @Override
    public void addComponentsToPanel() {
        this.add(new ConfigPanel(
            "UP",
            jbFactory.createRoundedButton("^", this.getSize(), 0.1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())
        ));
        this.add(new ConfigPanel(
            "DOWN",
            jbFactory.createRoundedButton("v", this.getSize(), 0.1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())
        ));
        this.add(new ConfigPanel(
            "LEFT",
            jbFactory.createRoundedButton("<", this.getSize(), 0.1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())
        ));
        this.add(new ConfigPanel(
            "RIGHT",
            jbFactory.createRoundedButton(">", this.getSize(), 0.1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())
        ));

        this.revalidate();
        this.repaint();
    }

}
