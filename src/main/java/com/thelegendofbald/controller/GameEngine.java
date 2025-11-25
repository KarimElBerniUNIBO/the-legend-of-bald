package com.thelegendofbald.controller;

import java.util.concurrent.locks.LockSupport;

import javax.swing.SwingUtilities;

import com.thelegendofbald.controller.input.InputController;
import com.thelegendofbald.controller.level.LevelManager;
import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.model.entity.DummyEnemy;
import com.thelegendofbald.model.system.CombatManager;
import com.thelegendofbald.model.system.Timer;
import com.thelegendofbald.view.panel.game.GamePanel;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Manages the main game loop, updates, and game state.
 */
public final class GameEngine implements Runnable {

    private static final long NANOS_IN_SECOND = 1_000_000_000L;
    private static final long MILLIS_IN_SECOND = 1000L;
    private static final long NANOS_IN_MILLI = 1_000_000L;
    private static final long SLEEP_INTERVAL_WHEN_PAUSED = 100L;
    private static final long LATE_FRAME_BACKOFF_NANOS = 250_000L;
    private static final int DEFAULT_MAX_FPS = 60;
    private static final long PORTAL_COOLDOWN_MS = 2000;
    private static final int ID_NEXT_MAP_TRIGGER = 10;
    private static final int ID_PREV_PORTAL = 8;

    private final GamePanel gamePanel;
    private final Bald bald;
    private final LevelManager levelManager;
    private final CombatManager combatManager;
    private final InputController inputController;
    private final Timer timer;

    private Thread gameThread;
    private volatile boolean running;
    private volatile boolean paused;
    private volatile boolean gameOver;
    private volatile boolean gameWon;
    private volatile int maxFPS = DEFAULT_MAX_FPS;
    private volatile int currentFPS;

    private volatile long portalCooldownUntil;

    /**
     * Constructs a new GameEngine.
     *
     * @param gamePanel       the game panel.
     * @param bald            the player character.
     * @param levelManager    the level manager.
     * @param combatManager   the combat manager.
     * @param inputController the input controller.
     * @param timer           the game timer.
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "GameEngine orchestrates these specific instances."
    )
    public GameEngine(final GamePanel gamePanel, final Bald bald, final LevelManager levelManager,
            final CombatManager combatManager, final InputController inputController, final Timer timer) {
        this.gamePanel = gamePanel;
        this.bald = bald;
        this.levelManager = levelManager;
        this.combatManager = combatManager;
        this.inputController = inputController;
        this.timer = timer;
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        this.running = true;
        gameThread = new Thread(this);
        gameThread.start();
        timer.start();
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        this.running = false;
        this.timer.stop();
    }

    /**
     * Pauses the game.
     */
    public void pause() {
        this.paused = true;
        this.timer.stop();
    }

    /**
     * Resumes the game.
     */
    public void resume() {
        this.paused = false;
        this.timer.resume();
    }

    /**
     * Checks if the game is running.
     *
     * @return true if running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Checks if the game is paused.
     *
     * @return true if paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets the maximum FPS.
     *
     * @param maxFPS the maximum FPS.
     */
    public void setMaxFPS(final int maxFPS) {
        this.maxFPS = maxFPS;
    }

    /**
     * Returns the current FPS.
     *
     * @return the current FPS.
     */
    public int getCurrentFPS() {
        return currentFPS;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if game over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Checks if the game is won.
     *
     * @return true if game won, false otherwise.
     */
    public boolean isGameWon() {
        return gameWon;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        int frames = 0;
        long fpsTimer = System.currentTimeMillis();

        while (gameThread != null && running) {
            if (paused) {
                LockSupport.parkNanos(SLEEP_INTERVAL_WHEN_PAUSED * NANOS_IN_MILLI);
                lastTime = System.nanoTime();
                continue;
            }

            final long now = System.nanoTime();
            final double deltaTime = (now - lastTime) / (double) NANOS_IN_SECOND;
            lastTime = now;

            update(deltaTime);
            gamePanel.repaint();

            frames++;
            if (System.currentTimeMillis() - fpsTimer >= MILLIS_IN_SECOND) {
                currentFPS = frames;
                frames = 0;
                fpsTimer += MILLIS_IN_SECOND;
            }

            final long frameTime = (System.nanoTime() - now) / NANOS_IN_MILLI;
            final long targetFrameTime = MILLIS_IN_SECOND / Math.max(1, maxFPS);
            final long sleepTime = targetFrameTime - frameTime;

            if (sleepTime > 0) {
                LockSupport.parkNanos(sleepTime * NANOS_IN_MILLI);
            } else {
                LockSupport.parkNanos(LATE_FRAME_BACKOFF_NANOS);
            }
        }
    }

    private void update(final double deltaTime) {
        if (gameOver || gameWon) {
            return;
        }
        inputController.handleInput();
        bald.updateAnimation();
        bald.move(levelManager.getTileMap(), deltaTime);
        bald.updateBuffs();

        if (System.currentTimeMillis() >= portalCooldownUntil) {
             if (levelManager.isBaldTouchingTile(ID_NEXT_MAP_TRIGGER)) {
                 levelManager.switchToNextMap();
                 portalCooldownUntil = System.currentTimeMillis() + PORTAL_COOLDOWN_MS;
                 return;
             }
             if (levelManager.isBaldTouchingTile(ID_PREV_PORTAL)) {
                 levelManager.switchToPreviousMap();
                 portalCooldownUntil = System.currentTimeMillis() + PORTAL_COOLDOWN_MS;
                 return;
             }
        }

        combatManager.checkEnemyAttacks();
        levelManager.getEnemies().removeIf(DummyEnemy::isRemovable);
        levelManager.getEnemies().forEach(enemy -> {
            if (enemy.isCloseTo(bald)) {
                enemy.followPlayer(bald);
                enemy.updateAnimation();
            }
        });

        if (levelManager.getBoss() != null && levelManager.getBoss().isAlive()) {
            levelManager.getBoss().followPlayer(bald);
            levelManager.getBoss().updateAnimation();
        }
        combatManager.getProjectiles().forEach(p -> p.move(levelManager.getTileMap()));
        combatManager.checkProjectiles();

        levelManager.getItemManager().updateAll();
        levelManager.getItemManager().handleItemCollection(bald);

        SwingUtilities.invokeLater(gamePanel::checkIfNearShopTile);

        if (!bald.isAlive() && !gameOver) {
            handleGameOver();
        }
        if (levelManager.getBoss() != null && !levelManager.getBoss().isAlive()
                && !gameWon && !gameOver) {
            handleGameWon();
        }
    }

    private void handleGameWon() {
        this.gameWon = true;
        this.pause();
        gamePanel.saveGame();
        inputController.clearPressedKeys();
        SwingUtilities.invokeLater(gamePanel::showMainMenuButton);
    }

    private void handleGameOver() {
        this.gameOver = true;
        this.pause();
        inputController.clearPressedKeys();
        SwingUtilities.invokeLater(gamePanel::showMainMenuButton);
    }

    /**
     * Resets the game engine state.
     */
    public void reset() {
        this.gameOver = false;
        this.gameWon = false;
        this.paused = false;
        inputController.clearPressedKeys();

        levelManager.reset();
    }
}
