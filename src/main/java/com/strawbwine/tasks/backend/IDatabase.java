package com.strawbwine.tasks.backend;

import java.util.List;

public interface IDatabase {
    DatabaseResponse write(TodoItem task);
    DatabaseResponse write(User user);
    List<TodoItem> fetchAllTasks();
    List<User> fetchAllUsers();
    List<TodoItem> fetchTasksForUser(User user);
    User fetchUser(String userName);
}
