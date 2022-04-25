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
      task.getOwner().getName(),
      task.getTimeSpent().toHours(),
      task.getEstimatedTimeToFinish().toHours()
    );

    FileWriter writer = null;

    try {
      writer = new FileWriter(TASKSFILEPATH, true);
      writer.append(String.format("%s%s", dbEntry, "\n"));
    } catch (IOException ex) {
        ExceptionUtilities.handleException(ex);
      return DatabaseResponse.FAILED;
    } finally {
      try {
        writer.close();
      } catch (IOException ex) {
        ExceptionUtilities.handleException(ex);
      }
    }
    return DatabaseResponse.OK;
  }

  @Override
  public DatabaseResponse write(User user) {
    String dbEntry = String.format(
      "%s,%s",
      user.getName(),
      user.getDateOfBirth().toString()
    );

    FileWriter writer = null;

    try {
      writer = new FileWriter(USERSFILEPATH, true);
      writer.append(String.format("%s%s", dbEntry, "\n"));
    } catch (IOException ex) {
      ExceptionUtilities.handleException(ex);
      return DatabaseResponse.FAILED;
    } finally {
      try {
        writer.close();
      } catch (IOException ex) {
        ExceptionUtilities.handleException(ex);
      }
    }
    return DatabaseResponse.OK;
  }

  @Override
  public List<TodoItem> fetchAllTasks() {
    List<TodoItem> tasks = new ArrayList<>();
    BufferedReader reader = null;
    String[] currentRowEntries;
    try {
      reader = new BufferedReader(new FileReader(TASKSFILEPATH));
      String currentLine;
      while ((currentLine = reader.readLine()) != null && currentLine.length() >= 1) {
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
          ExceptionUtilities.handleException(ex);
        } catch (NullPointerException ex) {
          ExceptionUtilities.handleException(ex);
        }
      }
    } catch (IOException ex) {
      ExceptionUtilities.handleException(ex);
    } finally {
      try {
        reader.close();
      } catch (IOException ex) {
        ExceptionUtilities.handleException(ex);
      }
    }
    return tasks;
  }

  @Override
  public List<User> fetchAllUsers() {
    List<User> users = new ArrayList<>();
    BufferedReader reader = null;
    String[] currentRowEntries;
    try {
      reader = new BufferedReader(new FileReader(USERSFILEPATH));
      String currentLine;
      while ((currentLine = reader.readLine()) != null && currentLine.length() >= 1) {
        currentRowEntries = currentLine.split(",");
        try {
          users.add(
            new User(
              currentRowEntries[0],
              LocalDate.parse(currentRowEntries[1])
            )
          );
        } catch (NumberFormatException ex) {
          ExceptionUtilities.handleException(ex);
        }
      }
    } catch (IOException ex) {
      ExceptionUtilities.handleException(ex);
    } finally {
      try {
        reader.close();
      } catch (IOException ex) {
        ExceptionUtilities.handleException(ex);
      }
    }
    return users;

  }

  @Override
  public List<TodoItem> fetchTasksForUser(User user) {
    return null;
  }

  @Override
  public User fetchUser(String userName) {
    BufferedReader reader;
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
      ExceptionUtilities.handleException(ex);
    } catch (IOException ex) {
      ExceptionUtilities.handleException(ex);
    }
    return null;
  }

  @Override
  public TodoItem fetchTask(String taskName) {
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(TASKSFILEPATH));
      String currentLine;
      while ((currentLine = reader.readLine()) != null) {
        String[] rowEntries = currentLine.split(",");
        if (taskName.equals(rowEntries[0])) {
          return new TodoItem(
            rowEntries[0],
            fetchUser(rowEntries[1]),
            Double.parseDouble(rowEntries[2]),
            Double.parseDouble(rowEntries[3])
          );
        }
      }
    } catch (FileNotFoundException ex) {
      ExceptionUtilities.handleException(ex);
    } catch (IOException ex) {
      ExceptionUtilities.handleException(ex);
    }
    return null;
  }
}

