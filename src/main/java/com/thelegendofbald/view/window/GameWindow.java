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
 * <p>
 * Extends {@link JFrame} and manages the main container for various game panels
 * (menu, game, settings), handling navigation between them and window property
 * configuration
 * (dimensions, screen mode, icon).
 * </p>
 */
public final class GameWindow extends JFrame implements View, MainView {

    private static final long serialVersionUID = 1L;

    /** Title of the application window. */
    private static final String TITLE = "The Legend of Bald";
    /** Default window width in pixels. */
    private static final int DEFAULT_WINDOW_WIDTH = 1280;
    /** Default window height in pixels. */
    private static final int DEFAULT_WINDOW_HEIGHT = 704;

    private volatile Dimension internalSize = new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);

    /** The panel currently displayed in the window. */
    private Panels currentPanel = Panels.MAIN_MENU;

    /**
     * Optional reference to the previously displayed panel.
     * Useful for "back" navigation logic.
     */
    private transient Optional<Panels> lastPanel = Optional.empty();

    /**
     * Default constructor.
     * Initializes the window and sets default dimensions for all managed panels.
     */
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

    /**
     * Configures initial window properties (title, icon, resize) and makes it
     * visible.
     * Sets the current panel as the content pane.
     */
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

    /**
     * Changes the main panel displayed in the window.
     * Stores the current panel in {@code lastPanel} before swapping.
     *
     * @param panelEnum the identifier of the new panel to display
     */
    @Override
    public void changeMainPanel(final Panels panelEnum) {
        lastPanel = Optional.of(currentPanel);
        currentPanel = panelEnum;
        this.updateView();
    }

    /**
     * Updates the current view, setting the frame content and requesting focus.
     * <p>
     * If the activated panel is a {@link GamePanel} and is not running, starts the
     * game loop.
     * </p>
     */
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

    /**
     * Returns a copy of the current internal window dimensions.
     *
     * @return a new {@link Dimension} object representing width and height
     */
    @Override
    public synchronized Dimension getInternalSize() {
        return (Dimension) internalSize.clone();
    }

    /**
     * Sets new internal window dimensions and updates all panels accordingly.
     *
     * @param size the new dimension to apply
     */
    @Override
    public synchronized void setInternalSize(final Dimension size) {
        internalSize = (Dimension) size.clone();
        this.updatePanelsSize();
    }

    /**
     * Returns, if present, the panel displayed before the current one.
     *
     * @return an {@link Optional} containing the last visited panel, or empty if
     *         none exists
     */
    @Override
    public Optional<Panels> getLastPanel() {
        return lastPanel;
    }

    /**
     * Returns the identifier of the currently displayed panel.
     *
     * @return the current {@link Panels} enum
     */
    @Override
    public Panels getCurrentPanel() {
        return currentPanel;
    }

    /**
     * Sets the window mode (fullscreen, windowed, borderless).
     * Modification requires closing and reopening (dispose/setVisible) the frame.
     *
     * @param windowMode the desired display mode
     */
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

    /**
     * Sets the target FPS (Frames Per Second) for the game panel.
     *
     * @param fps the desired number of frames per second
     */
    @Override
    public void setFPS(final int fps) {
        final Game game = (Game) Panels.GAME_MENU.getPanel();
        game.setFPS(fps);
    }

    /**
     * Enables or disables the on-screen FPS counter.
     *
     * @param showFPS {@code true} to show FPS, {@code false} to hide them
     */
    @Override
    public void toggleViewFps(final boolean showFPS) {
        final Game game = (Game) Panels.GAME_MENU.getPanel();
        game.setShowingFPS(showFPS);
    }

    /**
     * Enables or disables the on-screen game timer.
     *
     * @param showTimer {@code true} to show the timer, {@code false} to hide it
     */
    @Override
    public void toggleViewTimer(final boolean showTimer) {
        final GamePanel game = (GamePanel) Panels.GAME_MENU.getPanel();
        game.setShowingTimer(showTimer);
    }
}
