package com.thelegendofbald.model.common;

/**
 * Timer class to measure elapsed time.
 * It can be started, stopped, reset, and resumed.
 * Provides formatted time data in hours, minutes, and seconds.
 */

public final class Timer {

    private long startTime;
    private long elapsedTime;
    private boolean running;

    /**
     * Default constructor initializes the timer.
     * The timer is not running initially.
     */
    public Timer() {
        this.startTime = 0;
        this.elapsedTime = 0;
        this.running = false;
    }

    /**
     * Starts the timer.
     * The timer begins counting from the current time.
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
        this.running = true;
    }

    /**
     * Stops the timer.
     * It calculates the elapsed time since the timer was started.
     */
    public void stop() {
        if (running) {
            this.elapsedTime += System.currentTimeMillis() - this.startTime;
            this.running = false;
        }
    }

    /**
     * Resets the timer.
     * It sets the start time to the current time and resets elapsed time to zero.
     */
    public void reset() {
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
        this.running = false;
    }

    /**
     * Resumes the timer.
     * The timer continues counting from where it left off.
     */
    public void resume() {
        if (!running) {
            this.startTime = System.currentTimeMillis();
            this.running = true;
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
            return this.elapsedTime + System.currentTimeMillis() - this.startTime;
        } else {
            return this.elapsedTime;
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
