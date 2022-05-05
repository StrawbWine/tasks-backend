package com.strawbwine.tasks.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

  private final IDatabase cosmos = new CosmosDB();
  @GetMapping(value = "/task", produces = "application/json")
  public TodoItemDAO user(@RequestParam(value = "name") String name) {
    return new TodoItemDAO(cosmos.fetchTask(name));
  }
}