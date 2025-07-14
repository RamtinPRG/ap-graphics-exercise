package com.ramtinprg.model;

public class GameTimer {
    private float duration;
    private float timeRemaining; // in seconds
    private boolean finished = false;

    public GameTimer(float totalSeconds) {
        this.duration = totalSeconds;
        this.timeRemaining = totalSeconds;
    }

    public void update(float delta) {
        if (finished)
            return;
        timeRemaining -= delta;
        if (timeRemaining <= 0) {
            timeRemaining = 0;
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public float getPassedTime() {
        return this.duration - this.timeRemaining;
    }

    public String getFormattedTime() {
        int seconds = (int) timeRemaining;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
