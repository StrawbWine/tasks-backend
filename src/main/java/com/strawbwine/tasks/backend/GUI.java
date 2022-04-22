package com.strawbwine.tasks.backend;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

public class GUI {

    private Scanner scanner = new Scanner(System.in);
    private List<GUIOption> options;
    private IDatabase storage;

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
        StringBuilder sb = new StringBuilder(100);
        for (int i = 0; i < number; i++) {
            sb.append("*");
        }
        return sb.toString();
    }

    public String stars() {
        return stars(20);
    }

    public void displayOptions() {
        System.out.println("Please choose an option:");
        options.forEach(System.out::println);
    }

    public int receiveInputFromUser() {
        String input = scanner.nextLine();
        if (validateInputFromUser(input)) {
            return Integer.parseInt(input);
        }
        else {
            System.out.println("Invalid input, please try again");
            displayOptions();
            return receiveInputFromUser();
        }
    }

    public boolean validateInputFromUser(String s) {
        List<Integer> validInput = options.stream().map(GUIOption::getId).toList();
        try {
            int parsedInt = Integer.parseInt(s);
            if (validInput.contains(parsedInt)) return true;
            else return false;
        } catch (NumberFormatException ex) {
            return false;
        }

    }

    public void showAllTasks(List<TodoItem> tasks) {
        tasks.forEach(task -> System.out.println(task.toString()));
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
        Boolean inputHasProperFormat = false;
        String dateOfBirthString = null;
        while (!inputHasProperFormat) {
            System.out.println("Please enter the day of birth of the user in format YYYY-MM-DD");
            dateOfBirthString = scanner.nextLine();
            try {
                LocalDate.parse(dateOfBirthString);
            } catch (Exception ex) {
                System.out.println("Illegal date format");
            }
        }
        userParams.put("dateOfBirth", dateOfBirthString);
        return userParams;
    }
}
