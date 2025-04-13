import java.util.Scanner;

public class Tower {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mainMenu();
    }

    public static void mainMenu() {
        while (true) {
            System.out.println("=== Tower of Hanoi ===");
            System.out.println("1. Start Game");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    startGame();
                    break;
                case "2":
                    if (confirmExit()) {
                        System.out.println("Exiting game. Goodbye!");
                        return;
                    }
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void startGame() {
        System.out.print("Enter number of disks: ");
        int disks = Integer.parseInt(scanner.nextLine());

        System.out.println("Press Q at any time to return to the main menu.");
        System.out.println("Solving Tower of Hanoi for " + disks + " disks...\n");

        simulateGame(disks, 'A', 'C', 'B');
        System.out.println("Puzzle solved!");
        System.out.println();
    }

    // Simulates the recursive Tower of Hanoi and handles quitting to menu
    public static void simulateGame(int n, char from, char to, char aux) {
        if (n == 0) return;

        // Check if user wants to quit
        if (checkQuit()) return;

        simulateGame(n - 1, from, aux, to);

        System.out.println("Move disk " + n + " from " + from + " to " + to);

        if (checkQuit()) return;

        simulateGame(n - 1, aux, to, from);
    }

    // Checks for user input to quit mid-game
    public static boolean checkQuit() {
        System.out.print("Press Enter to continue or Q to quit: ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("Q")) {
            if (confirmExit()) {
                System.out.println("Returning to main menu...");
                System.out.println();
                return true;
            }
        }
        return false;
    }

    // Confirmation view for exiting or returning to menu
    public static boolean confirmExit() {
        System.out.print("Are you sure you want to quit? (Y/N): ");
        String confirm = scanner.nextLine();
        return confirm.equalsIgnoreCase("Y");
    }
}
