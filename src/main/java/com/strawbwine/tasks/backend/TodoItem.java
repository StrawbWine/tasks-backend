package com.strawbwine.tasks.backend;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class TodoItem {
    private final String id;
    private String name;
    private User owner;
    private Duration timeSpent = Duration.ZERO;
    private Duration estimatedTimeToFinish;
    private LocalDateTime createdTime = LocalDateTime.now();

    public TodoItem(String name, User owner, Duration estimatedTimeToFinish) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.owner = owner;
        this.estimatedTimeToFinish = estimatedTimeToFinish;
    }

    public TodoItem(String name, User owner, double hours) throws NegativeDurationException {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.owner = owner;
        setEstimatedTimeToFinish(TimeUtilities.fromHoursToDuration(hours));
    }

    public TodoItem(String name, User owner, double timeSpent, double estimatedTimeToFinish) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.owner = owner;
        setTimeSpent(TimeUtilities.fromHoursToDuration(timeSpent));
        setEstimatedTimeToFinish(TimeUtilities.fromHoursToDuration(estimatedTimeToFinish));
    }

    public TodoItem(String id, String name, User owner, double timeSpent, double estimatedTimeToFinish) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        setTimeSpent(TimeUtilities.fromHoursToDuration((timeSpent)));
        setEstimatedTimeToFinish(TimeUtilities.fromHoursToDuration((estimatedTimeToFinish)));
    }

    public TodoItem(TaskSpec taskSpec) {
        this.id = UUID.randomUUID().toString();
        this.name = taskSpec.getName();
        this.owner = taskSpec.getOwner();
        if (taskSpec.getEstimatedTimeToFinishDuration() != null)
            this.estimatedTimeToFinish = taskSpec.getEstimatedTimeToFinishDuration();
        else
            setEstimatedTimeToFinish(TimeUtilities.fromHoursToDuration(taskSpec.getEstimatedTimeToFinishInHours()));
    }

    public TodoItem(TodoItemDAO dao) {
        this.id = dao.getId();
        this.name = dao.getName();
        this.owner = new User(dao.getOwner());
        this.timeSpent = Duration.ofSeconds(dao.getSecondsSpent());
        this.estimatedTimeToFinish = Duration.ofSeconds(dao.getEstimatedSecondsToFinish());
        this.createdTime = LocalDateTime.ofEpochSecond(dao.getCreatedTime(), 0, ZoneOffset.UTC);
    }

    public String getId() {
        return id;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override
    public String toString() {
        return String.format("%20s %20s %20s %20s %20s", name, owner.toString(), timeSpent.toString(), estimatedTimeToFinish.toString(), createdTime.toString());
    }
}
