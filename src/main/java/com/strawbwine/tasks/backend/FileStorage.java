package com.strawbwine.tasks.backend;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileStorage implements IDatabase {

  Path currentRelativePath = Paths.get("");
  String rootPath = currentRelativePath.toAbsolutePath().toString();
  String workDir = rootPath + "\\src\\main\\java\\com\\strawbwine\\tasks\\backend\\";

  private final String TASKSFILEPATH = workDir + "tasks.txt";
  private final String USERSFILEPATH = workDir + "users.txt";

  public FileStorage() {
  }

  @Override
  public DatabaseResponse write(TodoItem task) {
    String dbEntry = String.format(
      "%s,%s,%d,%d",
      task.getName(),
      task.getOwner(),
      task.getTimeSpent().toHours(),
      task.getEstimatedTimeToFinish().toHours()
    );
    System.out.println(dbEntry);

    FileWriter writer = null;

    try {
      writer = new FileWriter(TASKSFILEPATH, true);
      writer.append(dbEntry + "\n");
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
      System.out.println(ex.getStackTrace());
      return DatabaseResponse.FAILED;
    } finally {
      try {
        writer.flush();
        writer.close();
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
        System.out.println(ex.getStackTrace());
      }
    }
    return DatabaseResponse.OK;
  }

  @Override
  public DatabaseResponse write(User user) {
    return null;
  }

  @Override
  public List<TodoItem> fetchAllTasks() {
    List<TodoItem> tasks = new ArrayList<>();
    BufferedReader reader = null;
    String[] currentRowEntries;
    try {
      reader = new BufferedReader(new FileReader(TASKSFILEPATH));
      String currentLine;
      while ((currentLine = reader.readLine()) != null) {
        currentRowEntries = currentLine.split(",");
        try {
          tasks.add(
            new TodoItem(
              currentRowEntries[0],
              fetchUser(currentRowEntries[1]),
              Double.parseDouble(currentRowEntries[2]),
              Double.parseDouble(currentRowEntries[3])
            )
          );
        } catch (NumberFormatException ex) {
          System.out.println(ex.getMessage());
          System.out.println(ex.getStackTrace());
        }
      }
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    } finally {
      try {
        reader.close();
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
      }
    }
    return tasks;
  }

  @Override
  public List<User> fetchAllUsers() {
    return null;
  }

  @Override
  public List<TodoItem> fetchTasksForUser(User user) {
    return null;
  }

  @Override
  public User fetchUser(String userName) {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(USERSFILEPATH));
      String currentLine;
      while ((currentLine = reader.readLine()) != null) {
        String[] rowEntries = currentLine.split(",");
        if (userName.equals(rowEntries[0])) {
          return new User(rowEntries[0], LocalDate.parse(rowEntries[1]));
        }
      }
    } catch (FileNotFoundException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
    return null;
  }
}
