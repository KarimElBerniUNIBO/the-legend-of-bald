package com.thelegendofbald.view.main;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.thelegendofbald.api.panels.Panels;
import com.thelegendofbald.api.views.MainView;
import com.thelegendofbald.api.views.View;
import com.thelegendofbald.view.game.GamePanel;

public final class GameWindow extends JFrame implements View, MainView {

    private static final String TITLE = "The Legend of Bald";
    private static Dimension internalSize = new Dimension(1280, 704);

    private Panels currentPanel = Panels.MAIN_MENU;
    private Optional<Panels> lastPanel = Optional.empty();

    public GameWindow() {
        super();
        this.updatePanelsSize();
    }

    private void updatePanelsSize() {
        Arrays.stream(Panels.values())
                .map(Panels::getPanel)
                .forEach(panel -> panel.setPreferredSize(internalSize));
    }

    @Override
    public void display() {
        this.setTitle(TITLE);
        this.setIconImage(new ImageIcon(this.getClass().getResource("/images/icon.png")).getImage());
        this.setResizable(true);
        this.setContentPane(currentPanel.getPanel());
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void changeMainPanel(Panels panelEnum) {
        lastPanel = Optional.of(currentPanel);
        currentPanel = panelEnum;
        this.updateView();
    }

    @Override
    public void updateView() {
        this.setContentPane(currentPanel.getPanel());
        this.revalidate();
        this.repaint();
        //this.pack();
        currentPanel.getPanel().requestFocusInWindow();
        if (currentPanel.getPanel() instanceof GamePanel gamePanel && !gamePanel.isRunning()) {
            gamePanel.startGame(); // <-- solo se è GamePanel
        }
    }

    public Dimension getInternalSize() {
        return internalSize;
    }

    public void setInternalSize(Dimension size) {
        GameWindow.internalSize = size;
    }

    public Optional<Panels> getLastPanel() {
        return lastPanel;
    }

}
