package com.thelegendofbald.ui.settingsmenu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.settingsmenu.api.Settings;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

public class SwitchToOtherSettingsEditorPanel implements ActionListener {

    private final SettingsEditorsManager sem;
    private final Settings setting;

    public SwitchToOtherSettingsEditorPanel(SettingsEditorsManager sem, Settings setting) {
        this.sem = sem;
        this.setting = setting;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> sem.changeSettingsEditorPanel(setting.getSettingsEditor()));
    }

}
