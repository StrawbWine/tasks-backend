package com.strawbwine.tasks.backend;

import java.time.Duration;

public class TodoItemDAO {
  private String id;
  private String name;
  private UserDAO owner;
  private long secondsSpent;
  private long estimatedSecondsToFinish;

  public TodoItemDAO() {}

  public TodoItemDAO(String id, String name, UserDAO owner, long secondsSpent, long estimatedSecondsToFinish) {
    this.id = id;
    this.name = name;
    this.owner = owner;
    this.secondsSpent = secondsSpent;
    this.estimatedSecondsToFinish = estimatedSecondsToFinish;
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
}
