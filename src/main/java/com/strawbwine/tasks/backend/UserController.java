package com.strawbwine.tasks.backend;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

  private final IDatabase cosmos = new CosmosDB();

  @GetMapping(value = "/user", produces = "application/json")
  public UserDAO getUser(@RequestParam(value = "name") String name) {
    return new UserDAO(cosmos.fetchUser(name));
  }

  @PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
  public UserDAO postUser(@RequestBody UserDAO userDAO) {
    cosmos.write(new User(userDAO));
    return userDAO;
  }
}
