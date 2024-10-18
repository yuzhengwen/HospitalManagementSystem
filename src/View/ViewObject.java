package View;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Abstract class for all views
 */
public abstract class ViewObject {
    public ArrayList<Action> actions = new ArrayList<>();

    public abstract void showMenu();

    protected void getInput() {
        System.out.println("Enter your choice:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        handleInput(choice);
    }

    public void handleInput(int choice) {
        for (Action action : actions) {
            if (action.choice == choice) {
                action.function.run();
                return;
            }
        }
        // if no choice is matched, show the menu again
        System.out.println("Invalid choice");
        System.out.println();
        showMenu();
    }

    protected void printActions() {
        actions.sort((a1, a2) -> a1.choice - a2.choice);
        for (Action action : actions) {
            System.out.println(action.getString());
        }
    }
}
