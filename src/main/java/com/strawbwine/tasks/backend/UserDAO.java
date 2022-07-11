package com.strawbwine.tasks.backend;

import java.time.ZoneOffset;

public class UserDAO {
  private String id;
  private String discriminator;
  private String name;
  private String date;
  private long createdTime;

  public UserDAO() {}

  public UserDAO(String id, String name, String date) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.discriminator = "User";
  }

  public UserDAO(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.date = user.getDateOfBirth().toString();
    this.createdTime = user.getCreatedTime().toEpochSecond(ZoneOffset.UTC);
    this.discriminator = "User";
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
