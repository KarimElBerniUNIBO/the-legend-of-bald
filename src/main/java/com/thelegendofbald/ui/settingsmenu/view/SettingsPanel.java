package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JPanel;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.mainmenu.model.BackToMainPanel;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

public class SettingsPanel extends AdapterPanel {

    private BackToMainPanel backToMainPanel;
    private JPanel northPanel;
    private SettingsEditorsManager sem;

    public SettingsPanel(Dimension size) {
        super(size);
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());
    }

    @Override
    protected void initializeComponents() {
        this.backToMainPanel = new BackToMainPanel(this.getSize());
        this.sem = new SettingsEditorPanel(this.getSize());
        this.northPanel = new NorthPanel(this.getSize(), this.sem);
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
