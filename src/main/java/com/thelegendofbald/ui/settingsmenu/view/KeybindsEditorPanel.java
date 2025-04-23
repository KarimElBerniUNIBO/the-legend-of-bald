package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import com.thelegendofbald.ui.settingsmenu.api.SettingsEditor;
import com.thelegendofbald.ui.settingsmenu.model.ConfigPanel;

class KeybindsEditorPanel extends JPanel implements SettingsEditor {

    private final Random random = new Random();

    public KeybindsEditorPanel(Dimension size) {
        this.setMaximumSize(size);
        this.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
    }

    @Override
    public void onResize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onResize'");
    }

    @Override
    public List<ConfigPanel> getConfigsPanels() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConfigsPanels'");
    }

    @Override
    public void addComponentsToPanel() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addComponentsToPanel'");
    }

}
