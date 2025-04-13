package com.thelegendofbald.ui.mainmenu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.api.Panels;
import com.thelegendofbald.ui.api.View;
import com.thelegendofbald.ui.view.GameWindow;

public class SwitchToOtherPanel implements ActionListener {

    private final View window;
    private final Panels panelEnum;

    public SwitchToOtherPanel(GameWindow window, Panels panelEnum) {
        this.window = window;
        this.panelEnum = panelEnum;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> {
            this.window.changeMainPanel(this.panelEnum);
        });
    }

}
