package com.strawbwine.tasks.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final IDatabase cosmos = new CosmosDB();
  @GetMapping("/user")
  public UserDAO user(@RequestParam(value = "name") String name) {
    return new UserDAO(cosmos.fetchUser(name));
  }
}
