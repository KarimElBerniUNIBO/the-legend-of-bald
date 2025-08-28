package com.thelegendofbald.model.common;

/**
 * Timer class to measure elapsed time.
 * It can be started, stopped, reset, and resumed.
 * Provides formatted time data in hours, minutes, and seconds.
 */
public final class Timer {

    private long startTime;
    private long pausedTime;
    private boolean running;

    /**
     * Default constructor initializes the timer.
     * The timer is not running initially.
     */
    public Timer() {
        this.startTime = 0;
        this.pausedTime = 0;
        this.running = false;
    }

    /**
     * Starts the timer.
     * The timer begins counting from the current time.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        pausedTime = 0;
        running = true;
    }

    /**
     * Stops the timer.
     * It calculates the elapsed time since the timer was started.
     */
    public void stop() {
        if (running) {
            pausedTime = System.currentTimeMillis() - startTime;
            running = false;
        }
    }

    /**
     * Resets the timer.
     * It sets the start time to the current time and resets elapsed time to zero.
     */
    public void reset() {
        this.startTime = System.currentTimeMillis();
        this.pausedTime = 0;
    }

    /**
     * Resumes the timer.
     * The timer continues counting from where it left off.
     */
    public void resume() {
        if (!running) {
            startTime = System.currentTimeMillis() - pausedTime;
            running = true;
        }
    }

    /**
     * Gets the elapsed time since the timer was started.
     * It includes any time accumulated while the timer was stopped.
     *
     * @return The total elapsed time in milliseconds.
     */
    public long getElapsedTime() {
        if (running) {
            return System.currentTimeMillis() - startTime;
        } else {
            return pausedTime;
        }
    }

    /**
     * Gets the formatted time data.
     * It returns the elapsed time in a structured format of hours, minutes, and seconds.
     *
     * @return A TimeData object containing hours, minutes, and seconds.
     */
    public TimeData getFormattedTime() {
        final long totalSeconds = getElapsedTime() / 1000;
        final int seconds = (int) (totalSeconds % 60);
        final int minutes = (int) (totalSeconds / 60) % 60;
        final int hours = (int) (totalSeconds / 3600);

        return new TimeData(hours, minutes, seconds);
    }

    /**
     * Record to hold formatted time data.
     * It provides a string representation in the format HH:MM:SS.
     * 
     * @param hours   The number of hours.
     * @param minutes The number of minutes.
     * @param seconds The number of seconds.
     */
    public record TimeData(int hours, int minutes, int seconds) {
        @Override
        public String toString() {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

}
