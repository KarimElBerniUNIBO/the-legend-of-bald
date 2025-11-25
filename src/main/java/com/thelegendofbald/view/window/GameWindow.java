package com.thelegendofbald.view.window;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.thelegendofbald.model.system.Game;
import com.thelegendofbald.view.panel.base.MenuPanel;
import com.thelegendofbald.view.panel.base.Panels;
import com.thelegendofbald.model.config.WindowMode;
import com.thelegendofbald.view.panel.game.GamePanel;

/**
 * The {@code GameWindow} class represents the main window of the game.
 * It is responsible for displaying the current game panel and managing the game state.
 */
public final class GameWindow extends JFrame implements View, MainView {

    private static final long serialVersionUID = 1L;

    private static final String TITLE = "The Legend of Bald";
    private static final int DEFAULT_WINDOW_WIDTH = 1280;
    private static final int DEFAULT_WINDOW_HEIGHT = 704;

    private volatile Dimension internalSize = new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    private Panels currentPanel = Panels.MAIN_MENU;
    private transient Optional<Panels> lastPanel = Optional.empty();

    /** Default constructor. */
    public GameWindow() {
        super();
        this.updatePanelsSize();
    }

    private synchronized void updatePanelsSize() {
        final Dimension size = this.getInternalSize();
        Arrays.stream(Panels.values())
                .map(Panels::getPanel)
                .forEach(panel -> panel.setPreferredSize(size));
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
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void changeMainPanel(final Panels panelEnum) {
        lastPanel = Optional.of(currentPanel);
        currentPanel = panelEnum;
        this.updateView();
    }

    @Override
    public void updateView() {
        final MenuPanel panel = currentPanel.getPanel();
        this.setContentPane(panel);
        this.revalidate();
        this.repaint();
        panel.requestFocusInWindow();
        if (panel instanceof GamePanel gamePanel && !gamePanel.isRunning()) {
            gamePanel.startGame();
            gamePanel.requestFocusInWindow();
        }
    }

    @Override
    public synchronized Dimension getInternalSize() {
        return (Dimension) internalSize.clone();
    }

    @Override
    public synchronized void setInternalSize(final Dimension size) {
        internalSize = (Dimension) size.clone();
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
    public void setWindowMode(final WindowMode windowMode) {
        Optional.ofNullable(windowMode).ifPresent(mode -> {
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
    public void setFPS(final int fps) {
        final Game game = (Game) Panels.GAME_MENU.getPanel();
        game.setFPS(fps);
    }

    @Override
    public void toggleViewFps(final boolean showFPS) {
        final Game game = (Game) Panels.GAME_MENU.getPanel();
        game.setShowingFPS(showFPS);
    }

    @Override
    public void toggleViewTimer(final boolean showTimer) {
        final GamePanel game = (GamePanel) Panels.GAME_MENU.getPanel();
        game.setShowingTimer(showTimer);
    }
}
