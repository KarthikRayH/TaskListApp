import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskListApp {
    private static final String WELCOME_MESSAGE = """
            Personal Task Manager
            Your friendly task companion!
            """;
    
    private static final String[] ENCOURAGEMENTS = {
        "Great job staying organized!",
        "You're doing amazing at managing your tasks!",
        "Keep up the excellent work!",
        "You're on fire with productivity!",
        "Way to stay on top of things!"
    };
    
    private static String userName = "";
    
    public static void main(String[] args) {
        TaskList taskList = new TaskList();
        Scanner scanner = new Scanner(System.in);
        
        // Welcome and get user's name
        System.out.println(WELCOME_MESSAGE);
        getUserName(scanner);
        
        System.out.println("\nHello " + userName + "! I'm here to help you manage your tasks efficiently.");
        System.out.println("Let's get organized and make your day productive!\n");
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getUserChoice(scanner);
            
            switch (choice) {
                case 1:
                    handleAddTask(taskList, scanner);
                    break;
                case 2:
                    handleRemoveTask(taskList, scanner);
                    break;
                case 3:
                    handleListTasks(taskList);
                    break;
                case 4:
                    handleMarkComplete(taskList, scanner);
                    break;
                case 5:
                    handleClearAllTasks(taskList, scanner);
                    break;
                case 6:
                    running = handleExit(scanner);
                    break;
                default:
                    System.out.println("Hmm, that's not a valid option. Please choose a number between 1-6.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                clearScreen();
            }
        }
        
        scanner.close();
    }
    
    private static void getUserName(Scanner scanner) {
        System.out.print("Before we start, what's your name? ");
        userName = scanner.nextLine().trim();
        if (userName.isEmpty()) {
            userName = "Friend";
        }
    }
    
    private static void displayMenu() {
        System.out.println("MAIN MENU");
        System.out.println("===================");
        System.out.println("1. Add a new task");
        System.out.println("2. Remove a task");
        System.out.println("3. View all tasks");
        System.out.println("4. Mark task as complete");
        System.out.println("5. Clear all tasks");
        System.out.println("6. Exit application");
        System.out.println("===================");
        System.out.print("\n" + userName + ", what would you like to do? (1-6): ");
    }
    
    private static int getUserChoice(Scanner scanner) {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return choice;
        } catch (NumberFormatException e) {
            return -1; // Invalid choice
        }
    }
    
    private static void handleAddTask(TaskList taskList, Scanner scanner) {
        System.out.println("\nLet's add a new task to your list!");
        System.out.print("What task would you like to add? ");
        String taskName = scanner.nextLine().trim();
        
        if (taskName.isEmpty()) {
            System.out.println("Oops! Task name can't be empty. Let's try again.");
            return;
        }
        
        System.out.print("Would you like to add a description? (y/n): ");
        String addDesc = scanner.nextLine().trim().toLowerCase();
        String description = "";
        
        if (addDesc.equals("y") || addDesc.equals("yes")) {
            System.out.print("Enter task description: ");
            description = scanner.nextLine().trim();
        }
        
        taskList.addTask(taskName, description);
        System.out.println("\nPerfect! Task added successfully!");
        if (taskList.getTaskCount() % 5 == 0) {
            System.out.println(getRandomEncouragement());
        }
    }
    
    private static void handleRemoveTask(TaskList taskList, Scanner scanner) {
        if (taskList.isEmpty()) {
            System.out.println("\nYour task list is already empty! Nothing to remove.");
            return;
        }
        
        System.out.println("\nLet's remove a task from your list:");
        taskList.listTasks();
        
        System.out.print("\nWhich task would you like to remove? (Enter task number): ");
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine().trim());
            
            if (taskList.isValidTaskNumber(taskNumber)) {
                String removedTask = taskList.getTaskName(taskNumber);
                System.out.print("Are you sure you want to remove '" + removedTask + "'? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                
                if (confirm.equals("y") || confirm.equals("yes")) {
                    taskList.removeTask(taskNumber);
                    System.out.println("Task removed successfully! One less thing to worry about.");
                } else {
                    System.out.println("No problem! Task kept in your list.");
                }
            } else {
                System.out.println("That task number doesn't exist. Please check your list and try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid task number.");
        }
    }
    
    private static void handleListTasks(TaskList taskList) {
        if (taskList.isEmpty()) {
            System.out.println("\nCongratulations! Your task list is completely empty!");
            System.out.println("You're either super productive or it's time to add some tasks!");
        } else {
            System.out.println("\nHere are your current tasks, " + userName + ":");
            taskList.listTasks();
            System.out.println("Total tasks: " + taskList.getTaskCount() + 
                             " | Completed: " + taskList.getCompletedCount() + 
                             " | Remaining: " + taskList.getRemainingCount());
        }
    }
    
    private static void handleMarkComplete(TaskList taskList, Scanner scanner) {
        if (taskList.isEmpty()) {
            System.out.println("\nNo tasks to mark as complete. Add some tasks first!");
            return;
        }
        
        System.out.println("\nLet's mark a task as complete:");
        taskList.listTasks();
        
        System.out.print("\nWhich task did you complete? (Enter task number): ");
        try {
            int taskNumber = Integer.parseInt(scanner.nextLine().trim());
            
            if (taskList.isValidTaskNumber(taskNumber)) {
                taskList.markTaskComplete(taskNumber);
                System.out.println("Awesome! Task marked as complete. You're crushing it!");
            } else {
                System.out.println("That task number doesn't exist. Please check your list and try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid task number.");
        }
    }
    
    private static void handleClearAllTasks(TaskList taskList, Scanner scanner) {
        if (taskList.isEmpty()) {
            System.out.println("\nYour task list is already empty!");
            return;
        }
        
        System.out.println("\nThis will remove ALL tasks from your list.");
        System.out.print("Are you absolutely sure? Type 'YES' to confirm: ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equals("YES")) {
            taskList.clearAllTasks();
            System.out.println("All tasks cleared! Fresh start, " + userName + "!");
        } else {
            System.out.println("Smart choice! Your tasks are safe.");
        }
    }
    
    private static boolean handleExit(Scanner scanner) {
        System.out.println("\nThanks for using the Task Manager, " + userName + "!");
        System.out.println("Remember: Small steps lead to big achievements!");
        System.out.print("Are you sure you want to exit? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            System.out.println("\nGoodbye! Stay productive and have a wonderful day!");
            return false;
        } else {
            System.out.println("Great! Let's continue managing your tasks.");
            return true;
        }
    }
    
    private static String getRandomEncouragement() {
        return ENCOURAGEMENTS[(int) (Math.random() * ENCOURAGEMENTS.length)];
    }
    
    private static void clearScreen() {
        System.out.print("\n".repeat(2));
    }
}

class TaskList {
    private ArrayList<Task> tasks = new ArrayList<>();
    
    public void addTask(String name, String description) {
        tasks.add(new Task(name, description));
    }
    
    public void removeTask(int taskNumber) {
        if (isValidTaskNumber(taskNumber)) {
            tasks.remove(taskNumber - 1);
        }
    }
    
    public void markTaskComplete(int taskNumber) {
        if (isValidTaskNumber(taskNumber)) {
            tasks.get(taskNumber - 1).markComplete();
        }
    }
    
    public void listTasks() {
        System.out.println();
        System.out.println("==================================================");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            String status = task.isCompleted() ? "[COMPLETED]" : "[PENDING]";
            
            System.out.printf("%s %d. %s\n", status, (i + 1), task.getName());
            
            if (!task.getDescription().isEmpty()) {
                System.out.println("    Description: " + task.getDescription());
            }
            
            System.out.println("    Created: " + task.getCreatedDate());
            if (task.isCompleted()) {
                System.out.println("    Completed: " + task.getCompletedDate());
            }
            System.out.println();
        }
        System.out.println("==================================================");
    }
    
    public void clearAllTasks() {
        tasks.clear();
    }
    
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
    
    public boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }
    
    public String getTaskName(int taskNumber) {
        if (isValidTaskNumber(taskNumber)) {
            return tasks.get(taskNumber - 1).getName();
        }
        return "";
    }
    
    public int getTaskCount() {
        return tasks.size();
    }
    
    public int getCompletedCount() {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }
    
    public int getRemainingCount() {
        return getTaskCount() - getCompletedCount();
    }
}

class Task {
    private String name;
    private String description;
    private boolean completed;
    private String createdDate;
    private String completedDate;
    
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.completed = false;
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
        this.completedDate = "";
    }
    
    public void markComplete() {
        this.completed = true;
        this.completedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public String getCreatedDate() { return createdDate; }
    public String getCompletedDate() { return completedDate; }
}