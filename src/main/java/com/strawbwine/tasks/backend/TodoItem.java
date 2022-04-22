package com.strawbwine.tasks.backend;

import java.sql.Time;
import java.time.Duration;

public class TodoItem {
    private String name;
    private User owner;
    private Duration timeSpent = Duration.ZERO;
    private Duration estimatedTimeToFinish;

    public TodoItem(String name, User owner, Duration estimatedTimeToFinish) {
        this.name = name;
        this.owner = owner;
        this.estimatedTimeToFinish = estimatedTimeToFinish;
    }

    public TodoItem(String name, User owner, double hours) throws NegativeDurationException {
        this.name = name;
        this.owner = owner;
        setEstimatedTimeToFinish(TimeUtilities.fromHoursToDuration(hours));
    }

    public TodoItem(String name, User owner, double timeSpent, double estimatedTimeToFinish) {
        this.name = name;
        this.owner = owner;
        setTimeSpent(TimeUtilities.fromHoursToDuration(timeSpent));
        setEstimatedTimeToFinish(TimeUtilities.fromHoursToDuration(estimatedTimeToFinish));
    }

    public TodoItem(TaskSpec taskSpec) {
        this.name = taskSpec.getName();
        this.owner = taskSpec.getOwner();
        if (taskSpec.getEstimatedTimeToFinishDuration() != null)
            this.estimatedTimeToFinish = taskSpec.getEstimatedTimeToFinishDuration();
        else
            setEstimatedTimeToFinish(TimeUtilities.fromHoursToDuration(taskSpec.getEstimatedTimeToFinishInHours()));
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

    public Duration getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Duration timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Duration getEstimatedTimeToFinish() {
        return estimatedTimeToFinish;
    }

    public void setEstimatedTimeToFinish(Duration estimatedTimeToFinish) {
        if (estimatedTimeToFinish.isNegative()) {
            throw new NegativeDurationException("Supplied estimatedTimeToFinish must be a positive Duration");
        }
        this.estimatedTimeToFinish = estimatedTimeToFinish;
    }

    @Override
    public String toString() {
        return String.format("%15s %15s %15s %15s", name, owner.toString(), timeSpent.toString(), estimatedTimeToFinish.toString());
    }
}
