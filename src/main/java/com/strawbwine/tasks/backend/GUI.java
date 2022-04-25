package com.strawbwine.tasks.backend;

import java.time.LocalDate;
import java.util.*;

public class GUI {

    private final Scanner scanner = new Scanner(System.in);
    private final List<GUIOption> options;
    private final IDatabase storage;

    public GUI(List<GUIOption> options, IDatabase storage) {
        this.options = options;
        this.storage = storage;
    }

    public void startUpMessage() {
        System.out.println(stars());
        System.out.println("Welcome to Todo App!");
        System.out.println(stars());
    }

    public String stars(int number) {
        return "*".repeat(number);
    }

    public String stars() {
        return stars(20);
    }

    public void displayOptions() {
        System.out.println("Please choose an option:");
        options.forEach(System.out::println);
    }

    public int receiveOptionFromUser() {
        String input = scanner.nextLine();
        if (validateOptionFromUser(input)) {
            return Integer.parseInt(input);
        }
        else {
            System.out.println("Invalid input, please try again");
            displayOptions();
            return receiveOptionFromUser();
        }
    }

    public boolean validateOptionFromUser(String s) {
        List<Integer> validInput = options.stream().map(GUIOption::getId).toList();
        try {
            int parsedInt = Integer.parseInt(s);
            return validInput.contains(parsedInt);
        } catch (NumberFormatException ex) {
            return false;
        }

    }

    public void showAllTasks(List<TodoItem> tasks) {
        if (tasks.size() >= 1) {
            tasks.forEach(task -> System.out.println(task.toString()));
        } else {
            System.out.println("No tasks to show!");
        }
    }

    public TaskSpec requestTaskInformation() {
        TaskSpec taskSpec = new TaskSpec();
        System.out.println("Please enter the name of the task");
        String taskName = scanner.nextLine();
        taskSpec.setName(taskName);
        System.out.println("Please enter the name of the owner of this task");
        String ownerName = scanner.nextLine();
        User storedUser = storage.fetchUser(ownerName);
        if (storedUser != null) {
            taskSpec.setOwner(storedUser);
        } else {
            System.out.println("User not found in database, please enter date of birth of user (YYYY-MM-DD)");
            String dateOfBirthString = scanner.nextLine();
            User newUser = new User(ownerName, LocalDate.parse(dateOfBirthString));
            storage.write(newUser);
            taskSpec.setOwner(newUser);
        }
        System.out.println("Please enter the estimated time to finish this task");
        taskSpec.setEstimatedTimeToFinishInHours(Double.parseDouble(scanner.nextLine()));
        return taskSpec;
    }

    public Map<String, String> requestUserInformation() {
        Map<String, String> userParams = new HashMap<>();
        System.out.println("Please enter the name of the user");
        userParams.put("name", scanner.nextLine());
        boolean inputHasProperFormat = false;
        String dateOfBirthString = null;
        while (!inputHasProperFormat) {
            System.out.println("Please enter the day of birth of the user in format YYYY-MM-DD");
            dateOfBirthString = scanner.nextLine();
            try {
                LocalDate.parse(dateOfBirthString);
                inputHasProperFormat = true;
            } catch (Exception ex) {
                System.out.println("Illegal date format");
            }
        }
        userParams.put("dateOfBirth", dateOfBirthString);
        return userParams;
    }

    public void showAllUsers(List<User> users) {
        if (users.size() >= 1) {
            users.forEach(user -> System.out.println(user.toString()));
        } else {
            System.out.println("No users to show!");
        }
    }

    public void returnToMainMenu() {
        System.out.println("Click enter to return to main menu");
        scanner.nextLine();
    }

    public TodoItem requestTaskName() {
        boolean validInput = false;
        System.out.println("Please enter the name of the task to work on:");
        while (!validInput) {
            String input = scanner.nextLine();
            TodoItem response = storage.fetchTask(input);
            if (response != null) {
                return response;
            } else {
                System.out.println("Task not found, please try again");
            }
        }
        return null;
    }
}
