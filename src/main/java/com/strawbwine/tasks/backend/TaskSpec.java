package com.strawbwine.tasks.backend;

import java.time.Duration;

public class TaskSpec {
    private String name;
    private User owner;
    private Duration estimatedTimeToFinishDuration;
    private double estimatedTimeToFinishInHours;

    public TaskSpec() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Duration getEstimatedTimeToFinishDuration() {
        return estimatedTimeToFinishDuration;
    }

    public void setEstimatedTimeToFinishDuration(Duration estimatedTimeToFinishDuration) {
        this.estimatedTimeToFinishDuration = estimatedTimeToFinishDuration;
    }

    public double getEstimatedTimeToFinishInHours() {
        return estimatedTimeToFinishInHours;
    }

    public void setEstimatedTimeToFinishInHours(double estimatedTimeToFinishInHours) {
        this.estimatedTimeToFinishInHours = estimatedTimeToFinishInHours;
    }
}
