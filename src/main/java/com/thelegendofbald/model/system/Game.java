package com.thelegendofbald.model.system;

/**
 * Interface representing a game in the Baldur's Gate universe.
 * Provides methods to control the game state and settings.
 */
public interface Game {

    /**
     * Starts the game.
     */
    void startGame();

    /**
     * Finishes the game, saving the current game run.
     */
    void saveGame();

    /**
     * Checks if the game is currently running.
     *
     * @return true if the game is running, false otherwise.
     */
    boolean isRunning();

    /**
     * Stops the game, cleaning up resources and saving the game state.
     */
    void stopGame();

    /**
     * Pauses the game, freezing the game state and timer.
     */
    void pauseGame();

    /**
     * Resumes the game, unfreezing the game state and timer.
     */
    void resumeGame();

    /**
     * Sets the frames per second (FPS) for the game.
     *
     * @param fps the desired FPS.
     */
    void setFPS(int fps);

    /**
     * Toggles the display of the FPS in the game view.
     *
     * @param showingFPS true to show FPS, false to hide it.
     */
    void setShowingFPS(boolean showingFPS);

}
