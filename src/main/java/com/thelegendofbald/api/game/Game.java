package com.thelegendofbald.api.game;

public interface Game {

    void startGame();

    void finishGame();

    boolean isRunning();

    void stopGame();

    void setFPS(int fps);

    void setShowingFPS(boolean showingFPS);

}