package com.strawbwine.tasks.backend;

import java.time.ZoneOffset;

public class TodoItemDAO {
  private String id;
  private String discriminator;
  private String name;
  private UserDAO owner;
  private long secondsSpent;
  private long estimatedSecondsToFinish;
  private long createdTime;

  public TodoItemDAO() {}

  public TodoItemDAO(String id, String name, UserDAO owner, long secondsSpent, long estimatedSecondsToFinish, long createdTime) {
    this.id = id;
    this.name = name;
    this.owner = owner;
    this.secondsSpent = secondsSpent;
    this.estimatedSecondsToFinish = estimatedSecondsToFinish;
    this.createdTime = createdTime;
    this.discriminator = "TodoItem";
  }

  public TodoItemDAO(TodoItem task) {
    this.id = task.getId();
    this.name = task.getName();
    this.owner = new UserDAO(task.getOwner());
    this.secondsSpent = task.getTimeSpent().toSeconds();
    this.estimatedSecondsToFinish = task.getEstimatedTimeToFinish().toSeconds();
    this.createdTime = task.getCreatedTime().toEpochSecond(ZoneOffset.UTC);
    this.discriminator = "TodoItem";
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

  public UserDAO getOwner() {
    return owner;
  }

  public void setOwner(UserDAO owner) {
    this.owner = owner;
  }

  public long getSecondsSpent() {
    return secondsSpent;
  }

  public void setSecondsSpent(int secondsSpent) {
    this.secondsSpent = secondsSpent;
  }

  public long getEstimatedSecondsToFinish() {
    return estimatedSecondsToFinish;
  }

  public void setEstimatedSecondsToFinish(int estimatedSecondsToFinish) {
    this.estimatedSecondsToFinish = estimatedSecondsToFinish;
  }

  public long getCreatedTime() {
    return createdTime;
  }

  public String getDiscriminator() {
    return discriminator;
  }

  public void setDiscriminator(String discriminator) {
    this.discriminator = discriminator;
  }
}
