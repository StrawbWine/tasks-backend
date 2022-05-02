package com.strawbwine.tasks.backend;

import java.util.UUID;

public class UserDAO {
  private String id;
  private String name;
  private String date;

  public UserDAO() {}

  public UserDAO(String id, String name, String date) {
    this.id = id;
    this.name = name;
    this.date = date;
  }

  public UserDAO(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.date = user.getDateOfBirth().toString();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getId() {
    return id;
  }
}
