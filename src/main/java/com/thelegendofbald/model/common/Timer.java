package com.thelegendofbald.model.common;

public class Timer {

    public static record TimeData(int hours, int minutes, int seconds) {
        @Override
        public String toString() {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    private long startTime;
    private long elapsedTime;

    public Timer() {
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
    }

    public void stop() {
        this.elapsedTime += System.currentTimeMillis() - this.startTime;
    }

    public void reset() {
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
    }

    public void resume() {
        this.startTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return this.elapsedTime + (System.currentTimeMillis() - this.startTime);
    }

    public TimeData getFormattedTime() {
        long totalSeconds = getElapsedTime() / 1000;
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours = (int) (totalSeconds / 3600);

        return new TimeData(hours, minutes, seconds);
    }

}
