package com.thelegendofbald.view.main;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.api.panels.Panels;
import com.thelegendofbald.api.settingsmenu.WindowMode;
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
        this.updateView();
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
        MenuPanel panel = currentPanel.getPanel();
        this.setContentPane(panel);
        this.revalidate();
        this.repaint();
        panel.requestFocusInWindow();
        if (panel instanceof GamePanel gamePanel && !gamePanel.isRunning()) {
            gamePanel.startGame(); // <-- solo se è GamePanel
        }
    }

    @Override
    public Dimension getInternalSize() {
        return internalSize;
    }

    @Override
    public void setInternalSize(Dimension size) {
        GameWindow.internalSize = size;
        this.updatePanelsSize();
    }

    @Override
    public Optional<Panels> getLastPanel() {
        return lastPanel;
    }

    @Override
    public Panels getCurrentPanel() {
        return currentPanel;
    }

    @Override
    public void setWindowMode(WindowMode windowMode) {
        Optional.ofNullable(windowMode).ifPresent(mode -> {
            System.out.println("Setting window mode to: " + mode.getText());
            this.dispose();
            switch (mode) {
                case FULLSCREEN -> {
                    this.setUndecorated(true);
                    this.setExtendedState(MAXIMIZED_BOTH);
                }
                case WINDOWED_FULLSCREEN -> {
                    this.setUndecorated(false);
                    this.setExtendedState(MAXIMIZED_BOTH);
                }
                case WINDOW -> {
                    this.setUndecorated(false);
                    this.setExtendedState(NORMAL);
                    this.pack();
                }
            }
            this.setVisible(true);
            this.updateView();
        });
    }

    @Override
    public void setFPS(int fps) {
        System.out.println("Setting FPS to: " + fps);
        ((GamePanel) Panels.GAME_MENU.getPanel()).setFPS(fps);
        // TODO: Implement FPS setting logic in GamePanel
    }

    @Override
    public void toggleViewFps(boolean showFPS) {
        System.out.println("Setting show FPS to: " + showFPS);
        // TODO: Implement FPS display toggle
    }

}
