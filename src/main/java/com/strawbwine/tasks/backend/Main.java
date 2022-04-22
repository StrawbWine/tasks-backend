package com.strawbwine.tasks.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        IDatabase storage = new FileStorage();

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
                    gui.showAllTasks(storage.fetchAllTasks());
                    gui.displayOptions();
                }
                case SHOWUSERS -> {
                    gui.showAllUsers(storage.fetchAllUsers());
                    gui.displayOptions();
                }
                case ADDTASK -> {
                    TodoItem newTask = new TodoItem(gui.requestTaskInformation());
                    storage.write(newTask);
                    gui.displayOptions();
                }
                case ADDUSER -> {
                    Map<String, String> userParams = gui.requestUserInformation();
                    storage.write(
                        new User(
                            userParams.get("name"),
                            LocalDate.parse(userParams.get("dateOfBirth"))
                        )
                    );
                }
                case QUIT -> running = false;
            }
        }
    }

    private static List<GUIOption> getGuiOptions() {
        List<GUIOption> options = new ArrayList<>();
        options.add(new GUIOption(1, OptionType.SHOWTASKS, "Show list of tasks"));
        options.add(new GUIOption(2, OptionType.SHOWUSERS, "Show list of users"));
        options.add(new GUIOption(3, OptionType.ADDTASK, "Add new task"));
        options.add(new GUIOption(4, OptionType.WORKONTASK, "Work on task"));
        options.add(new GUIOption(5, OptionType.DELETETASK, "Delete task"));
        options.add(new GUIOption(6, OptionType.QUIT, "Exit application"));
        return options;
    }
}
