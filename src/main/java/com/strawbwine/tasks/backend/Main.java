package com.strawbwine.tasks.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        IDatabase storage = new CosmosDB();

        List<GUIOption> options = getGuiOptions();
        GUI gui = new GUI(options, storage);

        gui.startUpMessage();
        gui.displayOptions();

        boolean running = true;

        while (running) {
            int userInput = gui.receiveOptionFromUser();

            GUIOption selectedOption = options.stream().filter(option -> option.getId() == userInput).toList().get(0);

            switch (selectedOption.getOptionType()) {
                case SHOWTASKS -> {
                    System.out.println("Listing tasks...");
                    System.out.println(gui.stars());
                    gui.showAllTasks(storage.fetchAllTasks());
                    System.out.println(gui.stars());
                    gui.returnToMainMenu();
                    gui.displayOptions();
                }
                case SHOWUSERS -> {
                    System.out.println("Listing users...");
                    System.out.println(gui.stars());
                    gui.showAllUsers(storage.fetchAllUsers());
                    System.out.println(gui.stars());
                    gui.returnToMainMenu();
                    gui.displayOptions();
                }
                case ADDTASK -> {
                    TodoItem newTask = new TodoItem(gui.requestTaskInformation());
                    storage.write(newTask);
                    System.out.printf("Added task: %s, %s%n", newTask.getName(), newTask.getOwner().getName());
                    gui.returnToMainMenu();
                    gui.displayOptions();
                }
                case ADDUSER -> {
                    Map<String, String> userParams = gui.requestUserInformation();
                    User newUser = new User(
                        userParams.get("name"),
                        LocalDate.parse(userParams.get("dateOfBirth"))
                    );
                    storage.write(newUser);
                    System.out.printf("Added user: %s, %s%n", newUser.getName(), newUser.getDateOfBirth().toString());
                    gui.returnToMainMenu();
                    gui.displayOptions();
                }
                case WORKONTASK -> {
                    TodoItem taskToWorkOn = gui.requestTaskName();
                }
                case QUIT -> running = false;
            }
        }
    }

    private static List<GUIOption> getGuiOptions() {
        List<GUIOption> options = new ArrayList<>();
        options.add(new GUIOption(1, OptionType.SHOWTASKS, "Show list of tasks"));
        options.add(new GUIOption(2, OptionType.SHOWUSERS, "Show list of users"));
        options.add(new GUIOption(3, OptionType.ADDUSER, "Add new user"));
        options.add(new GUIOption(4, OptionType.ADDTASK, "Add new task"));
        options.add(new GUIOption(5, OptionType.WORKONTASK, "Work on task"));
        options.add(new GUIOption(6, OptionType.DELETETASK, "Delete task"));
        options.add(new GUIOption(7, OptionType.QUIT, "Exit application"));
        return options;
    }
}
