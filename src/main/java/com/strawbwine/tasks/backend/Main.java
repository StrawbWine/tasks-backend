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

        while (true) {
            int userInput = gui.receiveInputFromUser();

            GUIOption selectedOption = options.stream().filter(option -> option.getId() == userInput).toList().get(0);

            switch (selectedOption.getOptionType()) {
                case SHOWTASKS -> {
                    gui.showAllTasks(storage.fetchAllTasks());
                    break;
                }
                case ADDTASK -> {
                    TodoItem newTask = new TodoItem(gui.requestTaskInformation());
                    storage.write(newTask);
                    gui.displayOptions();
                    break;
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
                case QUIT -> {}
            }
/*            if (selectedOption.getOptionType() == OptionType.SHOWTASKS) {
                gui.showAllTasks(storage.fetchAllTasks());
            }

            if (selectedOption.getOptionType() == OptionType.ADDTASK) {
                TodoItem newTask = new TodoItem(gui.requestTaskInformation());
                storage.write(newTask);
                gui.displayOptions();
            }*/

/*            if(selectedOption.getOptionType() == OptionType.QUIT)
                break;*/
        }




/*        try {
            TodoItem todoItem = new TodoItem("vaske", new User("Severin"), 3.50);
            System.out.println(
                    String.format(
                            "%s skal %s i %d timer",
                            todoItem.getOwner().getName(),
                            todoItem.getName(),
                            todoItem.getEstimatedTimeToFinish().toHours()
                    )
            );
        } catch (NegativeDurationException ex) {
            System.out.println("Estimated time to finish task must be positive");
            System.err.println(ex.getMessage());
        }*/


    }

    private static List<GUIOption> getGuiOptions() {
        List<GUIOption> options = new ArrayList<GUIOption>();
        options.add(new GUIOption(1, OptionType.SHOWTASKS, "Show list of tasks"));
        options.add(new GUIOption(2, OptionType.ADDTASK, "Add new task"));
        options.add(new GUIOption(3, OptionType.WORKONTASK, "Work on task"));
        options.add(new GUIOption(4, OptionType.DELETETASK, "Delete task"));
        options.add(new GUIOption(5, OptionType.QUIT, "Exit application"));
        return options;
    }
}
