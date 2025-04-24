package com.thelegendofbald.ui.settingsmenu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.settingsmenu.api.Buttons;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

public class SwitchToOtherSettingsEditorPanel implements ActionListener {

    private final SettingsEditorsManager sem;
    private final Buttons button;

    public SwitchToOtherSettingsEditorPanel(SettingsEditorsManager sem, Buttons button) {
        this.sem = sem;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            button.getLinkedButton();
            sem.changeSettingsEditorPanel(button.getSettingsEditor());
        });
    }

}
