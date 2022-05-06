package com.strawbwine.tasks.backend;

import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

  private final IDatabase cosmos = new CosmosDB();

  @GetMapping(value = "/task", produces = "application/json")
  public TodoItemDAO getUser(@RequestParam(value = "name") String name) {
    return new TodoItemDAO(cosmos.fetchTask(name));
  }

  @PostMapping(value = "/task", consumes = "application/json", produces = "application/json")
  public TodoItemDAO postUser(@RequestBody TodoItemDAO taskDAO) {
    cosmos.write(new TodoItem(taskDAO));
    return taskDAO;
  }
}