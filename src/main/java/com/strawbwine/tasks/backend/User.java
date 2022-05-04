package com.strawbwine.tasks.backend;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class User {
    private final String id;
    private String name;
    private LocalDate dateOfBirth;
    private LocalDateTime createdTime = LocalDateTime.now();

    public User(String name, LocalDate dateOfBirth) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public User(String id, String name, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public User(UserDAO dao) {
        this.id = dao.getId();
        this.name = dao.getName();
        this.dateOfBirth = LocalDate.parse(dao.getDate());
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override
    public String toString() {
        return String.format("%20s %20s %20s", this.name, this.dateOfBirth, this.createdTime.toString());
    }
}
