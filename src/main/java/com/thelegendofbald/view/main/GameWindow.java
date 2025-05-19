package com.thelegendofbald.view.main;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.api.panels.Panels;
import com.thelegendofbald.api.views.MainView;
import com.thelegendofbald.api.views.View;
import com.thelegendofbald.view.game.GamePanel;

public final class GameWindow extends JFrame implements View, MainView {

    private static final String TITLE = "The Legend of Bald";
    private static Dimension internalSize = new Dimension(1280, 704);

    private final List<MenuPanel> panels;

    public GameWindow() {
        super();
        this.panels = this.getMenuPanels();
        this.panels.forEach(panel -> panel.setPreferredSize(internalSize));

    }

    private List<MenuPanel> getMenuPanels() {
        return Arrays.stream(Panels.values())
                .map(Panels::getPanel)
                .toList();
    }

    @Override
    public void display() {
        this.setTitle(TITLE);
        this.setIconImage(new ImageIcon(this.getClass().getResource("/images/icon.png")).getImage());
        this.setResizable(true);
        this.setContentPane(this.panels.getFirst());
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void changeMainPanel(Panels panelEnum) {
        JPanel panel = this.panels.get(panelEnum.getIndex());
        this.setContentPane(panel);
        //this.pack();
        this.revalidate();
        this.repaint();
        panel.requestFocusInWindow();

        if (panel instanceof GamePanel gamePanel && !gamePanel.isRunning()) {
            gamePanel.startGame(); // <-- solo se è GamePanel
        }
    }

    public Dimension getInternalSize() {
        return internalSize;
    }

    public void setInternalSize(Dimension size) {
        GameWindow.internalSize = size;
    }

    @Override
    public void updateView() {
        SwingUtilities.invokeLater(() -> {
            this.revalidate();
            this.repaint();
        });
    }

}
