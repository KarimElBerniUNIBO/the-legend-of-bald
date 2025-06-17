package com.thelegendofbald.view.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.panels.MenuPanel;
import com.thelegendofbald.api.settingsmenu.VideoSettings;
import com.thelegendofbald.characters.Bald;
import com.thelegendofbald.characters.DummyEnemy;
import com.thelegendofbald.model.common.Timer;
import com.thelegendofbald.model.common.Timer.TimeData;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.view.inventory.InventoryPanel;
import com.thelegendofbald.view.main.GameWindow;
import com.thelegendofbald.view.main.GridPanel;
import com.thelegendofbald.view.main.TileMap;

public class GamePanel extends MenuPanel implements Runnable {

    private static final double OPTIONS_WIDTH_INSETS = 0.25;
    private static final double OPTIONS_HEIGHT_INSETS = 0.1;

    private static final double INVENTORY_WIDTH_INSETS = 0.25;
    private static final double INVENTORY_HEIGHT_INSETS = 0.25;

    private static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 20);

    private static final Pair<Integer, Integer> FPS_POSITION = Pair.of(15, 25);
    private static final Pair<Integer, Integer> TIMER_POSITION = Pair.of(1085, 25);

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints optionsGBC = gbcFactory.createBothGridBagConstraints();
    private final GridBagConstraints inventoryGBC = gbcFactory.createBothGridBagConstraints();

    private final Bald bald = new Bald(60, 60, 100, "Bald", 50);
    private final DummyEnemy dummyenemy = new DummyEnemy(500, 200, 50, "ZioBilly", 50);
    private final GridPanel gridPanel;
    private final TileMap tileMap;
    private final JPanel optionsPanel;
    private final JPanel inventoryPanel;
    private final Timer timer = new Timer();

    private Thread gameThread;
    private boolean running = false;

    private int maxFPS = (int) VideoSettings.FPS.getValue();
    private boolean showingFPS = (boolean) VideoSettings.SHOW_FPS.getValue();
    private int currentFPS = 0;
    private boolean showingTimer = (boolean) VideoSettings.SHOW_TIMER.getValue();

    private final Set<Integer> pressedKeys = new HashSet<>();

    public GamePanel() {
        super();
        Dimension size = new Dimension(1280, 704);
        // this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setLayout(new GridBagLayout());

        this.gridPanel = new GridPanel();
        this.gridPanel.setOpaque(false);
        this.gridPanel.setBounds(0, 0, size.width, size.height);

        this.optionsPanel = new GameOptionsPanel(size);
        this.inventoryPanel = new InventoryPanel("INVENTORY", size, 5, 3);

        this.tileMap = new TileMap(size.width, size.height);
        this.requestFocusInWindow();

        setupKeyBindings();
        // this.startGame();

        SwingUtilities.invokeLater(() -> this.requestFocusInWindow());
    }

    private void setupKeyBindings() {
        InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        // Tasti premuti
        bindKey(im, am, "pressed UP", KeyEvent.VK_UP, true, () -> pressedKeys.add(KeyEvent.VK_UP));
        bindKey(im, am, "pressed DOWN", KeyEvent.VK_DOWN, true, () -> pressedKeys.add(KeyEvent.VK_DOWN));
        bindKey(im, am, "pressed LEFT", KeyEvent.VK_LEFT, true, () -> pressedKeys.add(KeyEvent.VK_LEFT));
        bindKey(im, am, "pressed RIGHT", KeyEvent.VK_RIGHT, true, () -> pressedKeys.add(KeyEvent.VK_RIGHT));

        // Tasti rilasciati
        bindKey(im, am, "released UP", KeyEvent.VK_UP, false, () -> pressedKeys.remove(KeyEvent.VK_UP));
        bindKey(im, am, "released DOWN", KeyEvent.VK_DOWN, false, () -> pressedKeys.remove(KeyEvent.VK_DOWN));
        bindKey(im, am, "released LEFT", KeyEvent.VK_LEFT, false, () -> pressedKeys.remove(KeyEvent.VK_LEFT));
        bindKey(im, am, "released RIGHT", KeyEvent.VK_RIGHT, false, () -> pressedKeys.remove(KeyEvent.VK_RIGHT));
    }

    private void bindKey(InputMap im, ActionMap am, String name, int key, boolean pressed, Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0, !pressed), name);
        am.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                action.run();
            }
        });
    }

    private static final double MOVE_SPEED = 2.0;

    private void handleInput() {
        double dx = 0;
        double dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_LEFT))
            dx -= 1;
        if (pressedKeys.contains(KeyEvent.VK_RIGHT))
            dx += 1;
        if (pressedKeys.contains(KeyEvent.VK_UP))
            dy -= 1;
        if (pressedKeys.contains(KeyEvent.VK_DOWN))
            dy += 1;

        // Normalizza il vettore per garantire velocità costante
        double magnitude = Math.hypot(dx, dy); // meglio di sqrt(x^2 + y^2)
        if (magnitude > 0) {
            dx = (dx / magnitude) * MOVE_SPEED;
            dy = (dy / magnitude) * MOVE_SPEED;
        }

        bald.setSpeedX(dx);
        bald.setSpeedY(dy);
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
        timer.start();
    }

    @Override
    public void run() {
        running = true;
        System.out.println("Game loop started!");

        long lastTime = System.nanoTime();
        double interval = 0;
        int drawCount = 0;
        long updateInterval = 0;
        double delta = 0;

        while (true) {

            long now = System.nanoTime();
            updateInterval += (now - lastTime);
            interval = 1e9 / maxFPS;
            delta += (now - lastTime) / interval;
            lastTime = now;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++; // check FPS
            }

            if (updateInterval >= 1e9) {
                // System.out.println("FPS:" + drawCount);
                currentFPS = drawCount;
                drawCount = 0;
                updateInterval = 0;
            }

        }
    }

    private void update() {

        handleInput();
        bald.move();
        dummyenemy.followPlayer(bald);
        dummyenemy.updateAnimation();
        repaint();
        // System.out.printf("dx: %.3f dy: %.3f%n", bald.getSpeedX(), bald.getSpeedY());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        super.paintComponent(g2d);

        this.scaleGraphics(g2d);
        tileMap.render(g2d);
        gridPanel.paintComponent(g2d);
        bald.render(g2d);
        dummyenemy.render(g2d);
        this.drawFPS(g2d);
        this.drawTimer(g2d);

        g2d.dispose();
    }

    private void drawFPS(Graphics g) {
        if (showingFPS) {
            g.setColor(Color.YELLOW);
            g.setFont(DEFAULT_FONT);
            g.drawString("FPS: " + currentFPS, FPS_POSITION.getLeft(), FPS_POSITION.getRight());
        }
    }

    private void drawTimer(Graphics g) {
        if (showingTimer) {
            TimeData timeData = timer.getFormattedTime();

            g.setColor(Color.WHITE);
            g.setFont(DEFAULT_FONT);

            g.drawString(
                    String.format("Timer: %02d:%02d:%02d", timeData.hours(), timeData.minutes(), timeData.seconds()),
                    TIMER_POSITION.getLeft(), TIMER_POSITION.getRight());
        }
    }

    private void scaleGraphics(Graphics g) {
        Optional.ofNullable(SwingUtilities.getWindowAncestor(this))
                .filter(window -> window instanceof GameWindow)
                .map(window -> (GameWindow) window)
                .ifPresent(window -> {
                    double scaleX = this.getWidth() / window.getInternalSize().getWidth();
                    double scaleY = this.getHeight() / window.getInternalSize().getHeight();
                    ((Graphics2D) g).scale(scaleX, scaleY);
                });
    }

    @Override
    public void updateComponentsSize() {
        optionsGBC.insets.set((int) (this.getHeight() * OPTIONS_HEIGHT_INSETS),
                (int) (this.getWidth() * OPTIONS_WIDTH_INSETS), (int) (this.getHeight() * OPTIONS_HEIGHT_INSETS),
                (int) (this.getWidth() * OPTIONS_WIDTH_INSETS));
        inventoryGBC.insets.set((int) (this.getHeight() * INVENTORY_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_WIDTH_INSETS), (int) (this.getHeight() * INVENTORY_HEIGHT_INSETS),
                (int) (this.getWidth() * INVENTORY_WIDTH_INSETS));
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        this.add(gridPanel);
        this.add(optionsPanel, optionsGBC);
        this.add(inventoryPanel, inventoryGBC);
    }

    public boolean isRunning() {
        return running;
    }

    public void stopGame() {
        this.running = false;
    }

    public void setFPS(int fps) {
        this.maxFPS = fps;
    }

    public void setShowingFPS(boolean showingFPS) {
        this.showingFPS = showingFPS;
    }

    public boolean isShowingFPS() {
        return showingFPS;
    }

    public void setShowingTimer(boolean showingTimer) {
        this.showingTimer = showingTimer;
    }

    public boolean isShowingTimer() {
        return showingTimer;
    }

}
